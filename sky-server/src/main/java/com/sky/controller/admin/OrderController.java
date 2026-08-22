package com.sky.controller.admin;

import com.sky.dto.OrderPageQueryDTO;
import com.sky.dto.OrdersDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "管理端订单接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("分页条件查询订单")
    public Result<PageResult> conditionSearch(OrderPageQueryDTO dto) {
        log.info("分页条件查询订单：{}", dto);
        PageResult pageResult = orderService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation("订单数量统计")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO vo = orderService.statistics();
        return Result.success(vo);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details(@PathVariable Long id) {
        log.info("查询订单详情：id={}", id);
        OrderVO vo = orderService.getDetail(id);
        return Result.success(vo);
    }

    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrdersDTO dto) {
        log.info("接单：{}", dto);
        orderService.confirm(dto);
        return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersDTO dto) {
        log.info("拒单：{}", dto);
        orderService.rejection(dto);
        return Result.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersDTO dto) {
        log.info("取消订单：{}", dto);
        orderService.cancel(dto);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("派送")
    public Result delivery(@PathVariable Long id) {
        log.info("派送：id={}", id);
        orderService.delivery(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("完成")
    public Result complete(@PathVariable Long id) {
        log.info("完成：id={}", id);
        orderService.complete(id);
        return Result.success();
    }
}
