package com.ontoweb.newworkflow.core.service;

import com.ontoweb.newworkflow.core.model.calendar.CalendarDate;
import com.ontoweb.newworkflow.core.model.calendar.CalendarDef;
import org.quartz.Calendar;

import java.util.Collection;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:46
 * @description：
 * @modified By：
 * @version: $
 */
public interface CalendarService {
    public static final String BEAN_ID="uflo.calendarService";
    Collection<CalendarDef> getAllCalendarDefs();
    CalendarDef getCalendarDef(long calendarId);
    Collection<CalendarDate> getCalendarDate(long calendarId);
    Calendar getCalendar(long calendarId);
}
