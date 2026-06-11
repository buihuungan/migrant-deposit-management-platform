package com.ontoweb.newworkflow.core.process.node.reminder.impl;

import com.ontoweb.newworkflow.core.model.calendar.CalendarDef;
import com.ontoweb.newworkflow.core.process.node.reminder.CalendarInfo;
import com.ontoweb.newworkflow.core.process.node.reminder.CalendarProvider;
import com.ontoweb.newworkflow.core.service.CalendarService;
import org.quartz.Calendar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:45
 * @description：
 * @modified By：
 * @version: $
 */
public class UfloCalendarProvider implements CalendarProvider {
    private CalendarService calendarService;

    @Override
    public Calendar getCalendar(String calendarId) {
        return calendarService.getCalendar(Long.valueOf(calendarId));
    }

    @Override
    public List<CalendarInfo> getCalendarInfos() {
        Collection<CalendarDef> defs=calendarService.getAllCalendarDefs();
        List<CalendarInfo> list=new ArrayList<CalendarInfo>();
        for(CalendarDef def:defs){
            CalendarInfo info=new CalendarInfo();
            info.setId(String.valueOf(def.getId()));
            info.setName(def.getName());
            list.add(info);
        }
        return list;
    }

    public CalendarService getCalendarService() {
        return calendarService;
    }

    public void setCalendarService(CalendarService calendarService) {
        this.calendarService = calendarService;
    }
}
