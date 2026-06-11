package com.ontoweb.newworkflow.core.model.variable;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午4:46
 * @description：
 * @modified By：
 * @version: $
 */
@Entity
@DiscriminatorValue("Character")
@Data
public class CharacterVariable extends Variable{
    @Column(name="CHARACTER_VALUE_")
    private Character characterValue;

    public CharacterVariable(){}

    public CharacterVariable(Character value){
        this.characterValue=value;
    }
    @Override
    public Object getValue() {
        return characterValue;
    }

    @Override
    public VariableType getType() {
        return VariableType.Character;
    }
}
