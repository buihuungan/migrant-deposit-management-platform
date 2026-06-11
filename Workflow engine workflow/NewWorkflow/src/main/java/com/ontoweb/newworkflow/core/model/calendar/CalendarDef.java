package com.ontoweb.newworkflow.core.model.calendar;

import lombok.Data;


import javax.persistence.*;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/26 下午3:41
 * @description：日历描述部分
 * @modified By：
 * @version: 1.0$
 */
@Entity
@Table(name="UFLO_CALENDAR")
@Data
public class CalendarDef implements java.io.Serializable {
    private static final long serialVersionUID = 2579525564734297159L;
    @Id
    @Column(name="ID_")
    private long id;
    @Column(name="CATEGORY_ID_",length=60)
    private String categoryId;
    @Column(name="NAME_",length=60)
    private String name;
    @Column(name="DESC_",length=120)
    private String desc;
    @Column(name="TYPE_",length=12)
    @Enumerated(EnumType.STRING)
    private CalendarType type;

    @Transient
    private List<CalendarDate> calendarDates;
}
