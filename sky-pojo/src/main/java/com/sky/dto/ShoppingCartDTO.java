package com.sky.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ShoppingCartDTO implements Serializable{
    private Long dishId;
    private Long setmealId;
    private String dishFlavor;
    private Integer number;  // 加减数量时用（默认 1）

}
