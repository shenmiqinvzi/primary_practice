package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BaseException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO dto) {
        
        Long userId = BaseContext.getCurrentId();
        ShoppingCart cart = null;
        // 在构建 cart 对象时，处理 dishFlavor
        String flavor = dto.getDishFlavor();
        if (flavor == null) {
            flavor = "";  // null → 空字符串
        }
        

        // 1. 判断是菜品还是套餐
        if (dto.getDishId() != null) {
            // ====== 菜品 ======
            // 1.1 先查同款（同一菜品 + 同一口味）
            cart = shoppingCartMapper.getByDishAndFlavor(userId, dto.getDishId(), dto.getDishFlavor());

            if (cart != null) {
                // 有同款 → 数量 + 1
                shoppingCartMapper.updateNumberById(cart.getId(), cart.getNumber() + 1);
                log.info("购物车菜品数量+1：userId={}, dishId={}, flavor={}", userId, dto.getDishId(), dto.getDishFlavor());
                return;
            }

            // 1.2 没有同款 → 查菜品表，补全信息
            Dish dish = dishMapper.getById(dto.getDishId());
            if (dish == null) {
                throw new BaseException("菜品不存在");
            }
            if (dish.getStatus() == StatusConstant.DISABLE) {
                throw new BaseException("菜品已停售，不能加购");
            }

            cart = new ShoppingCart();
            cart.setUserId(userId);
            cart.setDishId(dto.getDishId());
            cart.setDishFlavor(dto.getDishFlavor());
            cart.setName(dish.getName());
            cart.setImage(dish.getImage());
            cart.setAmount(dish.getPrice());
            cart.setNumber(1);
            cart.setCreateTime(LocalDateTime.now());

        } else if (dto.getSetmealId() != null) {
            // ====== 套餐 ======
            // 2.1 先查同款（套餐没有口味）
            cart = shoppingCartMapper.getBySetmeal(userId, dto.getSetmealId());

            if (cart != null) {
                shoppingCartMapper.updateNumberById(cart.getId(), cart.getNumber() + 1);
                log.info("购物车套餐数量+1：userId={}, setmealId={}", userId, dto.getSetmealId());
                return;
            }

            // 2.2 没有同款 → 查套餐表，补全信息
            Setmeal setmeal = setmealMapper.getById(dto.getSetmealId());
            if (setmeal == null) {
                throw new BaseException("套餐不存在");
            }
            if (setmeal.getStatus() == StatusConstant.DISABLE) {
                throw new BaseException("套餐已停售，不能加购");
            }

            cart = new ShoppingCart();
            cart.setUserId(userId);
            cart.setSetmealId(dto.getSetmealId());
            cart.setName(setmeal.getName());
            cart.setImage(setmeal.getImage());
            cart.setAmount(setmeal.getPrice());
            cart.setNumber(1);
            cart.setCreateTime(LocalDateTime.now());

        } else {
            throw new BaseException("请选择要添加的菜品或套餐");
        }

        // 3. 插入新记录
        shoppingCartMapper.insert(cart);
        log.info("购物车新增记录：userId={}, name={}", userId, cart.getName());
    }

    @Override
    public void sub(ShoppingCartDTO dto) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart cart = null;

        // 1. 查同款
        if (dto.getDishId() != null) {
            cart = shoppingCartMapper.getByDishAndFlavor(userId, dto.getDishId(), dto.getDishFlavor());
        } else if (dto.getSetmealId() != null) {
            cart = shoppingCartMapper.getBySetmeal(userId, dto.getSetmealId());
        } else {
            throw new BaseException("请选择要减少的菜品或套餐");
        }

        if (cart == null) {
            throw new BaseException("购物车中不存在该商品");
        }

        // 2. 数量 - 1
        int newNumber = cart.getNumber() - 1;
        if (newNumber > 0) {
            shoppingCartMapper.updateNumberById(cart.getId(), newNumber);
            log.info("购物车数量-1：userId={}, id={}, newNumber={}", userId, cart.getId(), newNumber);
        } else {
            // 减到 0 → 删除该记录
            shoppingCartMapper.deleteById(cart.getId());
            log.info("购物车数量减到0，删除记录：userId={}, id={}", userId, cart.getId());
        }
    }

    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        return shoppingCartMapper.listByUserId(userId);
    }

    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
        log.info("清空购物车：userId={}", userId);
    }
}