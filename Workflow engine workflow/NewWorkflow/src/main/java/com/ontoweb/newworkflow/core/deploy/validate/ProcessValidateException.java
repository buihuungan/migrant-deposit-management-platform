package com.ontoweb.newworkflow.core.deploy.validate;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:38
 * @description：流程验证异常
 * @modified By：
 * @version: $
 */
public class ProcessValidateException extends RuntimeException{
    private static final long serialVersionUID = -8700678017516761037L;
    private String message;
    public ProcessValidateException(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
