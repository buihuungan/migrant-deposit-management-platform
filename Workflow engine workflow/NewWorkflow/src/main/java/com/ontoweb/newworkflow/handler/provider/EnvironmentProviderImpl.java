package com.ontoweb.newworkflow.handler.provider;

import com.ontoweb.newworkflow.core.env.EnvironmentProvider;
import com.ontoweb.newworkflow.handler.utils.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/3 上午10:49
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class EnvironmentProviderImpl implements EnvironmentProvider {
    @Autowired
    private HttpServletRequest request;

//    @Autowired
//    private SessionFactory sessionFactory;

//    @Autowired
//    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

//    public Session getSession() {
//        return entityManagerFactory.unwrap(SessionFactory.class).openSession();
//    }

//    @Override
//    public SessionFactory getSessionFactory() {
//        return sessionFactory;
//    }

    @Override
    public PlatformTransactionManager getPlatformTransactionManager() {
        return platformTransactionManager;
    }

    /**
     * 获取流程的分类信息
     * @return
     */
    @Override
    public String getCategoryId() {
        return "暂未分类";
    }

    /**
     * 通过RequestHolder获取登录的用户名
     * 有的写称了userid，有的写成了userId
     * @return
     */
    @Override
    public String getLoginUser() {
        if (request == null)
            return "";
        if (StringUtils.isNotEmpty(request.getHeader("userId"))) {
            return request.getHeader("userid");
        } else {
            return request.getHeader("userId");
        }
    }
}
