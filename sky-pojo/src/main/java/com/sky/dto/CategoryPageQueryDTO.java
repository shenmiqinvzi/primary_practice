package com.sky.dto;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryPageQueryDTO implements Serializable {
    private Integer page;
    private Integer pageSize;
    private String name;
    private Integer type;   // 分类类型：1菜品 2套餐
}