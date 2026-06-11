package com.ontoweb.newworkflow.core.command.impl.jump;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午10:37
 * @description：跳转节点
 * @modified By：
 * @version: $
 */
@Data
public class JumpNode {
    private List<String> parent=new LinkedList<String>();
    private int level;
    private String name;
    private String label;
    private boolean isTask;
    public JumpNode(){}
    public JumpNode(String name) {
        this.name = name;
    }

    public boolean isTask() {
        return isTask;
    }

    public void setTask(boolean isTask) {
        this.isTask = isTask;
    }
    public void addParent(String name){
        this.parent.add(name);
    }

    public void decreaseParent(){
        int size=parent.size();
        if(size>0){
            this.parent.remove((parent.size()-1));
        }
    }
}
