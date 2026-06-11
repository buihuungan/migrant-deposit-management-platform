package com.ontoweb.newworkflow.core.process.handler;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:07
 * @description：会签参与者
 * @modified By：
 * @version: $
 */
public interface CountersignHandler {
    boolean handle(Context context, ProcessInstance processInstance);
}
