package com.sky.service;

import java.util.List;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

public interface DishService {
    PageResult pageQuery(DishPageQueryDTO dto);

    void saveWithFlavor(DishDTO dishDTO);
    
    void deleteBatch(List<Long> ids);
}
