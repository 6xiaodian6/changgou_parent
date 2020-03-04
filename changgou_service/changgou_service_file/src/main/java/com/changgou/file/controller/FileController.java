package com.changgou.file.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.file.pojo.FastDFSClient;
import com.changgou.file.util.FastDFSFile;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-09 08:51:49
 **/
@RestController
@RequestMapping("/file")
public class FileController {
    @RequestMapping("/upload")
    public Result fileUpload(@RequestParam("file") MultipartFile file) {
        try {
            //获取文件名称
            if (file == null) {
                throw new RuntimeException("文件不存在");
            }
            String filename = file.getOriginalFilename();
            if (StringUtils.isEmpty(filename)) {
                throw new RuntimeException("文件不存在");
            }
            //获取文件后缀.
            String extFile = filename.substring(filename.lastIndexOf(".") + 1);
            //获取文件字节流
            byte[] content = file.getBytes();
            //文件上传工具类使用，上传文件
            FastDFSFile fastDFSFile = new FastDFSFile(filename, content, extFile);
            //开始上传文件
            String[] upload = FastDFSClient.upload(fastDFSFile);
            //封装返回结果
            String url = FastDFSClient.getTrackerUrl() + upload[0] + File.separator + upload[1];
            return new Result(true, StatusCode.OK, "上传成功", url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, StatusCode.ERROR, "上传失败");
    }
}
