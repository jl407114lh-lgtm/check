package com.itheima.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源映射：把 /images/** 映射到本地上传目录，
 * 这样上传到 D:\SpecialFile\image 的图片就能通过 URL 访问。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${tlias.upload.dir:D:/SpecialFile/image}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //文件系统路径要以 file: 开头，且目录结尾必须带 /
        String location = "file:" + uploadDir.replace("\\", "/") + "/";
        registry.addResourceHandler("/images/**").addResourceLocations(location);
    }
}
