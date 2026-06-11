package com.ontoweb.newworkflow.core.service;

import com.ontoweb.newworkflow.core.expr.impl.ProcessMapContext;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午3:54
 * @description：
 * @modified By：
 * @version: $
 */
public class DefaultMemoryCacheService implements CacheService{
    private Map<Long, ProcessDefinition> processDefinitionMap=new HashMap<Long,ProcessDefinition>();
    private Map<Long, ProcessMapContext> contextMap=new Hashtable<Long,ProcessMapContext>();
    @Override
    public ProcessDefinition getProcessDefinition(long processId) {
        return processDefinitionMap.get(processId);
    }
    @Override
    public void putProcessDefinition(long processId, ProcessDefinition process) {
        processDefinitionMap.put(processId, process);
    }
    @Override
    public boolean containsProcessDefinition(long processId) {
        return processDefinitionMap.containsKey(processId);
    }
    @Override
    public Collection<ProcessDefinition> loadAllProcessDefinitions() {
        return processDefinitionMap.values();
    }
    @Override
    public void removeProcessDefinition(long processId) {
        processDefinitionMap.remove(processId);
    }

    @Override
    public ProcessMapContext getContext(long processInstanceId) {
        return contextMap.get(processInstanceId);
    }
    @Override
    public void putContext(long processInstanceId, ProcessMapContext context) {
        contextMap.put(processInstanceId, context);
    }
    @Override
    public void removeContext(long processInstanceId) {
        contextMap.remove(processInstanceId);
    }
    @Override
    public boolean containsContext(long processInstanceId) {
        return contextMap.containsKey(processInstanceId);
    }
}
