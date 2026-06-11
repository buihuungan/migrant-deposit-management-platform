package com.ontoweb.newworkflow.core.model.task;

import com.ontoweb.newworkflow.core.env.Context;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:20
 * @description： 取消任务拦截器
 * @modified By：
 * @version: $
 */
public interface CancelTaskInterceptor {
    void intercept(Context context, Task task);
}
