package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.calendar.CalendarDate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:56
 * @description：获取日历时间
 * @modified By：
 * @version: $
 */
public class GetCalendarDateCommand implements Command<List<CalendarDate>> {
    private long calendarId;
    public GetCalendarDateCommand(long calendarId){
        this.calendarId=calendarId;
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<CalendarDate> execute(Context context) {
        Criteria dateCriteria=context.getSession().createCriteria(CalendarDate.class);
        dateCriteria.add(Restrictions.eq("calendarId", calendarId));
        return dateCriteria.list();
    }
}
