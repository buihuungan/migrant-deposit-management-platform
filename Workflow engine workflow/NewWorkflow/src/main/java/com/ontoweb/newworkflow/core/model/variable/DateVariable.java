package com.ontoweb.newworkflow.core.model.variable;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午4:46
 * @description：
 * @modified By：
 * @version: $
 */
@Entity
@DiscriminatorValue("Date")
@Data
public class DateVariable extends Variable{
    @Column(name="DATE_VALUE_")
    private Date dateValue;
    public DateVariable(){
    }
    public DateVariable(Date value){
        this.dateValue=value;
    }

    @Override
    public Object getValue() {
        return this.dateValue;
    }
    @Override
    public VariableType getType() {
        return VariableType.Date;
    }
}
