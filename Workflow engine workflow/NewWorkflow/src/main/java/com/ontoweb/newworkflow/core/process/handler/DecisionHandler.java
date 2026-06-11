package com.ontoweb.newworkflow.core.process.handler;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午8:46
 * @description：
 * @modified By：
 * @version: $
 */
public interface DecisionHandler {
    String handle(Context context, ProcessInstance processInstance);
}
