package com.ontoweb.newworkflow.core.utils;

import com.ontoweb.newworkflow.core.command.CommandService;
import com.ontoweb.newworkflow.core.command.impl.AcquireDbidCommand;
import com.ontoweb.newworkflow.handler.dao.AcquireDbidDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StaleStateException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Random;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午11:23
 * @description：ID生成器
 * @modified By：
 * @version: $
 */
@Component
public class IDGenerator implements ApplicationContextAware {
    private static Log log = LogFactory.getLog(IDGenerator.class);

    @Autowired
    private AcquireDbidCommand acquireDbidCommand;

    private static IDGenerator generator;
    private static Random random = new Random();
    private long nextId;
    private long lastId=-1;
    private int blockSize=5000;
    private int maxAttempts = 5;

    public synchronized long nextId() {
        if (lastId < nextId) {
            for (int attempts = maxAttempts; (attempts > 0); attempts--) {
                try {
                    nextId = acquireDbidCommand.getId(blockSize);
                    lastId = nextId + blockSize - 1;
                    break;
                } catch (StaleStateException e) {
                    attempts--;
                    if (attempts == 0) {
                        throw new IllegalStateException("couldn't acquire block of ids, tried "+ maxAttempts + " times");
                    }
                    // if there are still attempts left, first wait a bit
                    int millis = 20 + random.nextInt(200);
                    log.debug("optimistic locking failure while trying to acquire id block.  retrying in "+ millis + " millis");
                    try {
                        Thread.sleep(millis);
                    } catch (InterruptedException e1) {
                        log.debug("waiting after id block locking failure got interrupted");
                    }
                }
            }
        }
        return nextId++;
    }

    public static IDGenerator getInstance(){
        return generator;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        IDGenerator.generator=this;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
}
