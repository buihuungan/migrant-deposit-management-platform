package com.ontoweb.newworkflow.handler.controller;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.service.DiagramService;
import com.ontoweb.newworkflow.handler.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/11/10 上午10:28
 * @description：返回给前端画图的控制器
 * @modified By：
 * @version: 1.0$
 */
@RestController
@RequestMapping("/diagram")
@Api(value = "diagram controller", tags = "diagram")
public class DiagramController {
    @Autowired
    private DiagramService diagramService;

    /**
     * <p>
     *     正常流程：传入procInstId，跟新节点的状态返回给前端
     *     退回流程：传入procInstId，跟新节点的状态返回给前端，且需要返回退回节点的连线，使前端能够知道哪些节点产生了退回操作
     *     拒绝流程：传入procInstId，跟新节点的状态返回给前端，每个节点更新为相应的状态即可，体现拒绝的节点
     * </p>
     * @param procInstId
     * @return
     */
    @GetMapping("/getByProcInstId")
    @ApiOperation("根据流程实例id获取流程图")
    public Result getDiagram(@RequestParam long procInstId) {
        Result result = new Result();
        JSONObject jsonObject = diagramService.drawDiagram(procInstId);
        result.setResult(jsonObject);
        result.setSuccess(true);
        if (jsonObject == null)
            result.setSuccess(false);

        return result;
    }




}
