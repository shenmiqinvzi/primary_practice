package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐下的菜品展示对象（用户端）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDishVO implements Serializable {

    private Long dishId;        // 菜品ID
    private String name;        // 菜品名称
    private BigDecimal price;   // 单价
    private Integer copies;     // 份数
    private String image;       // 菜品图片（从 dish 表联查）
}