package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.OrderStatusConstant;
import com.sky.constant.PayStatusConstant;
import com.sky.dto.OrderPageQueryDTO;
import com.sky.dto.OrderSubmitDTO;
import com.sky.dto.OrdersDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BaseException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional  // 事务：任一失败全部回滚
    public OrderSubmitVO submitOrder(OrderSubmitDTO dto) {
        Long userId = BaseContext.getCurrentId();

        // 1. 查询地址簿（校验是否存在，并取收货信息）
        AddressBook addressBook = addressBookMapper.getByIdAndUserId(dto.getAddressBookId(), userId);
        if (addressBook == null) {
            throw new BaseException("地址不存在，请重新选择");
        }

        // 2. 查询购物车（不能为空）
        List<ShoppingCart> cartList = shoppingCartMapper.listByUserId(userId);
        if (cartList == null || cartList.isEmpty()) {
            throw new BaseException("购物车为空，请先添加商品");
        }

        // 3. 计算订单总金额（服务端算，不用前端传）
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ShoppingCart cart : cartList) {
            BigDecimal itemTotal = cart.getAmount().multiply(new BigDecimal(cart.getNumber()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // 4. 生成订单号（时间戳 + 随机4位数）
        String orderNumber = System.currentTimeMillis() + String.format("%04d", new Random().nextInt(10000));

        // 5. 构建 Orders 对象
        Orders orders = new Orders();
        orders.setNumber(orderNumber);
        orders.setStatus(OrderStatusConstant.PENDING_PAYMENT);   // 待付款
        orders.setUserId(userId);
        orders.setAddressBookId(dto.getAddressBookId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(0);                       // 未支付
        orders.setPayMethod(dto.getPayMethod());
        orders.setAmount(totalAmount);
        orders.setRemark(dto.getRemark());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setUserName(addressBook.getConsignee());
        orders.setConsignee(addressBook.getConsignee());

        // 餐具相关（前端没传则默认 0）
        orders.setTablewareNumber(dto.getTablewareNumber() != null ? dto.getTablewareNumber() : 0);
        orders.setTablewareStatus(dto.getTablewareStatus() != null ? dto.getTablewareStatus() : 0);
        orders.setPackAmount(BigDecimal.ZERO);        // 打包费可后续计算
        orders.setDeliveryStatus(0);                  // 未配送

        // 6. 插入订单主表（useGeneratedKeys 回填 id）
        orderMapper.insert(orders);
        Long orderId = orders.getId();  // 拿到数据库生成的订单ID

        // 7. 构建订单明细列表
        List<OrderDetail> detailList = new ArrayList<>();
        for (ShoppingCart cart : cartList) {
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(orderId);
            detail.setDishId(cart.getDishId());
            detail.setSetmealId(cart.getSetmealId());
            detail.setDishFlavor(cart.getDishFlavor());
            detail.setName(cart.getName());
            detail.setImage(cart.getImage());
            detail.setNumber(cart.getNumber());
            detail.setAmount(cart.getAmount());
            detailList.add(detail);
        }

        // 8. 批量插入订单明细
        orderDetailMapper.insertBatch(detailList);

        // 9. 清空购物车
        shoppingCartMapper.deleteByUserId(userId);

        log.info("下单成功，订单号：{}，用户ID：{}，总金额：{}", orderNumber, userId, totalAmount);

        // 下单成功后，推送来单提醒
        WebSocketServer.sendToAll("来单提醒：" + orderNumber);
        log.info("下单成功，订单号：{}，用户ID：{}，总金额：{}", orderNumber, userId, totalAmount);
        // 10. 组装返回结果
        return OrderSubmitVO.builder()
                .id(orderId)
                .orderNumber(orderNumber)
                .orderAmount(totalAmount)
                .orderTime(orders.getOrderTime())
                .build();
    }

    @Override
    public PageResult pageQuery(OrderPageQueryDTO dto){
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        List<Orders> list=orderMapper.pageQuery(dto);
        PageInfo<Orders> pageInfo=new PageInfo<>(list);

        List<OrderVO> voList=list.stream().map(order->{
            OrderVO vo=new OrderVO();
            BeanUtils.copyProperties(order,vo);
            List<OrderDetail>details=orderDetailMapper.getByOrderId(order.getId());
            String orderDishes=details.stream()
                                .map(d->d.getName()+"x"+d.getNumber())
                                .collect(Collectors.joining(","));
            vo.setOrderDishes(orderDishes);
            return vo;
        }).collect(Collectors.toList());

        return new PageResult(pageInfo.getTotal(), voList);
    }

    @Override
    public OrderVO getDetail(Long id){
        Orders order=orderMapper.getById(id);
        if(order==null) throw new BaseException("订单不存在");
        List<OrderDetail>details=orderDetailMapper.getByOrderId(id);

        OrderVO vo=new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setOrderDetailList(details);
        return vo;
    }

    @Override
    public OrderStatisticsVO statistics(){
        return orderMapper.countByStatus();
    }

    @Override
    @Transactional
    public void confirm(OrdersDTO dto){
        Orders order=orderMapper.getById(dto.getId());
        if(order==null) throw new BaseException("订单不存在");
        if(!order.getStatus().equals(OrderStatusConstant.TO_BE_CONFIRMED)) throw new BaseException("当前状态不允许接单");
        Orders update=new Orders();
        update.setId(dto.getId());
        update.setStatus(OrderStatusConstant.CONFIRMED);
        orderMapper.updateStatus(update);
    }

    @Override
    @Transactional
    public void rejection(OrdersDTO dto) {
        Orders order = orderMapper.getById(dto.getId());
        if (order == null) {
            throw new BaseException("订单不存在");
        }
        // 校验状态：只有待接单(2)才能拒单
        if (!order.getStatus().equals(OrderStatusConstant.TO_BE_CONFIRMED)) {
            throw new BaseException("当前订单状态不允许拒单");
        }
        if (dto.getRejectionReason() == null || dto.getRejectionReason().isEmpty()) {
            throw new BaseException("拒单原因不能为空");
        }

        Orders update = new Orders();
        update.setId(dto.getId());
        update.setStatus(OrderStatusConstant.REFUND);
        update.setRejectionReason(dto.getRejectionReason());
        update.setCancelTime(LocalDateTime.now());
        orderMapper.reject(update);
        log.info("拒单成功，订单ID：{}，原因：{}", dto.getId(), dto.getRejectionReason());
    }


    @Override
    @Transactional
    public void cancel(OrdersDTO dto) {
        Orders order = orderMapper.getById(dto.getId());
        if (order == null) {
            throw new BaseException("订单不存在");
        }
        // 校验状态：只有待接单(2)或已接单(3)才能取消
        if (!order.getStatus().equals(OrderStatusConstant.TO_BE_CONFIRMED)
                && !order.getStatus().equals(OrderStatusConstant.CONFIRMED)) {
            throw new BaseException("当前订单状态不允许取消");
        }
        if (dto.getCancelReason() == null || dto.getCancelReason().isEmpty()) {
            throw new BaseException("取消原因不能为空");
        }

        Orders update = new Orders();
        update.setId(dto.getId());
        update.setStatus(OrderStatusConstant.CANCELLED);
        update.setCancelReason(dto.getCancelReason());
        update.setCancelTime(LocalDateTime.now());
        orderMapper.cancel(update);
        log.info("取消成功，订单ID：{}，原因：{}", dto.getId(), dto.getCancelReason());
    }

    @Override
    @Transactional
    public void delivery(Long id) {
        Orders order = orderMapper.getById(id);
        if (order == null) {
            throw new BaseException("订单不存在");
        }
        // 校验状态：只有已接单(3)才能派送
        if (!order.getStatus().equals(OrderStatusConstant.CONFIRMED)) {
            throw new BaseException("当前订单状态不允许派送");
        }

        Orders update = new Orders();
        update.setId(id);
        update.setStatus(OrderStatusConstant.DELIVERY_IN_PROGRESS);
        update.setDeliveryStatus(1); // 待配送 → 配送中
        update.setDeliveryTime(LocalDateTime.now());
        orderMapper.updateStatus(update);
        log.info("派送成功，订单ID：{}", id);
    }


    @Override
    @Transactional
    public void complete(Long id) {
        Orders order = orderMapper.getById(id);
        if (order == null) {
            throw new BaseException("订单不存在");
        }
        // 校验状态：只有派送中(4)才能完成
        if (!order.getStatus().equals(OrderStatusConstant.DELIVERY_IN_PROGRESS)) {
            throw new BaseException("当前订单状态不允许完成");
        }

        Orders update = new Orders();
        update.setId(id);
        update.setStatus(OrderStatusConstant.COMPLETED);
        orderMapper.updateStatus(update);
        log.info("完成成功，订单ID：{}", id);
    }

    @Override
    @Transactional
    public void payment(String orderNumber){
        Orders orders=orderMapper.getByNumber(orderNumber);
        if(orders==null) throw new BaseException("订单不存在");
        if (!BaseContext.getCurrentId().equals(orders.getUserId())) throw new BaseException("无权操作该订单");

        if (!orders.getStatus().equals(OrderStatusConstant.PENDING_PAYMENT)) {
            throw new BaseException("当前订单状态不允许支付");
        }
        Orders update=new Orders();
        update.setId(orders.getId());
        update.setStatus(OrderStatusConstant.TO_BE_CONFIRMED);  // 待付款 → 待接单
        update.setPayStatus(PayStatusConstant.PAID);                                 // 未支付 → 已支付
        update.setCheckoutTime(LocalDateTime.now());
        orderMapper.updateStatus(update);

        log.info("支付成功：订单号={}", orderNumber);

    // 4. WebSocket 推送来单提醒（管理端）
        String message = "来单提醒：" + orderNumber;
        WebSocketServer.sendToAll(message);
        log.info("WebSocket 推送来单提醒：{}", message);
    }

    @Override
    public PageResult pageQueryByUser(OrderPageQueryDTO dto){
        dto.setUserId(BaseContext.getCurrentId());
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        List<Orders>list=orderMapper.pageQueryByUser(dto);
        PageInfo<Orders>pageInfo=new PageInfo<>(list);
        List<OrderVO> voList=list.stream().map(order->{
            OrderVO vo=new OrderVO();
            BeanUtils.copyProperties(order, vo);
            List<OrderDetail>details=orderDetailMapper.getByOrderId(order.getId());
            vo.setOrderDetailList(details);
            return vo;
        }).collect(Collectors.toList());
        return new PageResult(pageInfo.getTotal(), voList);
    }

    @Override
    public OrderVO getUserOrderDetail(Long id){
        Orders order=orderMapper.getById(id);
        if (order == null) {
        throw new BaseException("订单不存在");
    }

    // 2. 越权校验：当前用户只能看自己的订单
    Long currentUserId = BaseContext.getCurrentId();
    if (!currentUserId.equals(order.getUserId())) {
        throw new BaseException("无权查看该订单");
    
    }
    List<OrderDetail>details=orderDetailMapper.getByOrderId(id);
    OrderVO vo=new OrderVO();
    BeanUtils.copyProperties(order, vo);
    vo.setOrderDetailList(details);
    return vo;
  }

  @Override
  @Transactional
  public void userCancel(OrdersDTO dto){
    Orders order=orderMapper.getById(dto.getId());
    if (order == null) {
        throw new BaseException("订单不存在");
    }

    // 越权校验：只能取消自己的订单
    Long currentUserId = BaseContext.getCurrentId();
    if (!currentUserId.equals(order.getUserId())) {
        throw new BaseException("无权操作该订单");
    }

    // 2. 校验状态：只有待付款(1)能用户取消
    if (!order.getStatus().equals(OrderStatusConstant.PENDING_PAYMENT)) {
        throw new BaseException("当前订单状态不允许取消");
    }
    Orders update = new Orders();
    update.setId(dto.getId());
    update.setStatus(OrderStatusConstant.CANCELLED);
    update.setCancelReason("用户取消");
    update.setCancelTime(LocalDateTime.now());
    orderMapper.updateStatus(update);

    log.info("用户取消订单成功，订单号：{}", order.getNumber());
  }

  @Override
  public void reminder(Long id) {
    // 1. 查订单
    Orders order = orderMapper.getById(id);
    if (order == null) {
        throw new BaseException("订单不存在");
    }
    if (!BaseContext.getCurrentId().equals(order.getUserId())) {
        throw new BaseException("无权操作该订单");
    }
    if (!OrderStatusConstant.CONFIRMED.equals(order.getStatus())) {
        throw new BaseException("当前订单状态不允许催单");
    }

    // 2. 组装 JSON 消息（对接黑马前端协议）
    Map<String, Object> message = new HashMap<>();
    message.put("type", 2);          // type=2 表示催单
    message.put("orderId", id);
    message.put("content", "订单号：" + order.getNumber());

    String jsonMessage = "";
    try {
        jsonMessage = new ObjectMapper().writeValueAsString(message);
    } catch (Exception e) {
        throw new BaseException("催单消息序列化失败");
    }
    WebSocketServer.sendToAll(jsonMessage);

    log.info("催单消息已推送，订单号：{}", order.getNumber());
  }

}
