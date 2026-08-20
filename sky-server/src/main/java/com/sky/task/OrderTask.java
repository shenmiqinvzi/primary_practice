package com.sky.task;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sky.mapper.OrderMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron="0 * * * * ?")
    @Transactional
    public void cancelTimeoutOrders(){
        log.info("定时任务开始：清理超时未支付订单");
        LocalDateTime threshold=LocalDateTime.now().minusMinutes(15);
        LocalDateTime cancelTime=LocalDateTime.now();

        int count=orderMapper.cancelTimeoutOrders(threshold,cancelTime);
        if (count > 0) {
            log.info("清理超时未支付订单完成，共取消 {} 单，阈值时间：{}", count, threshold);
        } else {
            log.info("无超时未支付订单需要清理");
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")  // 每天凌晨1点
    @Transactional
    public void completeDeliveryOrders() {
        log.info("定时任务开始：自动完成派送中订单");
        LocalDateTime deliveryTime = LocalDateTime.now();
        
        int count = orderMapper.completeDeliveryOrders(deliveryTime);
        
        if (count > 0) {
            log.info("自动完成派送中订单完成，共完成 {} 单", count);
        } else {
            log.info("无派送中订单需要自动完成");
        }
    }
}
