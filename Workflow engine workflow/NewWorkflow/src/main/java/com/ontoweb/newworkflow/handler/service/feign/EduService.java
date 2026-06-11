package com.ontoweb.newworkflow.handler.service.feign;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/11/27 下午3:11
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@FeignClient("EDUMANAGER")
public interface EduService {

    @PostMapping("/manager/grantRole")
    Result<?> grantRole(@RequestBody JSONObject userRoleModel);

    @GetMapping("/eduStudentGroupDetail/getGroupId")
    Result<?> getGroupId(@RequestParam("userId") String userId, @RequestParam("planId")String planId, @RequestParam("lessonId")String lessonId);

    @GetMapping("/eduPlan/selectWareId/{category}/{userId}")
    JSONObject selectWareIdByUserId(@PathVariable("userId") String userId, @PathVariable("category") String category);

    @ApiOperation("查询该学生所在班级的所选择的所有模型课")
    @GetMapping("/eduPlan/selectWareId/{userId}")
    JSONObject selectByUserId(@PathVariable("userId") String userId);


}
