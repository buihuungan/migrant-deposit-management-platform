package com.ontoweb.newworkflow.core.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:07
 * @description：上下文属性
 * @modified By：
 * @version: $1.0
 */
@Entity
@Table(name="UFLO_CONTEXT_PROPERTY")
@Data
public class ContextProperty {
    @Id
    @Column(name="KEY_",length=120)
    private String key;
    @Column(name="VALUE_",length=35)
    private String value;
}
