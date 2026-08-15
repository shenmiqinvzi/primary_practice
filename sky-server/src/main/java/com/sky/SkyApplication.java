package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * 苍穹外卖 启动类
 */
@SpringBootApplication
@EnableTransactionManagement   // 开启声明式事务（做订单时要用）
@Slf4j
public class SkyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkyApplication.class, args);
        log.info("苍穹外卖项目启动成功...");
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
