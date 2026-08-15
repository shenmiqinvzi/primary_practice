package com.sky.service.impl;

import org.springframework.beans.BeanUtils;   // ✅

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.BaseException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public PageResult pageQuery(DishPageQueryDTO dto){
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<DishVO> page=(Page<DishVO>)dishMapper.pageQuery(dto);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO){
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setStatus(StatusConstant.ENABLE);
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        dish.setCreateUser(BaseContext.getCurrentId());
        dish.setUpdateUser(BaseContext.getCurrentId());
        dishMapper.insert(dish);

        List<DishFlavor> flavors=dishDTO.getFlavors();
        if(flavors!=null&&!flavors.isEmpty()){
            flavors.forEach(f->f.setDishId(dish.getId()));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public void deleteBatch(List<Long> ids){
        if(ids==null||ids.isEmpty()) return;
        Integer count=dishMapper.countByStatusAndIds(ids, StatusConstant.ENABLE);
        if(count!=null&&count>0){
            throw new BaseException("起售中的菜品不可以删除，请先停售");
        }

        dishMapper.deleteByIds(ids);
        log.info("批量删除菜品成功，ids: {}", ids);
        
    }

    @Override
    public DishVO getByIdWithFlavor(Long id){
        Dish dish=dishMapper.getById(id);
        List<DishFlavor> flavors=dishFlavorMapper.getByDishId(id);
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO){
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dish.setUpdateTime(LocalDateTime.now());
        dish.setUpdateUser(BaseContext.getCurrentId());

        dishMapper.update(dish);
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        List<DishFlavor>flavors=dishDTO.getFlavors();

        if(flavors!=null&&!flavors.isEmpty()){
            for(DishFlavor flavor:flavors){
                flavor.setDishId(dishDTO.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }
        log.info("修改菜品成功，ID:{}",dishDTO.getId());
    }

    @Override
    public void startOrStop(Integer status,Long id){
        if (status != StatusConstant.ENABLE && status != StatusConstant.DISABLE) {throw new BaseException("非法的菜品状态值：" + status);}

        Dish dish=Dish.builder()
                    .id(id)
                    .status(status)
                    .updateTime(LocalDateTime.now())
                    .updateUser(BaseContext.getCurrentId())
                    .build();
        dishMapper.update(dish);
        log.info("菜品状态修改成功：id={}, status={}", id, status);
    }

    @Override
    public List<DishVO> getDishListWithFlavorByCategoryId(Long categoryId){
        String cacheKey="dish_list_"+categoryId;

        String cachedJson=stringRedisTemplate.opsForValue().get(cacheKey);
        if(cachedJson!=null&&!cachedJson.isEmpty()){
            try{
                List<DishVO> dishVOList=objectMapper.readValue(cachedJson,new TypeReference<List<DishVO>>() {});
                log.info("命中缓存：{}", cacheKey);
                return dishVOList;
            }catch(Exception e){
                log.warn("Redis 缓存数据解析失败，忽略缓存，查数据库", e);
            }
        }

        List<Dish> dishList=dishMapper.getByCategoryIdAndStatus(categoryId);
        List<DishVO> result=new ArrayList<>();
        for(Dish dish:dishList){
            DishVO dishVO=new DishVO();
            BeanUtils.copyProperties(dish,dishVO);
            dishVO.setCategoryName(null);
            List<DishFlavor> flavors=dishFlavorMapper.getByDishId(dish.getId());
            dishVO.setFlavors(flavors);
            result.add(dishVO);
        }
        try{
            String json=objectMapper.writeValueAsString(result);
            stringRedisTemplate.opsForValue().set(cacheKey, json);
            log.info("缓存写入：{}", cacheKey);
        }catch (Exception e) {
        log.warn("Redis 缓存写入失败", e);
        }

        return result;
    }
}
