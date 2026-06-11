package com.ontoweb.newworkflow.core.process.handler;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.node.Node;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午11:18
 * @description：节点事件处理器
 * @modified By：
 * @version: $
 */
public interface NodeEventHandler {
    /**
     * 进入节点后触发的方法
     * @param node 当前节点对象
     * @param processInstance 当前流程实例对象
     * @param context 流程上下文
     */
    void enter(Node node, ProcessInstance processInstance, Context context);

    /**
     * 离开节点后触发的方法
     * @param node 当前节点对象
     * @param processInstance 当前流程实例对象
     * @param context 流程上下文
     */
    void leave(Node node,ProcessInstance processInstance,Context context);
}
