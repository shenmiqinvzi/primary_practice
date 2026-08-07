package com.sky.dto;

import com.sky.entity.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DishDTO implements Serializable {

    private Long id;

    // 菜品名称
    private String name;

    // 分类id
    private Long categoryId;

    // 价格
    private BigDecimal price;

    // 图片URL
    private String image;

    // 描述信息
    private String description;

    // 状态：1起售 0停售
    private Integer status;

    // 口味列表
    private List<DishFlavor> flavors;
}
