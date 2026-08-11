package com.sky.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sky.service.SetmealService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO){
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.ENABLE);
        setmeal.setCreateTime(LocalDateTime.now());
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setCreateUser(BaseContext.getCurrentId());
        setmeal.setUpdateUser(BaseContext.getCurrentId());

        setmealMapper.insert(setmeal);

        List<SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();
        if(setmealDishes!=null&&!setmealDishes.isEmpty()){
            for(SetmealDish dish:setmealDishes){
                dish.setSetmealId(setmeal.getId());
            }
        }
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO dto){
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        Page<Setmeal>page=(Page<Setmeal>)setmealMapper.pageQuery(dto);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids){
        Integer count=setmealMapper.countByStatusAndIds(ids,StatusConstant.ENABLE);
        if(count!=null&& count>0){
            throw new BaseException("起售中的套餐不能删除，请先停售");
        }
        setmealMapper.deleteByIds(ids);
        for(Long id:ids){
            setmealDishMapper.deleteBySetmealId(id);
        }
        log.info("批量删除套餐成功，ids：{}", ids);
    }
        

}
