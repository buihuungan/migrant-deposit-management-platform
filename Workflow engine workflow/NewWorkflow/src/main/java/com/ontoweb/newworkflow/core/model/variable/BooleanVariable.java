package com.ontoweb.newworkflow.core.model.variable;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午4:44
 * @description：
 * @modified By：
 * @version: $
 */
@Entity
@DiscriminatorValue("Boolean")
@Data
public class BooleanVariable extends Variable{
    @Column(name="BOOLEAN_VALUE_")
    private boolean booleanValue;

    public BooleanVariable(){}

    public BooleanVariable(boolean value){
        this.booleanValue=value;
    }
    @Override
    public Object getValue() {
        return booleanValue;
    }

    @Override
    public VariableType getType() {
        return VariableType.Boolean;
    }
}
