package com.ontoweb.newworkflow.core.env;

import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.process.node.reminder.DueDefinition;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:04
 * @description：
 * @modified By：
 * @version: $
 */
public interface TaskDueDefinitionProvider {
    DueDefinition getDueDefinition(Task task, ProcessInstance processInstance);
}
