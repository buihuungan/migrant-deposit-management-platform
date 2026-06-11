package com.ontoweb.newworkflow.core.env;

import org.hibernate.SessionFactory;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午3:36
 * @description：提供环境变量信息
 * @modified By：
 * @version: $
 */
public interface EnvironmentProvider {
    /**
     * @return 返回流程引擎需要使用的Hibernate SessionFactory
     */
//    SessionFactory getSessionFactory();
    /**
     * @return 返回与当前SessionFactory绑定的PlatformTransactionManager对象
     */
    PlatformTransactionManager getPlatformTransactionManager();
    /**
     * @return 返回当前系统的登录用户
     */
    String getLoginUser();
    /**
     * @return 返回当前系统分类ID
     */
    String getCategoryId();
}
