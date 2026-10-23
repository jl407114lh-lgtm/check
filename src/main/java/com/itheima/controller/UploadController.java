package com.itheima.controller;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    //允许上传的图片后缀
    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "gif", "webp", "bmp");

    //文件保存目录，可在 application.yml 的 tlias.upload.dir 中修改
    @Value("${tlias.upload.dir:D:/SpecialFile/image}")
    private String uploadDir;

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file){
        //1.校验文件是否为空
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        //2.校验文件类型(只允许图片)
        String originalFilename = file.getOriginalFilename();
        String ext = getExtension(originalFilename);
        if (!ALLOWED_EXT.contains(ext)) {
            return Result.error("只允许上传 jpg/jpeg/png/gif/webp/bmp 格式的图片");
        }

        try {
            //3.确保保存目录存在
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            //4.用UUID生成文件名，避免同名文件互相覆盖
            String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;
            Path target = dir.resolve(filename);
            file.transferTo(target);

            //5.拼接可访问的URL(WebConfig 把 /images/** 映射到了保存目录)
            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(filename)
                    .toUriString();
            log.info("文件上传成功: {} -> {}", originalFilename, target);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败: {}", originalFilename, e);
            return Result.error("文件上传失败");
        }
    }

    /**
     * 取文件后缀名，统一转小写
     */
    private String getExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dot = filename.lastIndexOf('.');
        return dot < 0 ? "" : filename.substring(dot + 1).toLowerCase();
    }

}
