package com.ontoweb.newworkflow.core.process.handler;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午3:25
 * @description：流程事件处理器
 * @modified By：
 * @version: $
 */
public interface ProcessEventHandler {
    /**
     * 流程实例开始后触发的方法
     * @param processInstance 流程实例
     * @param context 流程实例上下文
     */
    void start(ProcessInstance processInstance, Context context);

    /**
     * 流程实例结束后触发的方法
     * @param processInstance 流程实例
     * @param context 流程实例上下文
     */
    void end(ProcessInstance processInstance,Context context);
}
