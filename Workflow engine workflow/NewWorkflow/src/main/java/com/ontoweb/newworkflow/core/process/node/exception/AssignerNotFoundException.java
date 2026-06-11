package com.ontoweb.newworkflow.core.process.node.exception;

import com.ontoweb.newworkflow.core.process.node.TaskNode;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:03
 * @description：
 * @modified By：
 * @version: $
 */
public class AssignerNotFoundException extends AssignException{
    private static final long serialVersionUID = 7652631201766616995L;
    public AssignerNotFoundException(String message, TaskNode taskNode) {
        super(message, taskNode);
    }
}
