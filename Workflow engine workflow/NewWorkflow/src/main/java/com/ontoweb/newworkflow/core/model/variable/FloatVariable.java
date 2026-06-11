package com.ontoweb.newworkflow.core.model.variable;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午4:47
 * @description：
 * @modified By：
 * @version: $
 */
@Entity
@DiscriminatorValue("Float")
@Data
public class FloatVariable extends Variable{
    @Column(name="FLOAT_VALUE_")
    private float floatValue;

    public FloatVariable(){}

    public FloatVariable(float value){
        this.floatValue=value;
    }

    @Override
    public Object getValue() {
        return floatValue;
    }

    @Override
    public VariableType getType() {
        return VariableType.Float;
    }
}
