package com.ontoweb.newworkflow.core.model.calendar;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/26 下午3:39
 * @description：日历部分
 * @modified By：
 * @version: $1.0
 */
@Entity
@Table(name="UFLO_CALENDAR_DATE")
@Data
public class CalendarDate implements java.io.Serializable {
    private static final long serialVersionUID = -9078588793964776579L;
    @Id
    @Column(name="ID_")
    private long id;
    @Column(name="NAME_",length=60)
    private String name;
    @Column(name="CALENDAR_ID_")
    private long calendarId;

    @Column(name="RANGE_START_TIME_",length=60)
    private String rangeStartTime;

    @Column(name="RANGE_END_TIME_",length=60)
    private String rangeEndTime;

    @Column(name="CALENDAR_DATE_")
    private Date calendarDate;
    @Column(name="DAY_OF_MONTH_")
    private int dayOfMonth;
    @Column(name="MONTH_OF_YEAR_")
    private int monthOfYear;
    @Column(name="DAY_OF_WEEK_")
    private int dayOfWeek;
}
