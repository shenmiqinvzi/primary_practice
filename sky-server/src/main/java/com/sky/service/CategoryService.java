package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult pageQuery(CategoryPageQueryDTO dto);

    void save(CategoryDTO categoryDTO);

    void update(CategoryDTO categoryDTO);

    void startOrStop(Long id,Integer status);

    void deleteById(Long id);

    List<Category> listByType(Integer type);
}
