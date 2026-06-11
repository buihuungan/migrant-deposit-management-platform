package com.ontoweb.newworkflow.handler.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/5 下午3:09
 * @description：获取 ApplicationContext工具类，解决抽象类中无法注入bean的问题
 * @modified By：
 * @version: 1.0$
 */
@Component
public class SpringBootBeanUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBootBeanUtil.applicationContext == null) {
            SpringBootBeanUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }


    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }


    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
