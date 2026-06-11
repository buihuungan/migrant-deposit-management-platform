package com.ontoweb.newworkflow.core.process.handler;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.node.TaskNode;

import java.util.Collection;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:00
 * @description：
 * @modified By：
 * @version: $
 */
public interface AssignmentHandler {
    Collection<String> handle(TaskNode taskNode, ProcessInstance processInstance, Context context);
}
