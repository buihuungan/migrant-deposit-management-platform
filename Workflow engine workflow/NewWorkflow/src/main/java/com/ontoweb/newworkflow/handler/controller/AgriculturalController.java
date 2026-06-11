package com.ontoweb.newworkflow.handler.controller;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.service.feign.AgriculturalService;
import com.ontoweb.newworkflow.handler.utils.Result;
import io.swagger.annotations.Api;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenjian
 */
@RestController
@RequestMapping("/agricultural")
@Api(tags = "task operation", value = "农业")
public class AgriculturalController {
    @Resource
    private AgriculturalService agriculturalService;

    @PostMapping("/add")
    public Result<Object> add(@RequestBody JSONObject jsonObject) {
        String caseId = jsonObject.getString("caseId");
        String caseName = jsonObject.getString("caseName");
        agriculturalService.insertAgriculturalInspection(caseId, caseName);
        return Result.ok("立案流程已完成，现勘流程开启！");
    }

    @PostMapping("/addIdentify")
    public Result<Object> addIdentify(@RequestBody JSONObject jsonObject) {
        String caseId = jsonObject.getString("caseId");
        String caseName = jsonObject.getString("caseName");
        agriculturalService.insertAgriculturalIdentify(caseId, caseName);
        return Result.ok("现勘流程已完成，鉴定流程开启！");
    }

    @PostMapping("/addvalue")
    public Result<Object> addvalue(@RequestBody JSONObject jsonObject) {
        String caseId = jsonObject.getString("caseId");
        String caseName = jsonObject.getString("caseName");
        agriculturalService.insertAgriculturalValue(caseId, caseName);
        return Result.ok("鉴定流程完成，价值评估流程开启！");
    }

}
