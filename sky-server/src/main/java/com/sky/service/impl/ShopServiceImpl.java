package com.sky.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.sky.constant.ShopConstant;
import com.sky.service.ShopService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShopServiceImpl implements ShopService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void setStatus(Integer status){
        stringRedisTemplate.opsForValue().set(ShopConstant.SHOP_STATUS,String.valueOf(status));
        log.info("店铺营业状态已更新：{}", status == 1 ? "营业中" : "打烊");
    }

    @Override
    public Integer getStatus(){
        String value=stringRedisTemplate.opsForValue().get(ShopConstant.SHOP_STATUS);
        if(value==null){
            log.info("Redis 中无店铺状态，默认返回 0（打烊）");
            return 0;
        }
        return Integer.parseInt(value);
    }
}
