package com.ontoweb.newworkflow.core.process.handler;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;

import java.util.Collection;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午4:06
 * @description：
 * @modified By：
 * @version: $
 */
public interface ForeachHandler {
    Collection<Object> handle(ProcessInstance processInstance, Context context);
}
