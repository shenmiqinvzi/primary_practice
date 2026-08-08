package com.sky.mapper;


import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {
    List<DishVO> pageQuery(DishPageQueryDTO dto);

    void insert(Dish dish);

    Integer countByStatusAndIds(@Param("ids") List<Long> ids,@Param("status") Integer status);

    void deleteByIds(@Param("ids") List<Long>ids);

    Dish getById(Long id);

    void update(Dish dish);

   
}
