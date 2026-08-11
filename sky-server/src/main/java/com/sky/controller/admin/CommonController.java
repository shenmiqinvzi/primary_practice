package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.OssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 通用接口（文件上传等）
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private OssUtil ossUtil;

    /**
     * 文件上传：返回图片访问 URL，前端再把它填进菜品的 image 字段
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(@RequestPart("file") MultipartFile file) throws IOException {
        log.info("文件上传：{}", file.getOriginalFilename());
        String url = ossUtil.upload(file.getOriginalFilename(), file.getInputStream());
        return Result.success(url);
    }
}
