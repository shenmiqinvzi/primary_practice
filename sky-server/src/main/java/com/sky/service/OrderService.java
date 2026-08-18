package com.sky.service;

import com.sky.dto.OrderPageQueryDTO;
import com.sky.dto.OrderSubmitDTO;
import com.sky.dto.OrdersDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     */
    OrderSubmitVO submitOrder(OrderSubmitDTO dto);

    PageResult pageQuery(OrderPageQueryDTO dto);

    OrderVO getDetail(Long id);

    OrderStatisticsVO statistics();

    void confirm(OrdersDTO dto);

    void cancel(OrdersDTO dto);

    void delivery(Long id);

    void complete(Long id);

    void rejection(OrdersDTO dto);
}