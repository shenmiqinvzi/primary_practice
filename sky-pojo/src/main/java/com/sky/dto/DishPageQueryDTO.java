package com.sky.dto;


import lombok.Data;

@Data
public class DishPageQueryDTO {
    private Integer page;
    private Integer pageSize;
    private String name;
    private Long categoryId;
}
