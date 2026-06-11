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
@DiscriminatorValue("Double")
@Data
public class DoubleVariable extends Variable{
    @Column(name="DOUBLE_VALUE_")
    private double doubleValue;

    public DoubleVariable(){}
    public DoubleVariable(double value){
        this.doubleValue=value;
    }

    @Override
    public Object getValue() {
        return this.doubleValue;
    }
    @Override
    public VariableType getType() {
        return VariableType.Double;
    }
}
