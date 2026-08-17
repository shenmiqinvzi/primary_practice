package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitVO implements Serializable {

    private Long id;                // 订单ID
    private String orderNumber;     // 订单号（唯一）
    private BigDecimal orderAmount; // 订单总金额
    private LocalDateTime orderTime; // 下单时间
}