package com.ontoweb.newworkflow.core.model.task;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:23
 * @description：任务状态
 * @modified By：
 * @version: $
 */
public enum TaskState {
    Created,Ready,Reserved,InProgress,Completed,Suspended,Canceled,Forwarded,Rollback,Withdraw;
}
