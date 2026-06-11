package com.ontoweb.newworkflow.core.process.listener;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午11:09
 * @description：流程监听器
 * @modified By：
 * @version: $
 */
public interface ProcessListener {
    void processStart(ProcessInstance processInstance, Context context);
    void processEnd(ProcessInstance processInstance,Context context);
}
