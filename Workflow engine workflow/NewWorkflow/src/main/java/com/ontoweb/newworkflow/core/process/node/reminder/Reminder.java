package com.ontoweb.newworkflow.core.process.node.reminder;

import java.io.Serializable;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午3:15
 * @description：
 * @modified By：
 * @version: $
 */
public class Reminder implements Serializable {
    private static final long serialVersionUID = 1L;
    private String handlerBean;

    public String getHandlerBean() {
        return handlerBean;
    }

    public void setHandlerBean(String handlerBean) {
        this.handlerBean = handlerBean;
    }
}
