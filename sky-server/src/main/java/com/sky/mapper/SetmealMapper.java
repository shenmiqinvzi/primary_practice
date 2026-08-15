package com.sky.mapper;

import java.util.List;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sky.dto.SetmealPageQueryDTO;

@Mapper
public interface SetmealMapper {
    List<SetmealVO> pageQuery(SetmealPageQueryDTO dto);

    Setmeal getById(Long id);


    void insert(Setmeal setmeal);
    void update(Setmeal setmeal);

    Integer countByStatusAndIds(@Param("ids") List<Long> ids,@Param("status") Integer status);

    void deleteByIds(@Param("ids") List<Long> ids);

    /**
 * 用户端：根据分类ID查询起售中的套餐（按 sort 排序）
 */
    @Select("select * from setmeal where category_id = #{categoryId} and status = 1")
    List<Setmeal> getByCategoryIdAndStatus(Long categoryId);
    
} 

