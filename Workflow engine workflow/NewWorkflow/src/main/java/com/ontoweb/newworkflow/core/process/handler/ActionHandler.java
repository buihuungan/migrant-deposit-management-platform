package com.ontoweb.newworkflow.core.process.handler;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:06
 * @description：
 * @modified By：
 * @version: $
 */
public interface ActionHandler {
    void handle(ProcessInstance processInstance, Context context);
}
