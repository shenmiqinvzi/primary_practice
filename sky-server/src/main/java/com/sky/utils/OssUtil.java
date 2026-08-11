package com.sky.utils;

import com.aliyun.oss.OSS;
import com.sky.exception.BaseException;
import com.sky.properties.OssProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * OSS 上传工具
 * 通过构造器注入（@AllArgsConstructor）：OSS 客户端和配置都从 Spring 容器拿，全项目复用同一个连接
 */
@Component
@Slf4j
@AllArgsConstructor
public class OssUtil {

    /** OSS 客户端（由 OSSConfiguration 注册成 bean，全局只建一次） */
    private OSS ossClient;

    /** 阿里云 OSS 配置（读取 yml 中 sky.alioss） */
    private OssProperties ossProperties;

    /**
     * 上传文件到 OSS
     * @param fileName    原始文件名（用于生成存储路径）
     * @param inputStream 文件输入流
     * @return 文件访问 URL
     */
    public String upload(String fileName, InputStream inputStream) {
        try {
            String bucketName = ossProperties.getBucketName();

            // 1. 生成存储路径：按日期分目录 + 时间戳防重名
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String objectKey = datePath + "/" + System.currentTimeMillis() + "_" + fileName;

            // 2. 上传（复用容器里的连接，不再每次 new + shutdown）
            ossClient.putObject(bucketName, objectKey, inputStream);

            // 3. 拼接访问 URL
            String url = "https://" + bucketName + "." + ossProperties.getEndpoint() + "/" + objectKey;
            log.info("文件上传成功：{}", url);
            return url;
        } catch (Exception e) {
            log.error("OSS 上传失败", e);
            throw new BaseException("文件上传失败");
        }
    }
}
