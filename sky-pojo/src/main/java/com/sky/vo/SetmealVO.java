package com.sky.vo;

import com.sky.entity.SetmealDish;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SetmealVO implements Serializable {

    private Long id;
    private Long categoryId;
    private String categoryName;          // 分类名称（联表查询得到）
    private String name;
    private BigDecimal price;
    private String image;
    private String description;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
    private List<SetmealDish> setmealDishes; // 关联的菜品列表
}