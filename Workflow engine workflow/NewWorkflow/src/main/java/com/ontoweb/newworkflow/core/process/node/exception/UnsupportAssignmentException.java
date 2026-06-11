package com.ontoweb.newworkflow.core.process.node.exception;

import com.ontoweb.newworkflow.core.process.node.TaskNode;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午9:58
 * @description：
 * @modified By：
 * @version: $
 */
public class UnsupportAssignmentException extends AssignException{
    private static final long serialVersionUID = -6352144892097414902L;
    public UnsupportAssignmentException(String message, TaskNode taskNode) {
        super(message, taskNode);
    }

}
