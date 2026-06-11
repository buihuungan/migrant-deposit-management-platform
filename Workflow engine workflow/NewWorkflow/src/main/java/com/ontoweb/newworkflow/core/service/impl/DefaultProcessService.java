package com.ontoweb.newworkflow.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.command.impl.*;
import com.ontoweb.newworkflow.core.deploy.ProcessDeployer;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.variable.Variable;
import com.ontoweb.newworkflow.core.query.ProcessInstanceQuery;
import com.ontoweb.newworkflow.core.query.ProcessQuery;
import com.ontoweb.newworkflow.core.query.ProcessVariableQuery;
import com.ontoweb.newworkflow.core.query.impl.ProcessInstanceQueryImpl;
import com.ontoweb.newworkflow.core.query.impl.ProcessQueryImpl;
import com.ontoweb.newworkflow.core.query.impl.ProcessVariableQueryImpl;
import com.ontoweb.newworkflow.core.service.CacheService;
import com.ontoweb.newworkflow.core.service.ProcessInterceptor;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.StartProcessInfo;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午10:09
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class DefaultProcessService implements ProcessService, ApplicationContextAware {
    private Collection<ProcessInterceptor> processInterceptors;
    @Autowired
    private ProcessDeployer processDeployer;
    @Autowired
    private GetProcessCommand getProcessCommand;
    @Autowired
    private StartProcessInstanceCommand startProcessInstanceCommand;
    @Autowired
    private Context context;
    @Autowired
    private SaveProcessInstanceVariablesCommand saveProcessInstanceVariablesCommand;
    @Autowired
    private GetProcessInstanceCommand getProcessInstanceCommand;
    @Autowired
    private CommandService commandService;

    @Override
    public synchronized ProcessDefinition getProcessById(long processId) {
        CacheService cache= EnvironmentUtils.getEnvironment().getCache();

//        ProcessDefinition process=getProcessCommand.GetProcessCommand(processId,null,0,null);
//        cache.putProcessDefinition(process.getId(),process);
//        return process;

        if(cache.containsProcessDefinition(processId)){
            return cache.getProcessDefinition(processId);
        }else{
            ProcessDefinition process=getProcessCommand.GetProcessCommand(processId,null,0,null);
            cache.putProcessDefinition(process.getId(),process);
            return process;
        }
    }

    @Override
    public void deleteProcessVariable(String key, long processInstanceId) {
        commandService.executeCommand(new DeleteProcessVariableCommand(key,processInstanceId));
    }

    @Override
    public ProcessDefinition getProcessByKey(String key) {
        CacheService cache=EnvironmentUtils.getEnvironment().getCache();
        for(ProcessDefinition pd:cache.loadAllProcessDefinitions()){
            if(pd.getKey()==null)continue;
            if(pd.getKey().equals(key)){
                return pd;
            }
        }
        ProcessDefinition process=commandService.executeCommand(new GetProcessByKeyCommand(key));
        if(process!=null){
            cache.putProcessDefinition(process.getId(), process);
        }
        return process;
    }

    @Override
    public ProcessDefinition getProcessByName(String processName) {
        return getProcessCommand.GetProcessCommand(0,processName,0,null);
    }

    @Override
    public ProcessDefinition getProcessByName(String processName, String categoryId) {
        return getProcessCommand.GetProcessCommand(0,processName,0,categoryId);
    }

    @Override
    public  synchronized ProcessDefinition getProcessByName(String processName, final int version) {
        CacheService cache=EnvironmentUtils.getEnvironment().getCache();
        ProcessDefinition target=null;
        for(ProcessDefinition p:cache.loadAllProcessDefinitions()){
            if(p.getName().equals(processName) && p.getVersion()==version){
                target=p;
                break;
            }
        }
        if(target!=null){
            return target;
        }else{
            ProcessDefinition process=getProcessCommand.GetProcessCommand(0,processName,version,null);
            cache.putProcessDefinition(process.getId(), process);
            return process;
        }
    }

    @Override
    public ProcessInstance startProcessByKey(String key, StartProcessInfo startProcessInfo) {
        ProcessDefinition process=getProcessByKey(key);
        if(process==null){
            throw new IllegalArgumentException("Process definition ["+key+"] was not exist!");
        }
        checkProcessEffectDate(process);
        return startProcess(process,startProcessInfo,startProcessInfo.getVariables());
    }

    @Override
    public ProcessInstance startProcessById(long processId, StartProcessInfo startProcessInfo) {
        ProcessDefinition process=getProcessById(processId);
        if(process==null){
            throw new IllegalArgumentException("Process definition ["+processId+"] was not exist!");
        }
        checkProcessEffectDate(process);
        return startProcess(process,startProcessInfo,startProcessInfo.getVariables());
    }

    private void checkProcessEffectDate(ProcessDefinition process){
        Date effectDate=process.getEffectDate();
        if(effectDate==null){
            return;
        }
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(effectDate.getTime()>(new Date()).getTime()){
            throw new IllegalStateException("Process definition "+process.getName()+" effect date is "+sd.format(effectDate)+".");
        }
    }

    @Override
    public ProcessInstance startProcessByName(String processName, StartProcessInfo startProcessInfo) {
        ProcessDefinition process=getProcessByName(processName);
        if(process==null){
            throw new IllegalArgumentException("Process definition ["+processName+"] was not exist!");
        }
        checkProcessEffectDate(process);
        return startProcess(process,startProcessInfo,startProcessInfo.getVariables());
    }

    private ProcessInstance startProcess(ProcessDefinition process, StartProcessInfo startProcessInfo, Map<String,Object> variables){
        return startProcessInstanceCommand.StartProcessInstanceCommand(process,variables,startProcessInfo,0);
    }

    @Override
    public ProcessInstance startProcessByName(String processName, StartProcessInfo startProcessInfo, int version) {
        ProcessDefinition process=getProcessByName(processName,version);
        if(process==null){
            throw new IllegalArgumentException("Process definition ["+processName+"-"+version+"] was not exist!");
        }
        checkProcessEffectDate(process);
        return startProcess(process,startProcessInfo,startProcessInfo.getVariables());
    }

    @Override
    public synchronized ProcessDefinition deployProcess(JSONObject inputJson) {
        return processDeployer.deploy(inputJson);
    }

    @Override
    public ProcessDefinition deployProcess(JSONObject inputJson, long processId) {
        CacheService cache=EnvironmentUtils.getEnvironment().getCache();
        int version=getProcessById(processId).getVersion();
        ProcessDefinition process=processDeployer.deploy(inputJson,version,processId);
        cache.putProcessDefinition(processId, process);
        doProcessInterceptors(process,true);
        return process;
    }

    @Override
    public synchronized ProcessInstance getProcessInstanceById(long processInstanceId) {
        return getProcessInstanceCommand.getProcessInstanceCommand(processInstanceId);
    }
    public void setProcessDeployer(ProcessDeployer processDeployer) {
        this.processDeployer = processDeployer;
    }

    public void setCommandService(CommandService commandService) {
        this.commandService = commandService;
    }

    @Override
    public void saveProcessVariable(long processInstanceId, String key, Object value) {
        Map<String,Object> variables=new HashMap<String,Object>();
        variables.put(key, value);
        saveProcessVariables(processInstanceId, variables);
    }

    @Override
    public void saveProcessVariables(long processInstanceId, Map<String, Object> variables) {
        ProcessInstance pi = getProcessInstanceById(processInstanceId);
        saveProcessInstanceVariablesCommand.saveProcessInstanceVariablesCommand(pi,variables);
    }

    @Override
    public List<Variable> getProcessVariables(long processInstanceId) {
        ProcessVariableQuery query=new ProcessVariableQueryImpl(commandService);
        query.processInstanceId(processInstanceId);
        return query.list();
    }

    @Override
    public List<Variable> getProcessVariables(ProcessInstance processInstance) {
        ProcessVariableQuery query=new ProcessVariableQueryImpl(commandService);
        query.rootprocessInstanceId(processInstance.getRootId());
        return query.list();
    }

    @Override
    public Object getProcessVariable(String key, ProcessInstance processInstance) {
        Object obj=commandService.executeCommand(new GetExpressionValueCommand(processInstance.getId(),key));
        if(obj!=null)return obj;
        Variable var=commandService.executeCommand(new GetProcessInstanceVariableCommand(key,processInstance));
        if(var!=null){
            return var.getValue();
        }
        return null;
    }

    @Override
    public Object getProcessVariable(String key, long processInstanceId) {
        Object obj=commandService.executeCommand(new GetExpressionValueCommand(processInstanceId,key));
        if(obj!=null)return obj;
        ProcessInstance pi=getProcessInstanceById(processInstanceId);
        return getProcessVariable(key, pi);
    }

    @Override
    public void deleteProcessInstance(ProcessInstance processInstance) {
        commandService.executeCommand(new DeleteProcessInstanceCommand(processInstance));
    }

    @Override
    public void deleteProcessInstanceById(long processInstanceId) {
        deleteProcessInstance(getProcessInstanceById(processInstanceId));
    }

    @Override
    public ProcessInstanceQuery createProcessInstanceQuery() {
        return new ProcessInstanceQueryImpl(commandService);
    }

    @Override
    public ProcessVariableQuery createProcessVariableQuery() {
        return new ProcessVariableQueryImpl(commandService);
    }

    @Override
    public ProcessQuery createProcessQuery() {
        return new ProcessQueryImpl(commandService);
    }

    @Override
    public void deleteProcess(long processId) {
        ProcessDefinition processDefinition=getProcessById(processId);
        deleteProcess(processDefinition);
    }

    @Override
    public void deleteProcess(String processKey) {
        ProcessDefinition processDefinition=getProcessByKey(processKey);
        deleteProcess(processDefinition);
    }

    @Override
    public void updateProcessForMemory(long processId) {
        ProcessDefinition process=getProcessCommand.GetProcessCommand(processId,null,0,null);
        if(process!=null){
            CacheService cache=EnvironmentUtils.getEnvironment().getCache();
            cache.putProcessDefinition(process.getId(), process);
        }
    }

    @Override
    public void deleteProcess(ProcessDefinition processDefinition) {
        commandService.executeCommand(new DeleteProcessDefinitionCommand(processDefinition));
        CacheService cache=EnvironmentUtils.getEnvironment().getCache();
        cache.removeProcessDefinition(processDefinition.getId());
        doProcessInterceptors(processDefinition,false);
    }

    @Override
    public void deleteProcessFromMemory(long processId) {
        CacheService cache=EnvironmentUtils.getEnvironment().getCache();
        cache.removeProcessDefinition(processId);
    }
    @Override
    public void deleteProcessFromMemory(ProcessDefinition processDefinition) {
        CacheService cache=EnvironmentUtils.getEnvironment().getCache();
        cache.removeProcessDefinition(processDefinition.getId());
    }
    @Override
    public void deleteProcessFromMemory(String processKey) {
        ProcessDefinition processDefinition=getProcessByKey(processKey);
        CacheService cache=EnvironmentUtils.getEnvironment().getCache();
        cache.removeProcessDefinition(processDefinition.getId());
    }

    private void doProcessInterceptors(ProcessDefinition process,boolean update){
        for(ProcessInterceptor interceptor:processInterceptors){
            if(update){
                interceptor.updateProcess(process);
            }else{
                interceptor.deleteProcess(process);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        processInterceptors=applicationContext.getBeansOfType(ProcessInterceptor.class).values();
    }
}
