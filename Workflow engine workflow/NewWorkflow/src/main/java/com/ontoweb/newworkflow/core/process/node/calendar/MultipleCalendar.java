package com.ontoweb.newworkflow.core.process.node.calendar;

import org.quartz.impl.calendar.BaseCalendar;
import org.quartz.impl.calendar.DailyCalendar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:41
 * @description：
 * @modified By：
 * @version: $
 */
public class MultipleCalendar extends BaseCalendar {
    private static final long serialVersionUID = -4142756339211533719L;
    private List<WorkdayCalendar> workdayCalendars=new ArrayList<WorkdayCalendar>();
    private List<DailyCalendar> dailyCalendars=new ArrayList<DailyCalendar>();
    private List<BaseCalendar> calendars=new ArrayList<BaseCalendar>();
    public void addCalendar(BaseCalendar calendar){
        if(calendar instanceof WorkdayCalendar){
            workdayCalendars.add((WorkdayCalendar)calendar);
        }else if(calendar instanceof DailyCalendar){
            dailyCalendars.add((DailyCalendar)calendar);
        }else{
            calendars.add(calendar);
        }
    }
    @Override
    public boolean isTimeIncluded(long timeStamp) {
        for(WorkdayCalendar calendar:workdayCalendars){
            if(calendar.isTimeIncluded(timeStamp)){
                for(DailyCalendar dc:dailyCalendars){
                    if(dc.isTimeIncluded(timeStamp)==false){
                        return false;
                    }
                }
                return true;
            }
        }
        for(BaseCalendar calendar:calendars){
            if(calendar.isTimeIncluded(timeStamp)==false){
                return false;
            }
        }
        for(DailyCalendar calendar:dailyCalendars){
            if(calendar.isTimeIncluded(timeStamp)==false){
                return false;
            }
        }
        return true;
    }
    @Override
    public long getNextIncludedTime(long timeStamp) {
        java.util.Calendar day=null;
        for(BaseCalendar calendar:calendars){
            timeStamp = calendar.getNextIncludedTime(timeStamp);
            day = getStartOfDayJavaCalendar(timeStamp);
            while (isTimeIncluded(day.getTime().getTime()) == false) {
                day.add(java.util.Calendar.DATE, 1);
            }
        }
        for(DailyCalendar calendar:dailyCalendars){
            timeStamp = calendar.getNextIncludedTime(timeStamp);
            day = getStartOfDayJavaCalendar(timeStamp);
            while (isTimeIncluded(day.getTime().getTime()) == false) {
                day.add(java.util.Calendar.DATE, 1);
            }
        }
        return day.getTime().getTime();
    }
}
