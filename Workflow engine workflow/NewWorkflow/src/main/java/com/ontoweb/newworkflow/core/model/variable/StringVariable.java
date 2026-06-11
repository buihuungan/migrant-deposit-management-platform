package com.ontoweb.newworkflow.core.model.variable;

import lombok.Data;

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
@DiscriminatorValue("String")
@Data
public class StringVariable extends Variable{
    @Column(name="STRING_VALUE_")
    private String stringValue;
    public StringVariable(){}
    public StringVariable(String value){
        this.stringValue=value;
    }


    @Override
    public Object getValue() {
        return this.stringValue;
    }
    @Override
    public VariableType getType() {
        return VariableType.String;
    }
}
