package com.sky.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.constant.StatusConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.service.ShopService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealDishVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user/dish")
@Api(tags="用户端菜品接口")
@Slf4j
public class UserDishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private ShopService shopService;

    @GetMapping("/list")
    @ApiOperation("按分类查询菜品（用户端）")
    public Result<List<DishVO>> list(@RequestParam Long categoryId){
        if (shopService.getStatus() == StatusConstant.DISABLE) {
        throw new BaseException("店铺打烊中");
        }
        List<DishVO>list=dishService.getDishListWithFlavorByCategoryId(categoryId);
        return Result.success(list);
    }

    @GetMapping("/setmeal/{id}")
    @ApiOperation("查询套餐下的菜品列表（用户端）")
    public Result<List<SetmealDishVO>> getSetmealDishes(@PathVariable Long id){
        if (shopService.getStatus() == StatusConstant.DISABLE) {
        throw new BaseException("店铺打烊中");
        }
        List<SetmealDishVO>list=setmealService.getSetmealDishVOBySetmealId(id);
        return Result.success(list);
    }
}
