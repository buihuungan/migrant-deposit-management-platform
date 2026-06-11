package com.wust.businessback.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;;
import com.wust.businessback.constant.Result;
import com.wust.businessback.service.ShortLetterService;
import com.wust.businessback.utils.AESExample;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ShortLetterServicelmpl implements ShortLetterService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Result sendSingleSMS(JSONObject jsonObject){

        Result result = new Result();

        //设置请求头
        String secretKey = "935843A67516F24D";
        String url = "http://www.btom.cn:8080/inter/sendSingleSMS";
        HttpHeaders headers = new HttpHeaders();
        headers.add("appId","EUCP-EMY-SMS0-JHUQS");
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("mobile", jsonObject.get("mobile").toString());
        jsonObject1.put("content",jsonObject.get("content").toString());
        jsonObject1.put("requestTime", Calendar.getInstance().getTimeInMillis());
        jsonObject1.put("requestValidPeriod",30);
        jsonObject1.put("customSmsId",jsonObject.get("mobile").toString());
        byte[] data = AESExample.encrypt(jsonObject1.toString(),secretKey);
        HttpEntity<byte[]> httpEntity = new HttpEntity<>(data,headers);
        //发送请求
        ResponseEntity<byte []> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, byte[].class);

        // 1. 检查响应状态码是否为 200 成功
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            result.setMessage("请求失败");
            result.setSuccess(false);
            System.out.println("请求失败，状态码：" + responseEntity.getStatusCode());
            return result;
        }

        // 2. 从 HTTP 头中获取状态字段
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        String code = responseHeaders.getFirst("result");
        if (!"SUCCESS".equalsIgnoreCase(code)) {
            result.setMessage("请求失败");
            result.setSuccess(false);
            System.out.println("请求失败，状态码：" + code);
            return result;
        }

        // 3. 获取响应体并进行解密
        byte[] encryptedData = responseEntity.getBody();
        String response = AESExample.decrypt(encryptedData,secretKey);

        if (StringUtils.isEmpty(response)){
            result.setMessage("请求成功,解析响应体失败");
            result.setSuccess(true);
            return result;
        }

        result.setMessage("请求成功");
        result.setSuccess(true);
        result.setResult(JSONObject.parseObject(response));
        return result;
    }

    @Override
    public Result sendBatchSMS(JSONObject jsonObject) {

        Result result = new Result();

        //设置请求头
        String secretKey = "935843A67516F24D";
        String url = "http://www.btom.cn:8080/inter/sendBatchSMS";
        HttpHeaders headers = new HttpHeaders();
        headers.add("appId","EUCP-EMY-SMS0-JHUQS");
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject1 = new JSONObject();
        JSONArray JSONArray = jsonObject.getJSONArray("users");
        List<JSONObject> list = new ArrayList<JSONObject>();

        for (int i = 0; i < JSONArray.size(); i++) {
            JSONObject jsonObject2 = new JSONObject();
            String mobile = JSONArray.getJSONObject(i).getString("mobile");
            jsonObject2.put("mobile",mobile);
            jsonObject2.put("customSmsId",mobile);
            list.add(jsonObject2);
        }

        jsonObject1.put("smses",list);
        jsonObject1.put("content",jsonObject.get("content").toString());
        jsonObject1.put("requestTime", Calendar.getInstance().getTimeInMillis());
        jsonObject1.put("requestValidPeriod",30);

        byte[] data = AESExample.encrypt(jsonObject1.toString(),secretKey);
        HttpEntity<byte[]> httpEntity = new HttpEntity<>(data,headers);
        //发送请求
        ResponseEntity<byte []> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, byte[].class);

        // 1. 检查响应状态码是否为 200 成功
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            result.setMessage("请求失败");
            result.setSuccess(false);
            System.out.println("请求失败，状态码：" + responseEntity.getStatusCode());
            return result;
        }

        // 2. 从 HTTP 头中获取状态字段
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        String code = responseHeaders.getFirst("result");
        if (!"SUCCESS".equalsIgnoreCase(code)) {
            result.setMessage("请求失败");
            result.setSuccess(false);
            System.out.println("请求失败，状态码：" + code);
            return result;
        }

        // 3. 获取响应体并进行解密
        byte[] encryptedData = responseEntity.getBody();
        String response = AESExample.decrypt(encryptedData,secretKey);

        if (StringUtils.isEmpty(response)){
            result.setMessage("请求成功,解析响应体失败");
            result.setSuccess(true);
            return result;
        }

        result.setMessage("请求成功");
        result.setSuccess(true);
        result.setResult(JSONArray.parse(response));
        return result;
    }
}
