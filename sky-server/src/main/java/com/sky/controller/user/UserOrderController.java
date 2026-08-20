package com.sky.controller.user;

import com.sky.dto.OrderPageQueryDTO;
import com.sky.dto.OrdersDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
@Api(tags = "用户端订单接口")
@Slf4j
public class UserOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/historyOrders")
    @ApiOperation("历史订单分页查询")
    public Result<PageResult> historyOrders(OrderPageQueryDTO dto) {
        log.info("用户端查询历史订单：{}", dto);
        PageResult pageResult = orderService.pageQueryByUser(dto);
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        log.info("用户端查询订单详情：id={}", id);
        OrderVO vo = orderService.getUserOrderDetail(id);
        return Result.success(vo);
    }

    @PutMapping("/cancel")
    @ApiOperation("用户取消订单")
    public Result cancel(@RequestBody OrdersDTO dto) {
        log.info("用户取消订单：{}", dto);
        orderService.userCancel(dto);
        return Result.success();
    }

    @GetMapping("/reminder/{id}")
    @ApiOperation("催单")
    public Result reminder(@PathVariable Long id) {
        log.info("用户催单：id={}", id);
        orderService.reminder(id);
        return Result.success();
    }
}