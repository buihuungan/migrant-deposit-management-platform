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
@DiscriminatorValue("Integer")
@Data
public class IntegerVariable extends Variable{
    @Column(name="INTEGER_VALUE_")
    private int integerValue;

    public IntegerVariable(){}

    public IntegerVariable(int value){
        this.integerValue=value;
    }
    @Override
    public Object getValue() {
        return integerValue;
    }

    @Override
    public VariableType getType() {
        return VariableType.Integer;
    }
}
