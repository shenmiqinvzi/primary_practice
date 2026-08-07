package com.sky.vo;

import com.sky.entity.Dish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishVO extends Dish {
    private String categoryName;
}