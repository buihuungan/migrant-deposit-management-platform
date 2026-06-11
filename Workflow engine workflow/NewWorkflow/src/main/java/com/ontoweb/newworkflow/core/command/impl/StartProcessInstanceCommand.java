package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.ProcessInstanceState;
import com.ontoweb.newworkflow.core.process.handler.ProcessEventHandler;
import com.ontoweb.newworkflow.core.process.node.StartNode;
import com.ontoweb.newworkflow.core.service.StartProcessInfo;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import com.ontoweb.newworkflow.core.utils.ProcessListenerUtils;
import com.ontoweb.newworkflow.handler.dao.ProcessInstanceDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:49
 * @description：开启流程实例
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class StartProcessInstanceCommand{
    @Autowired
    private SaveProcessInstanceVariablesCommand saveProcessInstanceVariablesCommand;
    @Autowired
    private Context context;
    @Autowired
    private ProcessInstanceDao processInstanceDao;
    @Autowired
    private SaveHistoryProcessInstanceCommand saveHistoryProcessInstanceCommand;

    public ProcessInstance StartProcessInstanceCommand(ProcessDefinition process, Map<String,Object> variables, StartProcessInfo startProcessInfo, long parentProcessInstanceId) {
        ProcessInstance processInstance=new ProcessInstance();
        long piId= IDGenerator.getInstance().nextId();
        processInstance.addMetadata(StartProcessInfo.KEY, startProcessInfo);
        processInstance.setId(piId);
        processInstance.setRootId(piId);
        processInstance.setState(ProcessInstanceState.Start);
        processInstance.setProcessId(process.getId());
        processInstance.setCreateDate(new Date());
        processInstance.setPromoter(startProcessInfo.getPromoter());
        processInstance.setBusinessId(startProcessInfo.getBusinessId());
        //如果需要的话可以后期自行添加
        processInstance.setSubject(startProcessInfo.getSubject());
        processInstance.setTag(startProcessInfo.getTag());
        if(parentProcessInstanceId>0){
            processInstance.setParentId(parentProcessInstanceId);
        }
        processInstance.setHistoryProcessInstanceId(IDGenerator.getInstance().nextId());
        if(variables!=null && variables.size()>0){
            saveProcessInstanceVariablesCommand.saveProcessInstanceVariablesCommand(processInstance, variables);
        }

        context.getExpressionContext().createContext(processInstance, variables);
        StartNode startNode = process.getStartNode();
        processInstance.setCurrentTask(startNode.getName());
        processInstanceDao.save(processInstance);
        log.info("startNode1:{}", startNode.toString());
        saveHistoryProcessInstanceCommand.saveHistoryProcessInstanceCommand(processInstance);
        log.info("startNode2:{}",startNode.toString());
        processInstance.setCurrentNode(startNode.getName());
        ProcessListenerUtils.fireProcessStartListers(processInstance, context);
        String processEventHandlerBean=process.getEventHandlerBean();
        if(StringUtils.isNotEmpty(processEventHandlerBean)){
            ProcessEventHandler bean=(ProcessEventHandler)context.getApplicationContext().getBean(processEventHandlerBean);
            bean.start(processInstance, context);
        }
        startNode.createActivityHistory(context, processInstance);
        boolean isLeave=startNode.enter(context,processInstance);
        startNode.doEnterEventHandler(context, processInstance);
        if(isLeave){
            startNode.leave(context, processInstance, startProcessInfo.getSequenceFlowName());
        }
        return processInstance;
    }
}
