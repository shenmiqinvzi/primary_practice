package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.DishFlavor;

@Mapper
public interface DishFlavorMapper {
    void insertBatch(List<DishFlavor> flavors);

    @Select("select * from dish_flavor where dish_id=#{id}")
    List<DishFlavor>getByDishId(Long id);

    @Delete("delete from dish_flavor where dish_id=#{id}")
    void deleteByDishId(Long id);
}
