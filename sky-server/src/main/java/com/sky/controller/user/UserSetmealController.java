package com.sky.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.service.ShopService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user/setmeal")
@Api(tags="用户端套餐接口")
@Slf4j
public class UserSetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private ShopService shopService;

    @GetMapping("/list")
    @ApiOperation("按分类查询套餐")
    public Result<List<Setmeal>> list(@RequestParam Long categoryId){
        if (shopService.getStatus() == StatusConstant.DISABLE) {
            throw new BaseException("店铺打烊中");
        }
        List<Setmeal> list=setmealService.getSetmealListByCategoryId(categoryId);
        return Result.success(list);
    }
}
