package com.ontoweb.newworkflow.core.utils;

import com.ontoweb.newworkflow.core.env.EnvironmentProvider;
import com.ontoweb.newworkflow.core.service.CacheService;
import com.ontoweb.newworkflow.core.service.DefaultMemoryCacheService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collection;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午4:51
 * @description：环境设置
 * @modified By：
 * @version: $
 */
@Slf4j
@Component
public class EnvironmentUtils implements ApplicationContextAware {
    @Autowired
    private EnvironmentProvider provider;

    private static EnvironmentUtils environment;
    private ApplicationContext applicationContext;
    private CacheService cacheService;
    public static EnvironmentUtils getEnvironment(){
        return environment;
    }
//    public SessionFactory getSessionFactory(){
//        return provider.getSessionFactory();
//    }

    public PlatformTransactionManager getPlatformTransactionManager(){
        return provider.getPlatformTransactionManager();
    }

    public CacheService getCache(){
        return cacheService;
    }

    public String getLoginUser(){
        return provider.getLoginUser();
    }

    public String getCategoryId(){
        return provider.getCategoryId();
    }

    public EnvironmentProvider getEnvironmentProvider(){
        return provider;
    }
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
        Collection<EnvironmentProvider> providers=applicationContext.getBeansOfType(EnvironmentProvider.class).values();
        log.info("providers:{}" + providers);

        if(providers.size()==0){
            throw new RuntimeException("You must be implementation "+EnvironmentProvider.class.getName()+" interface when use uflo!");
        }
        provider=providers.iterator().next();
        Collection<CacheService> cacheServices=applicationContext.getBeansOfType(CacheService.class).values();
        if(cacheServices.size()>0){
            cacheService=cacheServices.iterator().next();
        }else{
            cacheService=new DefaultMemoryCacheService();
        }
        environment=this;
        new Splash().print();
    }
}
