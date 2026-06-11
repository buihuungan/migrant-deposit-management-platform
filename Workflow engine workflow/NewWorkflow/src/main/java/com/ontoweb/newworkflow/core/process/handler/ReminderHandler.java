package com.ontoweb.newworkflow.core.process.handler;

import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:40
 * @description：
 * @modified By：
 * @version: $
 */
public interface ReminderHandler {
    void execute(ProcessInstance processInstance, Task task);
}
