package com.ontoweb.newworkflow.handler.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.VO.TaskVO;
import com.ontoweb.newworkflow.handler.service.TaskOperateService;
import com.ontoweb.newworkflow.handler.utils.CommonConstant;
import com.ontoweb.newworkflow.handler.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/9 上午11:34
 * @description：任务相关的控制器
 * @modified By：
 * @version: $
 */
@RestController
@RequestMapping("/task")
@Api(tags = "task operation", value = "任务相关")
public class TaskOperateController {
    @Autowired
    private TaskOperateService taskOperateService;

    @PostMapping("/complete")
    @ApiOperation(value = "完成任务", notes = "完成指定任务")
    @Transactional
    public Result completeTask(@RequestBody JSONObject jsonObjectParam) {
        Result result = new Result();
        JSONObject jsonObject = taskOperateService.completeTask(jsonObjectParam);
        result.setResult(jsonObject);

        return result;
    }

    @PostMapping("/withdraw")
    @ApiOperation("撤回到上一任务节点")
    public Result withdrawTask(@RequestBody JSONObject jsonObjectRequest) {
        Result result = new Result();
        JSONObject jsonObject = new JSONObject();
        Boolean bool = taskOperateService.withdraw(jsonObjectRequest);
        jsonObject.put("status", bool);
        if (bool == false)
            jsonObject.put("result", CommonConstant.WITHDRAW_TASK_FAILURE);
        result.setResult(jsonObject);

        return result;
    }

    /**
     * ulfo设计的是取消任务，但在此用作拒绝任务使用
     * @param jsonObjectRequest
     * @return
     */
    @PostMapping("/cancel")
    @ApiOperation("取消指定的任务")
    public Result cancelTask(@RequestBody JSONObject jsonObjectRequest) {
        Result result = new Result();
        JSONObject jsonObject = new JSONObject();
        Boolean bool = taskOperateService.cancelTask(jsonObjectRequest);
        result.setResult(bool);

        return result;
    }

    @PostMapping("/assign")
    @ApiOperation("指定任务处理人")
    public Result getAssignee(@RequestBody(required = false) JSONObject requestJsonObject) {
        Result result = new Result();
        JSONObject jsonObject = taskOperateService.getAssignee(requestJsonObject);
        result.setResult(jsonObject);

        return result;
    }

    @PostMapping("/getClaim")
    @ApiOperation("获取可认领任务列表")
    public Result getClaimList(@RequestBody(required = false) JSONObject jsonObject) {
        Result result = new Result();
        List<TaskVO> takes = taskOperateService.getClaimTaskList(jsonObject);

        result.setResult(takes);
        return result;
    }

    @GetMapping("/claimTask/{taskId}")
    @ApiOperation("认领任务")
    public Result claimTask(@PathVariable Long taskId) {
        Result result = new Result();
        Boolean bool = taskOperateService.claim(taskId);

        if (bool) {
            result.setSuccess(true);
            result.setMessage("操作成功");
        } else {
            result.setSuccess(false);
            result.setMessage("操作失败");
        }
        result.setResult(bool);

        return result;
    }

    /**
     *  参数jsonObject目前还没有用，后期可动态加载一些条件，前后端不用链条，只需要分别添加条件即可
     * @param jsonObject
     * @return
     */
    @PostMapping("/getPendingTakes")
    @ApiOperation("获取待处理任务列表")
    public Result getPendingTakes(@RequestBody(required = false) JSONObject jsonObject) {
        Result result = new Result();
        List<TaskVO> list = taskOperateService.getPendingTakes(jsonObject);
        result.setResult(list);

        return result;
    }

    @PostMapping("/handleTask")
    @ApiOperation("处理该任务返回相应的id")
    public Result handleTask(@RequestBody JSONObject jsonObject) {
        Result result = new Result();
        JSONObject json = taskOperateService.handle(jsonObject);
        result.setResult(json);

        return result;
    }

    @ApiOperation("选择bean接口")
    @PostMapping("/handlerList")
    public Result getHandlerList(@RequestParam String handlerType) {
        Result result = new Result();
        Set<String> handlerSet = taskOperateService.getHandlerLists(handlerType);
        result.setResult(handlerSet);

        return result;
    }

    @ApiOperation("获取当前任务权限")
    @PostMapping("/nodePower")
    public Result getnodePower(@RequestBody JSONObject jsonObject) {
        Result result = new Result();
        JSONArray jsonArray = taskOperateService.nodePower(jsonObject);
        result.setResult(jsonArray);

        return result;
    }



}
