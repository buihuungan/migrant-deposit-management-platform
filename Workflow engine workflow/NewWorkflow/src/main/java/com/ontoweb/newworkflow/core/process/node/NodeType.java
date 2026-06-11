package com.ontoweb.newworkflow.core.process.node;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午11:13
 * @description：
 * @modified By：
 * @version: $
 */
public enum NodeType {
    Action,Task,CountersignTask,Fork,Join,Foreach,Start,End,TerminalEnd,Decision,Subprocess;
}
