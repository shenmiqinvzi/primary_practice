package com.sky.controller.user;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.entity.Category;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.ShopService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user/category")
@Api(tags="用户端分类接口")
@Slf4j
public class UserCategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ShopService shopService;

    @GetMapping("/list")
    @ApiOperation("查询分类列表（用户端）")
    public Result<List<Category>> list(@RequestParam(required=false) Integer type){
        Integer status=shopService.getStatus();
        if (status == 0) {
            throw new BaseException("店铺已打烊");
        }
        List<Category>list=categoryService.listByType(type);
        return Result.success(list);
    }
}
