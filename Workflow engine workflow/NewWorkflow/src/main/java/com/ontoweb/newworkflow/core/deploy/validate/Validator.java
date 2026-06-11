package com.ontoweb.newworkflow.core.deploy.validate;

import org.w3c.dom.Element;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:39
 * @description：
 * @modified By：
 * @version: $
 */
public interface Validator {
    void validate(Element element, List<String> errors, List<String> nodeNames);
    boolean support(Element element);
    String getNodeName();
}
