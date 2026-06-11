package com.ontoweb.newworkflow.margin.controller;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.handler.mapper.GeneralMapper;
import com.ontoweb.newworkflow.handler.utils.RedisUtils;
import com.ontoweb.newworkflow.margin.mapper.MainPaymentMapper;
import com.ontoweb.newworkflow.handler.service.feign.OnlineService;
import com.ontoweb.newworkflow.handler.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private GeneralMapper generalMapper;
    @Autowired
    private OnlineService onlineService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MainPaymentMapper mainPaymentMapper;


    @PostMapping("/testComplete")
    public Result completeTask(@RequestBody JSONObject jsonObject) {
        String onlineTableId = jsonObject.getString("onlineTableId");
        String onlineDataId = jsonObject.getString("onlineDataId");
        Result<?> res = onlineService.getForm(onlineTableId, onlineDataId);
        Map result = (Map) res.getResult();

        Result r = new Result();
        r.setResult(result);
        return r;
    }

    @PostMapping("/testDatabase")
    public Result testDatabase(@RequestBody JSONObject jsonObject) {
//        List<JSONObject> tableList = generalMapper.selectTableData("margin_payment");
        //全部流程
//        List<UfloProcess> processList = processMapper.selectList(null);
//        List<ProcessMapping> processMappingList = processMappingMapper.selectList(null);

//        tableList.forEach(
//                System.out::println
//        );
//        System.out.println("tableList: "+tableList.toString());
//        System.out.println("processList: "+processList.toString());
//        System.out.println("processMappingList: "+processMappingList.toString());


//        List<MainPayment> list = mainPaymentMapper.selectList(null);
//        list.forEach(
//                System.out::println
//        );
//        redisUtils.set("test","jhfsddfhjklsfhjlksddklshflkdfklshffhkl");

        return null;
    }
}
