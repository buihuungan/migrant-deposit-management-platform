package com.ontoweb.newworkflow.core.process.node.reminder;

import org.quartz.Calendar;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:44
 * @description：
 * @modified By：
 * @version: $
 */
public interface CalendarProvider {
    Calendar getCalendar(String calendarId);
    List<CalendarInfo> getCalendarInfos();
}
