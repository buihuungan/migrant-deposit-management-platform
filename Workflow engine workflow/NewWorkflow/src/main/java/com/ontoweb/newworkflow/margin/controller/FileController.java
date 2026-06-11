package com.ontoweb.newworkflow.margin.controller;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
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
        if(originalFilename.indexOf(".")!=-1){
            originalFilename = originalFilename.substring(0, originalFilename.lastIndexOf(".")) + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.indexOf("."));
        }else{
            originalFilename = originalFilename+ "_" + System.currentTimeMillis();
        }
        log.info("文件名称：{}",originalFilename);
        //获取文件类型
        String type =  multipartFile.getContentType();
        log.info("文件类型：{}",type);
        //构建文件目录
        File uploadParentFile = new File(fileParentPath+"/"+type+"/");
        if (!uploadParentFile.exists()){
            uploadParentFile.mkdirs();
        }

        //定义文件位置
        String uploadFilePath = fileParentPath + type + "/" + originalFilename;

        OutputStream outputStream = null;
        InputStream inputStream = null;

        //转储文件
        try {
            outputStream = new FileOutputStream(uploadFilePath);
            inputStream = multipartFile.getInputStream();

            byte[] buffer = new byte[1024];
            int  len = -1;
            while ((len = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,len);
            }

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
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }

    @ApiOperation("文件下载")
    @PostMapping("/download")
    public void fileDownLoad(@RequestParam("filePath") String filePath,HttpServletResponse response){
        File downLoadFile = new File(filePath);
        if (!downLoadFile.exists()){
            response.setStatus(404);
            throw new RuntimeException("文件不存在..");
        }else{
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = new FileInputStream(downLoadFile);
                //设置响应头，对文件进行url编码
                String fileName = URLEncoder.encode(downLoadFile.getName(),"UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename="+fileName);
                response.setContentLength((int)downLoadFile.length());

                //第三步：读文件写入http响应
                outputStream = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int  len = -1;
                while ((len = inputStream.read(buffer)) != -1){
                    outputStream.write(buffer,0,len);
                }
                //清理缓存
                response.flushBuffer();
            }catch (Exception e){
                log.error(e.getMessage(), e);
                response.setStatus(404);
                e.printStackTrace();
            }finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    @ApiOperation("文件预览")
    @GetMapping("/static/**")
    public void view(HttpServletRequest request, HttpServletResponse response) {
        //获取文件路劲
        String filePath = request.getRequestURI().substring("/file/static".length());
        // 其余处理略
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            // 解码 URL 中的中文字符（假设使用 UTF-8 编码）
            filePath = URLDecoder.decode(filePath, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            log.error("解码失败" + e.getMessage());
        }

        try {
            File file = new File(filePath);
            if(!file.exists()){
                response.setStatus(404);
                throw new RuntimeException("文件不存在..");
            }
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"),"iso-8859-1"));
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("预览文件失败" + e.getMessage());
            response.setStatus(404);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

}
