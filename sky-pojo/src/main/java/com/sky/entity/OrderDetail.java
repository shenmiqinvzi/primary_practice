package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long orderId;        // 订单ID
    private Long dishId;         // 菜品ID（和 setmealId 互斥）
    private Long setmealId;      // 套餐ID（和 dishId 互斥）
    private String dishFlavor;   // 口味
    private String name;         // 商品名称（冗余）
    private String image;        // 图片路径（冗余）
    private Integer number;      // 数量
    private BigDecimal amount;   // 单价
}