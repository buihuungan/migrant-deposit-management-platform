package com.ontoweb.newworkflow.handler.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/3 上午10:50
 * @description：
 * @modified By：
 * @version: $
 */
public class RequestHolder {
    private static final ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();

    public static void setThreadLocal(HttpServletRequest request) {
        threadLocal.set(request);
    }

    public static HttpServletRequest getThreadLocal() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
