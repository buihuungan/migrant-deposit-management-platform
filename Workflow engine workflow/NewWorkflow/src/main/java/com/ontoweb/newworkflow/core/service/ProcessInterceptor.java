package com.ontoweb.newworkflow.core.service;

import com.ontoweb.newworkflow.core.model.ProcessDefinition;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午9:59
 * @description：
 * @modified By：
 * @version: $
 */
public interface ProcessInterceptor {
    void updateProcess(ProcessDefinition process);
    void deleteProcess(ProcessDefinition process);
}
