package com.sky.service;

import java.util.List;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

public interface SetmealService {
    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO dto);

    void deleteBatch(List<Long> ids);

    SetmealVO getByIdWithDish(SetmealDTO setmealDTO);

    void startOrStop(Integer status,Long id);
}