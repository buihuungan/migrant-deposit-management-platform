package com.wust.businessback.controller;

import com.alibaba.fastjson.JSONObject;
import com.wust.businessback.constant.Result;
import com.wust.businessback.entity.MainPayment;
import com.wust.businessback.mapper.MainPaymentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author:ZhangD
 * @Description:
 * @Date: Created in 20:02 2022/1/11
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class test {

    @Autowired
    private MainPaymentMapper mainPaymentMapper;

    @PostMapping("/testComplete")
    public Result completeTask(@RequestBody JSONObject jsonObject) {
        Result r = new Result();
        r.setResult(mainPaymentMapper.selectList(null));
        return r;
    }

    @PostMapping("/testDatabase")
    public Result testDatabase(@RequestBody JSONObject jsonObject) {
        return null;
    }
}
