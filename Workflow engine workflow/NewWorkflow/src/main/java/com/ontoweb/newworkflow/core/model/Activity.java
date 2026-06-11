package com.ontoweb.newworkflow.core.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/26 下午2:55
 * @description：活动的基础模型类
 * @modified By：
 * @version: 1.0$
 */
@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class Activity {
    @Id
    @Column(name="ID_")
    private long id;

    @Column(name="NODE_NAME_",length=60)
    private String nodeName;

    @Column(name="DESCRIPTION_",length=120)
    private String description;

    @Column(name="PROCESS_ID_")
    private long processId;
}
