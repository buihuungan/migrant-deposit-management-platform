package com.ontoweb.newworkflow.handler.controller;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.VO.HistoryProcessInstanceVO;
import com.ontoweb.newworkflow.handler.VO.ProcessInstanceVO;
import com.ontoweb.newworkflow.handler.VO.ProcessListVO;
import com.ontoweb.newworkflow.handler.service.ProcessOperateService;
import com.ontoweb.newworkflow.handler.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/9/27 下午4:03
 * @description：流程操作相关的类
 * @modified By：
 * @version: $
 */
@Api(tags = "process operation", value = "流程操作相关")
@RestController
@RequestMapping("/process")
public class ProcessController {
    @Autowired
    private ProcessOperateService processService;

    @GetMapping("/processList/{categoryId}")
    @ApiOperation("获取流程列表")
    public Result getProcessList(@RequestParam(required = false) String categoryId,@RequestParam String category) {
        Result result = new Result();
        List<ProcessListVO> list =  processService.getProcessList(categoryId,category);
        result.setResult(list);

        return result;
    }

    /**
     * 开启流程
     * @param processId
     * @return
     */
    @ApiOperation("开启流程")
    @GetMapping("/startProcess/{processId}")
    public Result startProcess(@RequestParam String processId) {
        Result result = new Result();
        JSONObject resultJson = processService.start(Long.valueOf(processId));
        result.setResult(resultJson);
        return result;
    }

    @PostMapping("getCancelProcesses")
    @ApiOperation("获取已拒绝流程")
    public Result getCancelProcess(@RequestBody(required = false) JSONObject jsonObject) {
        Result result = new Result();
        List<HistoryProcessInstanceVO> list = processService.cancelProcess(jsonObject);
        result.setResult(list);

        return result;
    }

    @PostMapping("/getInProgressProcessInstance")
    @ApiOperation("获取正在进行中的流程实例")
    public Result getInProgressProcessInstance(@RequestBody(required = false) JSONObject jsonObject) {
        Result result = new Result();
        List<ProcessInstanceVO> list = processService.inProgressProcessInstance(jsonObject);
        result.setResult(list);

        return result;
    }

    @PostMapping("/getCompleteProcessInstance")
    @ApiOperation("获取已完成的流程实例")
    public Result getCompleteProcessInstance(@RequestBody(required = false) JSONObject jsonObject) {
        Result result = new Result();
        List<HistoryProcessInstanceVO> list = processService.completeProcessInstance(jsonObject);
        result.setResult(list);

        return result;
    }

    @PostMapping("/deleteProcessInstanceFirst")
    @ApiOperation("删除没有进行第一步的流程实例")
    public Result deleteProcessInstanceFirst(@RequestBody(required = false) JSONObject jsonObject) throws ParseException {
        Result result = new Result();
        result.setSuccess(processService.deleteProcessInstanceFirst(jsonObject));
        return result;
    }
}
