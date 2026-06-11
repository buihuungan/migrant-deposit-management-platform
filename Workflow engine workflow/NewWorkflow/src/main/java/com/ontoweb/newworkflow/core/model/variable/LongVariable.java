package com.ontoweb.newworkflow.core.model.variable;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午4:48
 * @description：
 * @modified By：
 * @version: $
 */
@Entity
@DiscriminatorValue("Long")
@Data
public class LongVariable extends Variable{
    @Column(name="LONG_VALUE_")
    private long longValue;
    public LongVariable(){}
    public LongVariable(long value){
        this.longValue=value;
    }

    @Override
    public Object getValue() {
        return this.longValue;
    }
    @Override
    public VariableType getType() {
        return VariableType.Long;
    }
}
