package com.ontoweb.newworkflow.core.model.variable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午4:49
 * @description：
 * @modified By：
 * @version: $
 */
@Entity
@DiscriminatorValue("Short")
public class ShortVariable extends Variable{
    @Column(name="SHORT_VALUE_")
    private Short shortValue;

    public ShortVariable(){}

    public ShortVariable(short value){
        this.shortValue=value;
    }
    @Override
    public Object getValue() {
        return shortValue;
    }

    @Override
    public VariableType getType() {
        return VariableType.Short;
    }
}
