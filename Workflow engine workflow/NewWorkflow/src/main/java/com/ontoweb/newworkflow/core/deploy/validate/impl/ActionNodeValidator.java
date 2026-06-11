package com.ontoweb.newworkflow.core.deploy.validate.impl;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:40
 * @description：
 * @modified By：
 * @version: $
 */
public class ActionNodeValidator extends NodeValidator{

    @Override
    public void validate(Element element, List<String> errors, List<String> nodeNames) {
        super.validate(element, errors,nodeNames);
        String handlerBean=element.getAttribute("handler-bean");
        if(StringUtils.isEmpty(handlerBean)){
            errors.add("动作节点必须要定义实现类Bean");
        }
    }

    @Override
    public boolean support(Element element) {
        return element.getNodeName().equals("action");
    }

    @Override
    public String getNodeName() {
        return "动作";
    }
}
