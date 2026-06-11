package com.ontoweb.newworkflow.handler.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.core.model.HistoryActivity;
import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.service.HistoryService;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.TaskService;
import com.ontoweb.newworkflow.handler.VO.StartProcessVO;
import com.ontoweb.newworkflow.handler.dao.BlobDao;
import com.ontoweb.newworkflow.handler.dao.DiagramDao;
import com.ontoweb.newworkflow.handler.dao.GetFormOnlineId;
import com.ontoweb.newworkflow.handler.entity.Diagram;
import com.ontoweb.newworkflow.handler.entity.VideoVo;
import com.ontoweb.newworkflow.handler.service.DiagramService;
import com.ontoweb.newworkflow.handler.utils.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/11/10 上午10:34
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class DiagramServiceImpl implements DiagramService {
    @Autowired
    private DiagramDao diagramDao;
    @Autowired
    private ProcessService processService;
    @Autowired
    private BlobDao blobDao;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private GetFormOnlineId getFormOnlineId;

    @Override
    public JSONObject drawDiagram(long procInstId) {
        JSONObject jsonObject = new JSONObject();
        List<Diagram> diagramInformation = new ArrayList<>();
        List<Diagram> hisTaskInformation = diagramDao.getHisTaskInformation(procInstId);
        List<Diagram> taskInformation = diagramDao.getTaskInformation(procInstId);
        diagramInformation.addAll(hisTaskInformation);
        diagramInformation.addAll(taskInformation);
        for (Iterator<Diagram> it = diagramInformation.iterator(); it.hasNext();) {
            Diagram diagram = it.next();
            for (Diagram taskDiagram : taskInformation) {
                if (diagram.getTaskId() == taskDiagram.getId()) {
                    it.remove();
                }
            }
        }
        // 任务历史信息
        jsonObject.put("historyInfo", diagramInformation);
        // 任务节点与对应的状态信息
        Map<String, Diagram> nodeStateMap = new HashMap<>();
        // 存放退回操作的list，这里需要用list存放，因为有可能退回几遍
        for (Diagram diagram : diagramInformation) {
            nodeStateMap.put(diagram.getNodeName(), diagram);
        }

        HistoryProcessInstance historyProcessInstance = historyService.getHistoryProcessInstance(procInstId);
        // 历史活动信息，因为结束节点不会存放在历史任务中
        List<HistoryActivity> historyActivities = historyService.getHistoryActivitysByHistoryProcesssInstanceId(historyProcessInstance.getId());
        Map<String, String> activityNodeNameMap = new HashMap<>();
        for (HistoryActivity historyActivity : historyActivities) {
            activityNodeNameMap.put(historyActivity.getNodeName(), CommonConstant.COMPLETED);
        }

        ProcessInstance processInstance = processService.getProcessInstanceById(procInstId);
        long processId;
        if (historyProcessInstance == null) {
            processId = processInstance.getProcessId();
        } else {
            processId = historyProcessInstance.getProcessId();
        }
        Blob blob = blobDao.findblobProcessId(processId);
        byte[] blobValue = blob.getBlobValue();
        try {
            String str = new String(blobValue, "UTF-8");
            log.info(str);
            JSONObject inputJson = JSONObject.parseObject(str);

            String nodeListStr = inputJson.getString("nodeList");
            List<JSONObject> nodeList = JSONArray.parseArray(nodeListStr, JSONObject.class);

            // 节点列表，为了获取退回的前一个节点
//            List<Object> arrayList = new ArrayList<>();
            for (int i = 0; i < nodeList.size(); i++) {
                JSONObject nodeJson = nodeList.get(i);

                log.info("nodeJson:{}",nodeJson);
                String nodeName = nodeJson.getString("name");
//                VideoVo videoVo = new VideoVo();
//
//                String formDesignerId = nodeJson.getString("formDesignerId");
//                String onlineTableId = nodeJson.getString("onlineTableId");
//                String onlineDataId = nodeJson.getString("onlineDataId");
//                videoVo.setFormDesignerId(formDesignerId)
//                        .setOnlineTableId(onlineTableId)
//                        .setOnlineDataId(onlineDataId);
//                arrayList.add(videoVo);

                if (nodeStateMap.containsKey(nodeName)) {
                    nodeJson.put("state", nodeStateMap.get(nodeName).getState());
                }

                // 最后一个节点是结束节点
                if (i == nodeList.size() - 1) {
                    if (activityNodeNameMap.containsKey(nodeName)) {
                        nodeJson.put("state", activityNodeNameMap.get(nodeName));
                    }
                }
            }

            // 从uflo_his_task中根据processInstId 查询已经行节点的dataId和FormId

            List<Object> arrayList = new ArrayList<>();
            for (Diagram diagram : diagramInformation) {
                String allId = diagram.getUrl();
                if (allId == null) {
                    allId = "";
                }
                String[] allIdList = allId.split("-");
                String formDesignerId = allIdList.length == 3 ? allIdList[0] : "";
                String onlineDataId = allIdList.length == 3 ? allIdList[2] : "";
                String onlineTableId = allIdList.length == 3 ? allIdList[1] : "";
                VideoVo videoVo = new VideoVo();
                videoVo.setFormDesignerId(formDesignerId)
                        .setOnlineTableId(onlineTableId)
                        .setOnlineDataId(onlineDataId);
                arrayList.add(videoVo);
            }

            inputJson.put("nodeList", nodeList);
            jsonObject.put("startProcessVO",arrayList);
            jsonObject.put("returnJson", inputJson);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return jsonObject;
    }
}
