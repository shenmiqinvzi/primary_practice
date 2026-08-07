package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.RequestBody;   // ✅
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags="菜品管理接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @GetMapping("/page")
    public Result<PageResult>pageQuery(DishPageQueryDTO dto){
        log.info("菜品分页查询:{}",dto);
        PageResult pageResult=dishService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam("ids") List<Long>ids){
        dishService.deleteBatch(ids);
        log.info("批量删除菜品，ids: {}", ids);
        return Result.success();
    }
}
