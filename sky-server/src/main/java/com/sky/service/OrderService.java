package com.sky.service;

import com.sky.dto.OrderSubmitDTO;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {

    /**
     * 用户下单
     */
    OrderSubmitVO submitOrder(OrderSubmitDTO dto);
}