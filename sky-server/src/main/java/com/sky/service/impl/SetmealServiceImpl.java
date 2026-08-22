package com.sky.service.impl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sky.service.SetmealService;
import com.sky.vo.SetmealDishVO;
import com.sky.vo.SetmealVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

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
            setmealDishMapper.insertBatch(setmealDishes);
        }
        
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO dto){
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        Page<SetmealVO>page=(Page<SetmealVO>)setmealMapper.pageQuery(dto);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids){
        if (ids == null || ids.isEmpty()) {
          log.warn("删除套餐：ids 为空，不执行任何操作");
          return;
        }
        List<Long> categoryIds = new java.util.ArrayList<>();
        for (Long id : ids) {
            Setmeal old = setmealMapper.getById(id);
            if (old != null && old.getCategoryId() != null) categoryIds.add(old.getCategoryId());
        }
        Integer count=setmealMapper.countByStatusAndIds(ids,StatusConstant.ENABLE);
        if(count!=null&& count>0){
            throw new BaseException("起售中的套餐不能删除，请先停售");
        }
        setmealMapper.deleteByIds(ids);
        for(Long id:ids){
            setmealDishMapper.deleteBySetmealId(id);
        }
        categoryIds.forEach(id -> stringRedisTemplate.delete("setmeal_list_" + id));
        log.info("批量删除套餐成功，ids：{}", ids);
    }

    @Override
    public SetmealVO getByIdWithDish(Long id){
        Setmeal setmeal=setmealMapper.getById(id);
        List<SetmealDish>setmealDishs=setmealDishMapper.getBySetmealId(id);
        SetmealVO setmealVO=new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishs);
        return setmealVO;
    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDTO setmealDTO){
        Setmeal old = setmealMapper.getById(setmealDTO.getId());
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setUpdateUser(BaseContext.getCurrentId());
        setmealMapper.update(setmeal);
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
        List<SetmealDish> setmealDishs=setmealDTO.getSetmealDishes();
        if(setmealDishs!=null&&!setmealDishs.isEmpty()){
            for(SetmealDish dish:setmealDishs){
                dish.setSetmealId(setmealDTO.getId());
            }
            setmealDishMapper.insertBatch(setmealDishs);
        }
        if (old != null && old.getCategoryId() != null) stringRedisTemplate.delete("setmeal_list_" + old.getCategoryId());
        if (setmealDTO.getCategoryId() != null) stringRedisTemplate.delete("setmeal_list_" + setmealDTO.getCategoryId());
        log.info("修改套餐成功，id：{}", setmealDTO.getId());
    }

    @Override
    public void startOrStop(Integer status,Long id){
        if (status != StatusConstant.ENABLE && status != StatusConstant.DISABLE) throw new BaseException("非法套餐状态");
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        setmealMapper.update(setmeal);
        Setmeal current = setmealMapper.getById(id);
        if (current != null && current.getCategoryId() != null) stringRedisTemplate.delete("setmeal_list_" + current.getCategoryId());
        log.info("修改套餐状态：id={}, status={}", id, status);
    }
    
    @Override
    public List<Setmeal> getSetmealListByCategoryId(Long categoryId){
        String cacheKey="setmeal_list_"+categoryId;

        String cachedJson=stringRedisTemplate.opsForValue().get(cacheKey);
        if(cachedJson!=null&&!cachedJson.isEmpty()){
            try{
                List<Setmeal> list=objectMapper.readValue(cachedJson, new TypeReference<List<Setmeal>>() {});
                log.info("命中缓存：{}", cacheKey);
                return list;
            }catch (Exception e) {
            log.warn("Redis 缓存解析失败", e);
            }
        }

        List<Setmeal> list = setmealMapper.getByCategoryIdAndStatus(categoryId);
        try{
            String json=objectMapper.writeValueAsString(list);
            stringRedisTemplate.opsForValue().set(cacheKey, json);
            log.info("缓存写入：{}", cacheKey);
        }catch (Exception e) {
            log.warn("Redis 缓存写入失败", e);
        }

        return list;
    }

    @Override
    public List<SetmealDishVO> getSetmealDishVOBySetmealId(Long setmealId){
        return setmealDishMapper.getSetmealDishVOBySetmealId(setmealId);
    }
}
