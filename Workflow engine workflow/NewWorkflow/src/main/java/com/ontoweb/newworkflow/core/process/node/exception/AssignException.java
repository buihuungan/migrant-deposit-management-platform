package com.ontoweb.newworkflow.core.process.node.exception;

import com.ontoweb.newworkflow.core.process.node.TaskNode;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午9:59
 * @description：
 * @modified By：
 * @version: $
 */
public class AssignException extends RuntimeException{
    private TaskNode taskNode;
    private static final long serialVersionUID = -7276567238830869572L;
    public AssignException(String message,TaskNode taskNode) {
        super(message);
        this.taskNode = taskNode;
    }
    public TaskNode getTaskNode() {
        return taskNode;
    }
}
