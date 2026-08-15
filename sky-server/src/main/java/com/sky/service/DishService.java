package com.sky.service;

import java.util.List;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishService {
    PageResult pageQuery(DishPageQueryDTO dto);

    void saveWithFlavor(DishDTO dishDTO);
    
    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDTO dto);

    void startOrStop(Integer status,Long id);

    List<DishVO> getDishListWithFlavorByCategoryId(Long categoryId);
}
