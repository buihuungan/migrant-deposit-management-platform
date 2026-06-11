package com.ontoweb.newworkflow.core.process.node.exception;

import com.ontoweb.newworkflow.core.process.node.TaskNode;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:02
 * @description：
 * @modified By：
 * @version: $
 */
public class AssignExprNullException extends AssignException{
    private static final long serialVersionUID = -8859142296441418288L;
    public AssignExprNullException(String message, TaskNode taskNode) {
        super(message, taskNode);
    }
}
