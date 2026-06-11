package com.ontoweb.newworkflow.core.service;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:33
 * @description：任务意见对象
 * @modified By：
 * @version: $
 */
public class TaskOpinion {
    private String opinion;
    public TaskOpinion(String opinion){
        this.opinion=opinion;
    }
    public String getOpinion() {
        if(opinion==null){
            return opinion;
        }
        if(opinion.length()>250){
            opinion=opinion.substring(0,250);
        }
        return opinion;
    }
}
