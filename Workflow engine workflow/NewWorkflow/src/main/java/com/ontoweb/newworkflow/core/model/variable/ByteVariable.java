package com.ontoweb.newworkflow.core.model.variable;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午4:45
 * @description：
 * @modified By：
 * @version: $
 */
@Entity
@DiscriminatorValue("Byte")
@Data
public class ByteVariable extends Variable{
    @Column(name="BYTE_VALUE_")
    private Byte byteValue;

    public ByteVariable(){}

    public ByteVariable(byte value){
        this.byteValue=value;
    }
    @Override
    public Object getValue() {
        return byteValue;
    }

    @Override
    public VariableType getType() {
        return VariableType.Byte;
    }
}
