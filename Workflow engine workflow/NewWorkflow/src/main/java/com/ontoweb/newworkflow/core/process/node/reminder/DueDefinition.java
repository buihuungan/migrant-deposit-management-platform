package com.ontoweb.newworkflow.core.process.node.reminder;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午3:14
 * @description：
 * @modified By：
 * @version: $
 */
public class DueDefinition implements Serializable {
    private static final long serialVersionUID = 1L;
    private int day;
    private int hour;
    private int minute;
    private int businessDayHours;
    private List<CalendarInfo> calendarInfos;
    private Reminder reminder;
    private DueAction dueAction;
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public List<CalendarInfo> getCalendarInfos() {
        return calendarInfos;
    }
    public void setCalendarInfos(List<CalendarInfo> calendarInfos) {
        this.calendarInfos = calendarInfos;
    }
    public Reminder getReminder() {
        return reminder;
    }
    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }
    public DueAction getDueAction() {
        return dueAction;
    }
    public void setDueAction(DueAction dueAction) {
        this.dueAction = dueAction;
    }
    public int getBusinessDayHours() {
        return businessDayHours;
    }
    public void setBusinessDayHours(int businessDayHours) {
        this.businessDayHours = businessDayHours;
    }
}
