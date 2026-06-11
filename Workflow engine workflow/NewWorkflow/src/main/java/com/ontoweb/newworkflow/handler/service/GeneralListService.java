package com.ontoweb.newworkflow.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.VO.ProcessAllVO;

import java.util.List;

public interface GeneralListService {

    List<ProcessAllVO> getClaimTaskList(JSONObject jsonObject);

    List<ProcessAllVO> getPendingTakes(JSONObject jsonObject);

    List<ProcessAllVO> cancelProcess(JSONObject jsonObject);

    List<ProcessAllVO> inProgressProcessInstance(JSONObject jsonObject);

    List<ProcessAllVO> completeProcessInstance(JSONObject jsonObject);
}
