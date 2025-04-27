package com.wzh.controller;

import com.wzh.until.UploadUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload")
public class CommonController {
    @Value("${server.port}")
    private String dkh;

    @RequestMapping("/uploadImage")
    @ResponseBody
    public String upload_Image(@RequestParam(value = "file") MultipartFile file) {

        String imagePath = UploadUtils.upload(file);//获得图片路径

        return imagePath;
    }




}
