package com.ontoweb.newworkflow.core.process.node.reminder;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午2:27
 * @description：日历信息
 * @modified By：
 * @version: $
 */
@Data
public class CalendarInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
}
