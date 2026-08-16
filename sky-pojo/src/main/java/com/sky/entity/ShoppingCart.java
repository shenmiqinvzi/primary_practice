package com.sky.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCart implements Serializable{
    private static final long serialVersionUID=1L;

    private Long id;
    private String name;          // 商品名称（冗余存储）
    private String image;         // 图片路径（冗余存储）
    private Long userId;          // 用户ID
    private Long dishId;          // 菜品ID（和 setmealId 互斥）
    private Long setmealId;       // 套餐ID（和 dishId 互斥）
    private String dishFlavor;    // 口味（只有 dish 有）
    private Integer number;       // 数量
    private BigDecimal amount;    // 单价
    private LocalDateTime createTime;
}
