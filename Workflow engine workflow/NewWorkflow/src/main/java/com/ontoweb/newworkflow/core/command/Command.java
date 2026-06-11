package com.ontoweb.newworkflow.core.command;

import com.ontoweb.newworkflow.core.env.Context;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:23
 * @description：执行
 * @modified By：
 * @version: $
 */
public interface Command<T> {
    T execute(Context context);
}
