package com.ontoweb.newworkflow.core.utils;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.listener.ProcessListener;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午3:24
 * @description：流程工具类
 * @modified By：
 * @version: $
 */
public class ProcessListenerUtils {
    public static void fireProcessStartListers(ProcessInstance processInstance, Context context){
        fireProcessListers(processInstance,context,true);
    }
    public static void fireProcessEndListers(ProcessInstance processInstance,Context context){
        fireProcessListers(processInstance,context,false);
    }
    private static void fireProcessListers(ProcessInstance processInstance,Context context,boolean isStart){
        for(ProcessListener listener:context.getApplicationContext().getBeansOfType(ProcessListener.class).values()){
            if(isStart){
                listener.processStart(processInstance, context);
            }else{
                listener.processEnd(processInstance, context);
            }
        }
    }
}
