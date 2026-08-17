package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.constant.OrderStatusConstant;
import com.sky.dto.OrderSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BaseException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        AddressBook addressBook = addressBookMapper.getById(dto.getAddressBookId());
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

        // 10. 组装返回结果
        return OrderSubmitVO.builder()
                .id(orderId)
                .orderNumber(orderNumber)
                .orderAmount(totalAmount)
                .orderTime(orders.getOrderTime())
                .build();
    }
}