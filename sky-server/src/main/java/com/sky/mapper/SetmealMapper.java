package com.sky.mapper;

import java.util.List;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sky.dto.SetmealPageQueryDTO;

@Mapper
public interface SetmealMapper {
    List<SetmealVO> pageQuery(SetmealPageQueryDTO dto);

    Setmeal getById(Long id);


    void insert(Setmeal setmeal);
    void update(Setmeal setmeal);

    Integer countByStatusAndIds(@Param("ids") List<Long> ids,@Param("status") Integer status);

    void deleteByIds(@Param("ids") List<Long> ids);
    
} 

