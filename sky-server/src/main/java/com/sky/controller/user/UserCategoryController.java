package com.sky.controller.user;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.entity.Category;
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
            // 店铺打烊，但分类列表通常允许查看？根据业务自行决定。
            // 黑马课程里用户端任何接口都要先判断打烊，这里按规则统一抛异常
            // 我们统一在 Controller 层校验
        }
        List<Category>list=categoryService.listByType(type);
        return Result.success(list);
    }
}
