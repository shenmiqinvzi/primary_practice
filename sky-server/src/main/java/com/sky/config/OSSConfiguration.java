package com.sky.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sky.properties.OssProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OSS 配置类
 * 把 OSS 客户端注册成单例 bean，全项目复用这一个连接，避免每次上传都重新创建
 */
@Configuration
public class OSSConfiguration {

    @Bean
    public OSS ossClient(OssProperties ossProperties) {
        return new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret()
        );
    }
}
