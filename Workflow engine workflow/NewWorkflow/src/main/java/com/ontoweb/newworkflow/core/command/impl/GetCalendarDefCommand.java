package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.calendar.CalendarDef;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:56
 * @description：获取日历描述信息
 * @modified By：
 * @version: $
 */
public class GetCalendarDefCommand implements Command<CalendarDef> {
    private long calendarId;
    public GetCalendarDefCommand(long calendarId){
        this.calendarId=calendarId;
    }
    public CalendarDef execute(Context context) {
        Criteria criteria=context.getSession().createCriteria(CalendarDef.class);
        criteria.add(Restrictions.eq("id", calendarId));
        CalendarDef def=(CalendarDef)criteria.uniqueResult();
        return def;
    }
}
