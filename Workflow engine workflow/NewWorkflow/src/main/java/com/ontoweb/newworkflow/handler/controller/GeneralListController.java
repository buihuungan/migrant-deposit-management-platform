package com.ontoweb.newworkflow.handler.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ontoweb.newworkflow.handler.VO.ProcessAllVO;
import com.ontoweb.newworkflow.handler.entity.ProcessMapping;
import com.ontoweb.newworkflow.handler.entity.UfloProcess;
import com.ontoweb.newworkflow.handler.mapper.GeneralMapper;
import com.ontoweb.newworkflow.handler.mapper.ProcessMapper;
import com.ontoweb.newworkflow.handler.mapper.ProcessMappingMapper;
import com.ontoweb.newworkflow.handler.service.GeneralListService;
import com.ontoweb.newworkflow.handler.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Api("通用列表系接口")
@RestController
@RequestMapping("/generalList")
public class GeneralListController {
    @Autowired
    private GeneralListService generalListService;
    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private ProcessMappingMapper processMappingMapper;
    @Autowired
    private GeneralMapper generalMapper;

    @PostMapping("getAllList")
    @ApiOperation("通用列表接口")
    public Result getAllList(@RequestBody JSONObject jsonObject) {

        Result result = new Result();
        JSONObject returnJsonObject = new JSONObject();
        //所有相关流程(减少查询次数)
        List<Long> processIdList = jsonObject.getJSONArray("processIdList").toJavaList(Long.class);
        //获取所有相关流程
        List<UfloProcess> processList = new ArrayList<>();
        //获取映射表信息
        List<ProcessMapping> processMappingList = new ArrayList<>();

        if (processIdList.size() != 0){
            //流程信息
            QueryWrapper<UfloProcess> ufloProcessWrapper = new QueryWrapper<>();
            ufloProcessWrapper.in("ID_", processIdList);
            processList = processMapper.selectList(ufloProcessWrapper);
            //流程映射信息
            QueryWrapper<ProcessMapping> processMappingWrapper = new QueryWrapper<>();
            processMappingWrapper.in("process_id", processIdList);
            processMappingList = processMappingMapper.selectList(processMappingWrapper);
        }else{
            //默认全部信息
            processList = processMapper.selectList(null);
            processMappingList = processMappingMapper.selectList(null);
        }

        //主表Map
        Map<String,String> mainTaleMap = processList.stream()
                .filter(process -> StringUtils.isNotEmpty(process.getProcessMainTable()) && StringUtils.isNotEmpty(process.getProcessMainKey()))
                .collect(Collectors.toMap(
                        UfloProcess::getProcessMainTable,
                        UfloProcess::getProcessMainKey,
                        (Key1,Key2) -> Key2
                ));
        //关联表Map
        Map<String,String> associationTaleMap = processList.stream()
                .filter(process -> StringUtils.isNotEmpty(process.getProcessAssociationTable()) && StringUtils.isNotEmpty(process.getProcessAssociationKey()))
                .collect(Collectors.toMap(
                        UfloProcess::getProcessAssociationTable,
                        UfloProcess::getProcessAssociationKey,
                        (Key1,Key2) -> Key2
                ));
        //主表数据
        Map<String,Map<String,JSONObject>> mainTableDataMap = new HashMap<>();
        for (String table : mainTaleMap.keySet()) {
            List<JSONObject> tableList = generalMapper.selectTableData(table);
            String mainKey = mainTaleMap.get(table);
            Map<String,JSONObject> tableDataMap = tableList.stream().collect(
                    Collectors.toMap(
                       JSONObject -> JSONObject.getString(mainKey),
                       JSONObject -> JSONObject
                    ));
            mainTableDataMap.put(table, tableDataMap);
        }
        //关联表数据
        Map<String,Map<String,JSONObject>> associationTableDataMap = new HashMap<>();
        for (String table : associationTaleMap.keySet()) {
            List<JSONObject> tableList = generalMapper.selectTableData(table);
            String associationKey = associationTaleMap.get(table);
            Map<String,JSONObject> tableDataMap = tableList.stream().collect(
                    Collectors.toMap(
                            JSONObject -> JSONObject.getString(associationKey),
                            JSONObject -> JSONObject
                    ));
            associationTableDataMap.put(table, tableDataMap);
        }
        //流程Map
        Map<Long,UfloProcess> processMap = processList.stream().collect(
                Collectors.toMap(
                        UfloProcess::getId,
                        UfloProcess -> UfloProcess,
                        (Key1,Key2) -> Key2
                ));
        //映射流程实例Map
        Map<Long,ProcessMapping> processMappingMap = processMappingList.stream().collect(
                Collectors.toMap(
                        ProcessMapping::getProcessInstanceId,
                        ProcessMapping -> ProcessMapping,
                        (Key1,Key2) -> Key2
                ));
        //映射主表Map
        Map<String,Map<String,ProcessMapping>> mainTableProcessMappingMap = processMappingList.stream()
                .collect(Collectors.groupingBy(
                        ProcessMapping::getTableName,
                        Collectors.toMap(
                                ProcessMapping::getMainId,
                                ProcessMapping -> ProcessMapping
                        )
                ));
        //整体数据
        List<ProcessAllVO> dataList = new ArrayList<>();
        //待认领
        List<ProcessAllVO> listClaim = new ArrayList<>();
        //正在进行
        List<ProcessAllVO> listPending = new ArrayList<>();
        //拒绝
        List<ProcessAllVO> listCancel = new ArrayList<>();
        //进行
        List<ProcessAllVO> listInstance = new ArrayList<>();
        //完成
        List<ProcessAllVO> listComplete = new ArrayList<>();

        //类型获取
        JSONArray jsonArray = jsonObject.getJSONArray("applyState");
        //数据拼接
        for (int i = 0; i < jsonArray.size(); i++) {
            String applyState = jsonArray.getString(i);
            switch (applyState){
                case "claim":{
                    listClaim = generalListService.getClaimTaskList(jsonObject);
                    for (ProcessAllVO processAllVO : listClaim) {
                        JSONObject allData = new JSONObject();
                        JSONObject mainJSONObject = new JSONObject();
                        JSONObject associationJSONObject = new JSONObject();
                        Long dataProcessId = processAllVO.getProcessId();
                        Long dataProcessInstanceId = processAllVO.getProcessInstanceId();
                        processAllVO.setDataType("claim");
                        String processMainTable = null;
                        String processAssociationTable = null;
                        String associationTableDataId = null;
                        //默认数据
                        UfloProcess ufloProcess = processMap.get(dataProcessId);
                        if (processMappingMap.containsKey(dataProcessInstanceId)){
                            processMainTable = ufloProcess.getProcessMainTable();
                            ProcessMapping processMapping = processMappingMap.get(dataProcessInstanceId);
                            mainJSONObject = mainTableDataMap.get(processMainTable).get(processMapping.getMainId());
                            //主表数据
                            processAllVO.setProcessMainTable(processMainTable);
                            processAllVO.setProcessMainKey(ufloProcess.getProcessMainKey());
                            //存放数据
                            allData.put(processMainTable, mainJSONObject);
                            if (StringUtils.isNotEmpty(ufloProcess.getProcessAssociationTable())){
                                processAssociationTable = ufloProcess.getProcessAssociationTable();
                                associationTableDataId = mainJSONObject.getString(ufloProcess.getMainTableAssociationKey());
                                associationJSONObject = associationTableDataMap.get(processAssociationTable).get(associationTableDataId);
                                //存放数据
                                allData.put(processAssociationTable, associationJSONObject);
                            }
                        }
                        //回溯 需要传入对应的流程id
                        while (processAssociationTable != null && associationTableDataId != null){
                            //上个流程的关联表就是本流程的主表
                            ProcessMapping processMapping =  mainTableProcessMappingMap.get(processAssociationTable).get(associationTableDataId);
                            UfloProcess tempUfloProcess = processMap.get(processMapping.getProcessId());
                            if (StringUtils.isNotEmpty(tempUfloProcess.getProcessAssociationTable())){
                                //获取回溯流程的 关联表数据
                                processAssociationTable = tempUfloProcess.getProcessAssociationTable();
                                associationTableDataId =  associationJSONObject.getString(tempUfloProcess.getMainTableAssociationKey());
                                associationJSONObject = associationTableDataMap.get(processAssociationTable).get(associationTableDataId);
                                //用流程id标识
                                allData.put(processAssociationTable, associationJSONObject);
                            }else{
                                //结束回溯
                                processAssociationTable = null;
                                associationTableDataId = null;
                            }
                        }
                        processAllVO.setAllData(allData);
                    }
                    break;
                }
                case "pending":{
                    listPending = generalListService.getPendingTakes(jsonObject);
                    for (ProcessAllVO processAllVO : listPending) {
                        JSONObject allData = new JSONObject();
                        JSONObject mainJSONObject = new JSONObject();
                        JSONObject associationJSONObject = new JSONObject();
                        Long dataProcessId = processAllVO.getProcessId();
                        Long dataProcessInstanceId = processAllVO.getProcessInstanceId();
                        processAllVO.setDataType("pending");
                        String processMainTable = null;
                        String processAssociationTable = null;
                        String associationTableDataId = null;
                        //默认数据
                        UfloProcess ufloProcess = processMap.get(dataProcessId);
                        if (processMappingMap.containsKey(dataProcessInstanceId)){
                            processMainTable = ufloProcess.getProcessMainTable();
                            ProcessMapping processMapping = processMappingMap.get(dataProcessInstanceId);
                            mainJSONObject = mainTableDataMap.get(processMainTable).get(processMapping.getMainId());
                            //主表数据
                            processAllVO.setProcessMainTable(processMainTable);
                            processAllVO.setProcessMainKey(ufloProcess.getProcessMainKey());
                            //存放数据
                            allData.put(processMainTable, mainJSONObject);
                            if (StringUtils.isNotEmpty(ufloProcess.getProcessAssociationTable())){
                                processAssociationTable = ufloProcess.getProcessAssociationTable();
                                associationTableDataId = mainJSONObject.getString(ufloProcess.getMainTableAssociationKey());
                                associationJSONObject = associationTableDataMap.get(processAssociationTable).get(associationTableDataId);
                                //存放数据
                                allData.put(processAssociationTable, associationJSONObject);
                            }
                        }
                        //回溯 需要传入对应的流程id
                        while (processAssociationTable != null && associationTableDataId != null){
                            //上个流程的关联表就是本流程的主表
                            ProcessMapping processMapping =  mainTableProcessMappingMap.get(processAssociationTable).get(associationTableDataId);
                            UfloProcess tempUfloProcess = processMap.get(processMapping.getProcessId());
                            if (StringUtils.isNotEmpty(tempUfloProcess.getProcessAssociationTable())){
                                //获取回溯流程的 关联表数据
                                processAssociationTable = tempUfloProcess.getProcessAssociationTable();
                                associationTableDataId =  associationJSONObject.getString(tempUfloProcess.getMainTableAssociationKey());
                                associationJSONObject = associationTableDataMap.get(processAssociationTable).get(associationTableDataId);
                                //用流程id标识
                                allData.put(processAssociationTable, associationJSONObject);
                            }else{
                                //结束回溯
                                processAssociationTable = null;
                                associationTableDataId = null;
                            }
                        }
                        processAllVO.setAllData(allData);
                    }
                    break;
                }
                case "cancel":{
                    listCancel = generalListService.cancelProcess(jsonObject);
                    for (ProcessAllVO processAllVO : listCancel) {
                        JSONObject allData = new JSONObject();
                        JSONObject mainJSONObject = new JSONObject();
                        JSONObject associationJSONObject = new JSONObject();
                        Long dataProcessId = processAllVO.getProcessId();
                        Long dataProcessInstanceId = processAllVO.getProcessInstanceId();
                        processAllVO.setDataType("cancel");
                        String processMainTable = null;
                        String processAssociationTable = null;
                        String associationTableDataId = null;
                        //默认数据
                        UfloProcess ufloProcess = processMap.get(dataProcessId);
                        if (processMappingMap.containsKey(dataProcessInstanceId)){
                            processMainTable = ufloProcess.getProcessMainTable();
                            ProcessMapping processMapping = processMappingMap.get(dataProcessInstanceId);
                            mainJSONObject = mainTableDataMap.get(processMainTable).get(processMapping.getMainId());
                            //主表数据
                            processAllVO.setProcessMainTable(processMainTable);
                            processAllVO.setProcessMainKey(ufloProcess.getProcessMainKey());
                            //存放数据
                            allData.put(processMainTable, mainJSONObject);
                            if (StringUtils.isNotEmpty(ufloProcess.getProcessAssociationTable())){
                                processAssociationTable = ufloProcess.getProcessAssociationTable();
                                associationTableDataId = mainJSONObject.getString(ufloProcess.getMainTableAssociationKey());
                                associationJSONObject = associationTableDataMap.get(processAssociationTable).get(associationTableDataId);
                                //存放数据
                                allData.put(processAssociationTable, associationJSONObject);
                            }
                        }
                        //回溯 需要传入对应的流程id
                        while (processAssociationTable != null && associationTableDataId != null){
                            //上个流程的关联表就是本流程的主表
                            ProcessMapping processMapping =  mainTableProcessMappingMap.get(processAssociationTable).get(associationTableDataId);
                            UfloProcess tempUfloProcess = processMap.get(processMapping.getProcessId());
                            if (StringUtils.isNotEmpty(tempUfloProcess.getProcessAssociationTable())){
                                //获取回溯流程的 关联表数据
                                processAssociationTable = tempUfloProcess.getProcessAssociationTable();
                                associationTableDataId =  associationJSONObject.getString(tempUfloProcess.getMainTableAssociationKey());
                                associationJSONObject = associationTableDataMap.get(processAssociationTable).get(associationTableDataId);
                                //用流程id标识
                                allData.put(processAssociationTable, associationJSONObject);
                            }else{
                                //结束回溯
                                processAssociationTable = null;
                                associationTableDataId = null;
                            }
                        }
                        processAllVO.setAllData(allData);
                    }
                    break;
                }
                case "instance":{
                    listInstance = generalListService.inProgressProcessInstance(jsonObject);
                    for (ProcessAllVO processAllVO : listInstance) {
                        JSONObject allData = new JSONObject();
                        JSONObject mainJSONObject = new JSONObject();
                        JSONObject associationJSONObject = new JSONObject();
                        Long dataProcessId = processAllVO.getProcessId();
                        Long dataProcessInstanceId = processAllVO.getProcessInstanceId();
                        processAllVO.setDataType("instance");
                        String processMainTable = null;
                        String processAssociationTable = null;
                        String associationTableDataId = null;
                        //默认数据
                        UfloProcess ufloProcess = processMap.get(dataProcessId);
                        if (processMappingMap.containsKey(dataProcessInstanceId)){
                            processMainTable = ufloProcess.getProcessMainTable();
                            ProcessMapping processMapping = processMappingMap.get(dataProcessInstanceId);
                            mainJSONObject = mainTableDataMap.get(processMainTable).get(processMapping.getMainId());
                            //主表数据
                            processAllVO.setProcessMainTable(processMainTable);
                            processAllVO.setProcessMainKey(ufloProcess.getProcessMainKey());
                            //存放数据
                            allData.put(processMainTable, mainJSONObject);
                            if (StringUtils.isNotEmpty(ufloProcess.getProcessAssociationTable())){
                                processAssociationTable = ufloProcess.getProcessAssociationTable();
                                associationTableDataId = mainJSONObject.getString(ufloProcess.getMainTableAssociationKey());
                                associationJSONObject = associationTableDataMap.get(processAssociationTable).get(associationTableDataId);
                                //存放数据
                                allData.put(processAssociationTable, associationJSONObject);
                            }
                        }
                        //回溯 需要传入对应的流程id
                        while (processAssociationTable != null && associationTableDataId != null){
                            //上个流程的关联表就是本流程的主表
                            ProcessMapping processMapping =  mainTableProcessMappingMap.get(processAssociationTable).get(associationTableDataId);
                            UfloProcess tempUfloProcess = processMap.get(processMapping.getProcessId());
                            if (StringUtils.isNotEmpty(tempUfloProcess.getProcessAssociationTable())){
                                //获取回溯流程的 关联表数据
                                processAssociationTable = tempUfloProcess.getProcessAssociationTable();
                                associationTableDataId =  associationJSONObject.getString(tempUfloProcess.getMainTableAssociationKey());
                                associationJSONObject = associationTableDataMap.get(processAssociationTable).get(associationTableDataId);
                                //用流程id标识
                                allData.put(processAssociationTable, associationJSONObject);
                            }else{
                                //结束回溯
                                processAssociationTable = null;
                                associationTableDataId = null;
                            }
                        }
                        processAllVO.setAllData(allData);
                    }
                    break;
                }
                case "complete":{
                    listComplete = generalListService.completeProcessInstance(jsonObject);
                    for (ProcessAllVO processAllVO : listComplete) {
                        JSONObject allData = new JSONObject();
                        JSONObject mainJSONObject = new JSONObject();
                        JSONObject associationJSONObject = new JSONObject();
                        Long dataProcessId = processAllVO.getProcessId();
                        Long dataProcessInstanceId = processAllVO.getProcessInstanceId();
                        processAllVO.setDataType("complete");
                        String processMainTable = null;
                        String processAssociationTable = null;
                        String associationTableDataId = null;
                        //默认数据
                        UfloProcess ufloProcess = processMap.get(dataProcessId);
                        if (processMappingMap.containsKey(dataProcessInstanceId)){
                            processMainTable = ufloProcess.getProcessMainTable();
                            ProcessMapping processMapping = processMappingMap.get(dataProcessInstanceId);
                            mainJSONObject = mainTableDataMap.get(processMainTable).get(processMapping.getMainId());
                            //主表数据
                            processAllVO.setProcessMainTable(processMainTable);
                            processAllVO.setProcessMainKey(ufloProcess.getProcessMainKey());
                            //存放数据
                            allData.put(processMainTable, mainJSONObject);
                            if (StringUtils.isNotEmpty(ufloProcess.getProcessAssociationTable())){
                                processAssociationTable = ufloProcess.getProcessAssociationTable();
                                associationTableDataId = mainJSONObject.getString(ufloProcess.getMainTableAssociationKey());
                                associationJSONObject = associationTableDataMap.get(processAssociationTable).get(associationTableDataId);
                                //存放数据
                                allData.put(processAssociationTable, associationJSONObject);
                            }
                        }
                        //回溯 需要传入对应的流程id
                        while (processAssociationTable != null && associationTableDataId != null){
                            //上个流程的关联表就是本流程的主表
                            ProcessMapping processMapping =  mainTableProcessMappingMap.get(processAssociationTable).get(associationTableDataId);
                            UfloProcess tempUfloProcess = processMap.get(processMapping.getProcessId());
                            if (StringUtils.isNotEmpty(tempUfloProcess.getProcessAssociationTable())){
                                //获取回溯流程的 关联表数据
                                processAssociationTable = tempUfloProcess.getProcessAssociationTable();
                                associationTableDataId =  associationJSONObject.getString(tempUfloProcess.getMainTableAssociationKey());
                                associationJSONObject = associationTableDataMap.get(processAssociationTable).get(associationTableDataId);
                                //用流程id标识
                                allData.put(processAssociationTable, associationJSONObject);
                            }else{
                                //结束回溯
                                processAssociationTable = null;
                                associationTableDataId = null;
                            }
                        }
                        processAllVO.setAllData(allData);
                    }
                    break;
                }
                default:
                    result.setSuccess(false);
                    result.setCode(500);
                    result.setMessage("类型筛选出错");
                    return result;
            }
        }

        dataList.addAll(listClaim);
        dataList.addAll(listPending);
        dataList.addAll(listCancel);
        dataList.addAll(listInstance);
        dataList.addAll(listComplete);

        //按生成时间排序 降序
        dataList.sort(Comparator.comparing(ProcessAllVO::getCreateDate).reversed());

        returnJsonObject.put("dataList", dataList);
        returnJsonObject.put("total", dataList.size());
        result.setResult(returnJsonObject);
        return result;
    }
}
