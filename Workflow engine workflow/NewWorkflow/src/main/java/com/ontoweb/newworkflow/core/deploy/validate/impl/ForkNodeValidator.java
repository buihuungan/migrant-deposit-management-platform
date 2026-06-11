package com.ontoweb.newworkflow.core.deploy.validate.impl;

import org.w3c.dom.Element;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:43
 * @description：
 * @modified By：
 * @version: $
 */
public class ForkNodeValidator extends NodeValidator {
    public boolean support(Element element) {
        return element.getNodeName().equals("fork");
    }

    public String getNodeName() {
        return "分支";
    }
}
