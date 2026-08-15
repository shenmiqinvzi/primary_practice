package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.SetmealDish;
import com.sky.vo.SetmealDishVO;

@Mapper
public interface SetmealDishMapper {
    @Select("select * from setmeal_dish where setmeal_id=#{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
    
    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    void deleteBySetmealId(Long setmealId);

    void insertBatch(List<SetmealDish> setmealDishs);

    /**
 * 用户端：根据套餐ID查询关联菜品（含图片），用于展示
 */
    List<SetmealDishVO> getSetmealDishVOBySetmealId(Long setmealId);
} 
