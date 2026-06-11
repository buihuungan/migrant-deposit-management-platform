package com.ontoweb.newworkflow.core.deploy.validate.impl;

import org.w3c.dom.Element;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:44
 * @description：
 * @modified By：
 * @version: $
 */
public class StartNodeValidator extends NodeValidator {

    @Override
    public void validate(Element element, List<String> errors, List<String> nodeNames) {
        super.validate(element, errors,nodeNames);
    }

    @Override
    public boolean support(Element element) {
        return element.getNodeName().equals("start");
    }

    @Override
    public String getNodeName() {
        return "开始";
    }
}
