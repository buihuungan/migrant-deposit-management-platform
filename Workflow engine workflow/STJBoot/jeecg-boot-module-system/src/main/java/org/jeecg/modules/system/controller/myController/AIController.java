package org.jeecg.modules.system.controller.myController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author chenjian
 */

@RestController
@RequestMapping("/llm")
@Slf4j
@Api(tags = "AI接口")
public class AIController {
    @Value("${llm.changJiangUrl}")
    private  String ChangJiangUrl ;
    @ApiOperation("获取llm回答")
    @PostMapping("/getFromChangJiang")
    public Result<Object> getFromChangJiang(String params) throws Exception {
        Result<Object> result=new Result<>();
        result.setMessage("已获取数据");
        //解析params包裹成相应的参数
        //写好接口地址
        // 创建URL对象
        URL requestUrl = new URL(ChangJiangUrl);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        try (OutputStream outputStream = connection.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"))) {
            writer.write(params);
            writer.flush();
        }
        // 获取响应结果
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        connection.disconnect();
        result.setResult( response.toString());
        return result;
    }

}
