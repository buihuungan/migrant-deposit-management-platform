package com.ontoweb.newworkflow.core.process.node.calendar;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:06
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class BusinessCalendar {
    private int businessDayHours;

    public Date calEndDate(Calendar startDate, Calendar endDate, org.quartz.Calendar baseCalendar){
        int holidayMinutes=calHolidayMinutes(startDate,endDate,baseCalendar);
        if(holidayMinutes==0){
            return endDate.getTime();
        }
        startDate.setTime(endDate.getTime());
        endDate.add(Calendar.MINUTE, holidayMinutes);
        return calEndDate(startDate,endDate,baseCalendar);
    }

    private int calHolidayMinutes(Calendar startDate,Calendar endDate,org.quartz.Calendar baseCalendar) {
        if(startDate.getTimeInMillis()==endDate.getTimeInMillis()){
            return 0;
        }
        int count=0;
        while(startDate.getTimeInMillis()<endDate.getTimeInMillis()){
            if(!baseCalendar.isTimeIncluded(startDate.getTime().getTime())){
                count++;
            }
            startDate.add(Calendar.MINUTE, 1);
        }
        return count;
    }

    public int getBusinessDayHours() {
        return businessDayHours;
    }

    public void setBusinessDayHours(int businessDayHours) {
        this.businessDayHours = businessDayHours;
    }
}
