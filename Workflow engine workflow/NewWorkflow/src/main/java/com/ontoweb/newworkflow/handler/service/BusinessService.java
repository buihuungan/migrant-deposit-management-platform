package com.ontoweb.newworkflow.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.VO.ProcessUsersVo;
import com.ontoweb.newworkflow.handler.VO.ProcessVo;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/11/27 上午11:25
 * @description：业务上的一些代码
 * @modified By：
 * @version: $
 */
public interface BusinessService {
    Boolean grantRole(JSONObject jsonObject);

    List<JSONObject> getLists(String identification);

    List<JSONObject> getWaresByCategory(String category, String userId) ;

    Double getPercentageByProceeeInsId(long processInstanceId);

    JSONObject startClassByProcessId(ProcessVo processVo,String wareId);

    String getTaskId(ProcessVo processVo);

    Boolean saveDesc(JSONObject jsonObject);

    Double getPercentagecms(ProcessVo processVo);

    String getDesc(long processId);

    String getDescById(long id);

    JSONObject getDefault(ProcessVo processVo);

    JSONObject getRoleId(long processInstanceId);

    Long getId(Long processId);

    JSONObject getNumOfProcess(Long identification);

    JSONObject shutDownTask(ProcessUsersVo processVo);

    List<JSONObject> getWaresByUserId(String userId);

    JSONObject shutDownTaskByStudent(ProcessVo processVo);
}
