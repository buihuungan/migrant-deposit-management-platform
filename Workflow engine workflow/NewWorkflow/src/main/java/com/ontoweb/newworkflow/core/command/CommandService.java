package com.ontoweb.newworkflow.core.command;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:25
 * @description：
 * @modified By：
 * @version: $
 */
public interface CommandService {
    <T> T executeCommand(Command<T> command);

    <T> T executeCommandInNewTransaction(Command<T> command);
}
