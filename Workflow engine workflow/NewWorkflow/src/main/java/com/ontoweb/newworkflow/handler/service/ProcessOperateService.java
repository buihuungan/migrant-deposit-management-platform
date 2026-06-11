package com.ontoweb.newworkflow.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.VO.*;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/9/27 下午4:11
 * @description：
 * @modified By：
 * @version: $
 */
public interface ProcessOperateService {
    JSONObject start(Long processVo);

    List<ProcessListVO> getProcessList(String categoryId,String category);

    List<HistoryProcessInstanceVO> cancelProcess(JSONObject jsonObject);

    List<ProcessInstanceVO> inProgressProcessInstance(JSONObject jsonObject);

    List<HistoryProcessInstanceVO> completeProcessInstance(JSONObject jsonObject);

    JSONObject startBy(ProcessVo processVo,String s);

    Boolean deleteProcessInstanceFirst(JSONObject jsonObject);
}
