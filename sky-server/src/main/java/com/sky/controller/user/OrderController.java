package com.sky.controller.user;

import com.sky.dto.OrderSubmitDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/order")
@Api(tags = "用户端订单接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("提交订单")
    public Result<OrderSubmitVO> submit(@RequestBody OrderSubmitDTO dto) {
        log.info("用户下单：{}", dto);
        OrderSubmitVO vo = orderService.submitOrder(dto);
        return Result.success(vo);
    }

    @PutMapping("/payment")
    @ApiOperation("模拟支付")
    public Result payment(@RequestBody OrdersPaymentDTO dto) {
        log.info("模拟支付：订单号={}", dto.getOrderNumber());
        orderService.payment(dto.getOrderNumber());
        return Result.success();
}
}