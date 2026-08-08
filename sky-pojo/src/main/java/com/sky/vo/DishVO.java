package com.sky.vo;

import java.util.List;

import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishVO extends Dish {
    private String categoryName;
    private List<DishFlavor> flavors;
}