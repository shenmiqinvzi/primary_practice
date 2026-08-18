package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersPaymentDTO implements Serializable {

    /** 订单号（微信支付回调用，模拟支付也用它查订单） */
    private String orderNumber;
}