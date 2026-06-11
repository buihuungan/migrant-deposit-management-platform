package com.ontoweb.newworkflow.core.service.impl;

import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.command.impl.GetAllCalendarDefCommand;
import com.ontoweb.newworkflow.core.command.impl.GetCalendarDateCommand;
import com.ontoweb.newworkflow.core.command.impl.GetCalendarDefCommand;
import com.ontoweb.newworkflow.core.model.calendar.CalendarDate;
import com.ontoweb.newworkflow.core.model.calendar.CalendarDef;
import com.ontoweb.newworkflow.core.model.calendar.CalendarType;
import com.ontoweb.newworkflow.core.process.node.calendar.WorkdayCalendar;
import com.ontoweb.newworkflow.core.service.CalendarService;
import org.quartz.Calendar;
import org.quartz.impl.calendar.*;

import java.util.Collection;
import java.util.GregorianCalendar;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午10:00
 * @description：
 * @modified By：
 * @version: $
 */
public class CalendarServiceImpl implements CalendarService {

    private CommandService commandService;

    @Override
    public Collection<CalendarDef> getAllCalendarDefs() {
        return commandService.executeCommand(new GetAllCalendarDefCommand());
    }

    @Override
    public CalendarDef getCalendarDef(long calendarId) {
        return commandService.executeCommand(new GetCalendarDefCommand(calendarId));
    }

    @Override
    public Collection<CalendarDate> getCalendarDate(long calendarId) {
        return commandService.executeCommand(new GetCalendarDateCommand(calendarId));
    }

    @Override
    public Calendar getCalendar(long calendarId) {
        CalendarDef def=getCalendarDef(calendarId);
        Collection<CalendarDate> dates=getCalendarDate(calendarId);
        Calendar baseCalendar=buildCalendar(def,dates);
        return baseCalendar;
    }

    private Calendar buildCalendar(CalendarDef calendarDef,Collection<CalendarDate> dates){
        CalendarType type=calendarDef.getType();
        if(type.equals(CalendarType.holiday)){
            HolidayCalendar calendar=new HolidayCalendar();
            if(dates!=null){
                for(CalendarDate d:dates){
                    calendar.addExcludedDate(d.getCalendarDate());
                }
            }
            return calendar;
        }else if(type.equals(CalendarType.workday)){
            WorkdayCalendar calendar=new WorkdayCalendar();
            if(dates!=null){
                for(CalendarDate d:dates){
                    calendar.addIncludedDate(d.getCalendarDate());
                }
            }
            return calendar;
        }else if(type.equals(CalendarType.annual)){
            AnnualCalendar calendar=new AnnualCalendar();
            if(dates!=null){
                for(CalendarDate d:dates){
                    java.util.Calendar c=new GregorianCalendar();
                    c.set(java.util.Calendar.MONTH,d.getMonthOfYear());
                    c.set(java.util.Calendar.DAY_OF_MONTH,d.getDayOfMonth());
                    calendar.setDayExcluded(c,true);
                }
            }
            return calendar;
        }else if(type.equals(CalendarType.monthly)){
            MonthlyCalendar calendar=new MonthlyCalendar();
            if(dates!=null){
                for(CalendarDate d:dates){
                    calendar.setDayExcluded(d.getDayOfMonth(),true);
                }
            }
            return calendar;
        }else if(type.equals(CalendarType.weekly)){
            WeeklyCalendar calendar=new WeeklyCalendar();
            calendar.setDayExcluded(java.util.Calendar.SUNDAY, false);
            calendar.setDayExcluded(java.util.Calendar.SATURDAY, false);
            if(dates!=null){
                for(CalendarDate d:dates){
                    calendar.setDayExcluded(d.getDayOfWeek(),true);
                }
            }
            return calendar;
        }else if(type.equals(CalendarType.daily)){
            if(dates!=null){
                DailyCalendar calendar=null;
                for(CalendarDate d:dates){
                    calendar=new DailyCalendar(calendar,d.getRangeStartTime(),d.getRangeEndTime());
                }
                return calendar;
            }
        }
        return null;
    }

    public CommandService getCommandService() {
        return commandService;
    }

    public void setCommandService(CommandService commandService) {
        this.commandService = commandService;
    }
}
