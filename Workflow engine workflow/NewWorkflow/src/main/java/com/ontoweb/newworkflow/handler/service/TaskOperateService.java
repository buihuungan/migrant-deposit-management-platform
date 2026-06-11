package com.ontoweb.newworkflow.handler.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.VO.TaskVO;

import java.util.List;
import java.util.Set;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/9 下午2:42
 * @description：
 * @modified By：
 * @version: $
 */
public interface TaskOperateService {
    JSONObject completeTask(JSONObject jsonObject);

    Boolean withdraw(JSONObject jsonObject);

    Boolean cancelTask(JSONObject jsonObject);

    JSONObject getAssignee(JSONObject requestJsonObject);

    List<TaskVO> getClaimTaskList(JSONObject jsonObject);

    Boolean claim(Long taskId);

    List<TaskVO> getPendingTakes(JSONObject jsonObject);

    Set<String> getHandlerLists(String handlerType);

    JSONObject handle(JSONObject jsonObject);

    JSONArray nodePower(JSONObject jsonObject);
}
