package com.ontoweb.newworkflow.core.process.node.reminder;

import com.ontoweb.newworkflow.core.model.task.DateUnit;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:05
 * @description：
 * @modified By：
 * @version: $
 */
public class PeriodReminder extends Reminder{
    private static final long serialVersionUID = 1L;
    private int repeat;
    private DateUnit unit;
    private List<CalendarInfo> calendarInfos;
    public int getRepeat() {
        return repeat;
    }
    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
    public DateUnit getUnit() {
        return unit;
    }
    public void setUnit(DateUnit unit) {
        this.unit = unit;
    }
    public List<CalendarInfo> getCalendarInfos() {
        return calendarInfos;
    }
    public void setCalendarInfos(
            List<CalendarInfo> calendarInfos) {
        this.calendarInfos = calendarInfos;
    }
}
