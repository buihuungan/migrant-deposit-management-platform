package com.ontoweb.newworkflow.core.utils;

import java.math.BigDecimal;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午8:35
 * @description：
 * @modified By：
 * @version: $
 */
public class Utils {
    public static BigDecimal toBigDecimal(Object obj){
        if(obj==null){
            return null;
        }
        if(obj instanceof BigDecimal){
            return (BigDecimal)obj;
        }else if(obj instanceof String){
            if(obj.toString().trim().equals("")){
                return new BigDecimal(0);
            }
            try{
                String str=obj.toString().trim();
                return new BigDecimal(str);
            }catch(Exception ex){
                throw new RuntimeException("Can not convert "+obj+" to BigDecimal.");
            }
        }else if(obj instanceof Number){
            Number n=(Number)obj;
            return new BigDecimal(n.doubleValue());
        }
        throw new RuntimeException("Can not convert "+obj+" to BigDecimal.");
    }
}
