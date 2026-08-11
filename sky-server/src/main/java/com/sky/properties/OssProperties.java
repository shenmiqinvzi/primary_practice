package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取 application.yml 中 sky.alioss 开头的配置
 */
@Component
@ConfigurationProperties(prefix = "sky.alioss")
@Data
public class OssProperties {

    /** 阿里云 OSS 地域节点 */
    private String endpoint;

    /** 访问密钥 ID */
    private String accessKeyId;

    /** 访问密钥 Secret */
    private String accessKeySecret;

    /** 存储空间名称（Bucket） */
    private String bucketName;
}
