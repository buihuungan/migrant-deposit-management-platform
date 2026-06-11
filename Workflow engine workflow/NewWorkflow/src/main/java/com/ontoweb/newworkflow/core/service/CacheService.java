package com.ontoweb.newworkflow.core.service;

import com.ontoweb.newworkflow.core.expr.impl.ProcessMapContext;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;

import java.util.Collection;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午3:37
 * @description：缓存的接口
 * @modified By：
 * @version: $
 */
public interface CacheService {
    ProcessDefinition getProcessDefinition(long processId);
    void putProcessDefinition(long processId,ProcessDefinition process);
    boolean containsProcessDefinition(long processId);
    Collection<ProcessDefinition> loadAllProcessDefinitions();
    void removeProcessDefinition(long processId);
    ProcessMapContext getContext(long processInstanceId);
    void putContext(long processInstanceId,ProcessMapContext context);
    void removeContext(long processInstanceId);
    boolean containsContext(long processInstanceId);
}
