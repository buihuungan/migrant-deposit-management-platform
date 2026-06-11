package com.ontoweb.newworkflow.core.command.impl;

import com.ontoweb.newworkflow.core.command.Command;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ContextProperty;
import com.ontoweb.newworkflow.handler.dao.AcquireDbidDao;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午9:26
 * @description：获取ID的实现类,command命令
 * @modified By：
 * @version: $
 */
@Service
public class AcquireDbidCommand{
    private static final String ID_KEY="dbid";

    @Autowired
    private AcquireDbidDao acquireDbidDao;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Long getId(Integer blockSize) {
        long nextId=0;
        List<ContextProperty> list= acquireDbidDao.getlist(ID_KEY);
        if(list.size()>0){
            ContextProperty prop=list.get(0);
            nextId=Long.valueOf(prop.getValue());
            prop.setValue(String.valueOf(nextId+blockSize));

            //
            acquireDbidDao.save(prop);
        }else{
            ContextProperty prop=new ContextProperty();
            prop.setKey(ID_KEY);
            prop.setValue(String.valueOf(blockSize));
            acquireDbidDao.save(prop);
        }
        return nextId+1;
    }
}
