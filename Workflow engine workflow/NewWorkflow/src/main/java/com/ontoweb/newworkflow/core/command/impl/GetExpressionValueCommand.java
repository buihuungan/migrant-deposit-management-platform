package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:57
 * @description：获取表达式的值
 * @modified By：
 * @version: $
 */
public class GetExpressionValueCommand implements Command<Object> {
    private long processInstanceId;
    private String key;
    public GetExpressionValueCommand(long processInstanceId,String key){
        this.processInstanceId=processInstanceId;
        this.key=key;
    }
    public Object execute(Context context) {
        return context.getExpressionContext().eval(processInstanceId, "${"+key+"}");
    }
}
