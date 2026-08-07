package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品口味实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键ID
    private Long id;

    // 菜品ID（对应 dish 表的 id）
    private Long dishId;

    // 口味名称
    private String name;

    // 口味数据（JSON格式字符串）
    private String value;
}
