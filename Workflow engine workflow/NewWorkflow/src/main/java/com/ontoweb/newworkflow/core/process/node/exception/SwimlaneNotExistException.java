package com.ontoweb.newworkflow.core.process.node.exception;

import com.ontoweb.newworkflow.core.process.node.TaskNode;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:01
 * @description：
 * @modified By：
 * @version: $
 */
public class SwimlaneNotExistException extends AssignException{
    private static final long serialVersionUID = 5900914289050449146L;
    public SwimlaneNotExistException(String message, TaskNode taskNode) {
        super(message, taskNode);
    }
}
