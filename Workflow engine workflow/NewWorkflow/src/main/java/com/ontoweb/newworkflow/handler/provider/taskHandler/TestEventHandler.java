package com.ontoweb.newworkflow.handler.provider.taskHandler;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.handler.NodeEventHandler;
import com.ontoweb.newworkflow.core.process.node.Node;
import org.springframework.stereotype.Component;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/14 上午10:54
 * @description：测试
 * @modified By：
 * @version: $
 */
@Component
public class TestEventHandler implements NodeEventHandler {
    @Override
    public void enter(Node node, ProcessInstance processInstance, Context context) {

    }

    @Override
    public void leave(Node node, ProcessInstance processInstance, Context context) {

    }
}
