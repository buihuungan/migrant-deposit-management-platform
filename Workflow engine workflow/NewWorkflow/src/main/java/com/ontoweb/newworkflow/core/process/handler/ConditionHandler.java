package com.ontoweb.newworkflow.core.process.handler;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.flow.SequenceFlow;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午9:48
 * @description：
 * @modified By：
 * @version: $
 */
public interface ConditionHandler {
    boolean handle(Context context, ProcessInstance processInstance, SequenceFlow flow);
}
