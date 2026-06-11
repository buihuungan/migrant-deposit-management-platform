package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.calendar.CalendarDef;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:54
 * @description：获得所有日历描述信息
 * @modified By：
 * @version: $
 */
public class GetAllCalendarDefCommand  implements Command<Collection<CalendarDef>> {
    @SuppressWarnings("unchecked")
    @Override
    public Collection<CalendarDef> execute(Context context) {
        Criteria criteria=context.getSession().createCriteria(CalendarDef.class);
        String categoryId= EnvironmentUtils.getEnvironment().getCategoryId();
        if(StringUtils.isNotEmpty(categoryId)){
            criteria.add(Restrictions.eq("categoryId", categoryId));
        }
        return criteria.list();
    }
}
