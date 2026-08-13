package com.sky.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.result.Result;
import com.sky.service.ShopService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user/shop")
@Api(tags="用户端店铺接口")
@Slf4j
public class UserShopController {
    @Autowired
    private ShopService shopService;

    @GetMapping("/status")
    @ApiOperation("查询店铺营业状态")
    public Result<Integer> getStatus(){
        Integer status=shopService.getStatus();
        log.info("用户端查询店铺状态：{}", status == 1 ? "营业中" : "打烊");
        return Result.success(status);
    }

}
