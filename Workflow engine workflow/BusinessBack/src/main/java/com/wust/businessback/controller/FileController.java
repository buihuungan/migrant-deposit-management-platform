package com.wust.businessback.controller;

import com.alibaba.fastjson.JSONObject;
import com.wust.businessback.constant.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

@Api(tags = "file", value = "文件上传下载")
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {


    @Value("${upload.dirName}")
    private String fileParentPath;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result fileUpLoad(@RequestParam("file")MultipartFile multipartFile){
        Result result = new Result();
        JSONObject jsonObject = new JSONObject();
        //原文件名称
        String originalFilename = multipartFile.getOriginalFilename();
        log.info("文件名称：{}",originalFilename);
        //获取文件类型
        String type =  multipartFile.getContentType();
        log.info("文件类型：{}",type);
        //构建文件目录
        File uploadParentFile = new File(fileParentPath+"/"+type+"/");
        if (!uploadParentFile.exists()){
            uploadParentFile.mkdirs();
        }

        //定义一个文件的唯一标识
        String uuid = UUID.randomUUID().toString();
        //定义文件位置
        String uploadFilePath = fileParentPath+type+"/"+uuid+"-"+originalFilename;

        //转储文件
        try {
            OutputStream outputStream = new FileOutputStream(uploadFilePath);
            InputStream inputStream = multipartFile.getInputStream();

            byte[] buffer = new byte[1024];
            int  len = -1;
            while ((len = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,len);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            jsonObject.put("uplodadFile",uploadFilePath);
            result.setCode(200);
            result.setResult(jsonObject);
            result.setMessage("上传成功");
            result.setSuccess(true);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            result.setCode(500);
            result.setMessage(e.toString());
            result.setSuccess(false);
        }
        return result;
    }

    @ApiOperation("文件下载")
    @PostMapping("/download")
    public Result fileDownLoad(@RequestParam("filePath") String filePath,HttpServletResponse response){
        File downLoadFile = new File(filePath);
        Result result = new Result();
        if (!downLoadFile.exists()){
            result.setSuccess(false);
            result.setMessage("文件不存在");
        }else{
            try {
                InputStream inputStream = new FileInputStream(downLoadFile);
                //设置响应头，对文件进行url编码
                String fileName = URLEncoder.encode(downLoadFile.getName(),"UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename="+fileName);
                response.setContentLength((int)downLoadFile.length());

                //第三步：读文件写入http响应
                OutputStream outputStream = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int  len = -1;
                while ((len = inputStream.read(buffer)) != -1){
                    outputStream.write(buffer,0,len);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                result.setCode(200);
                result.setSuccess(true);
                result.setMessage("文件下载成功");

            }catch (Exception e){
                log.error(e.getMessage(), e);
                result.setCode(500);
                result.setSuccess(false);
                result.setMessage(e.toString());
            }
        }

        return result;
    }




}
