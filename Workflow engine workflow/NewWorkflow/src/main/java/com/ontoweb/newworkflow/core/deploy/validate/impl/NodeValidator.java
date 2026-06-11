package com.ontoweb.newworkflow.core.deploy.validate.impl;

import com.ontoweb.newworkflow.core.deploy.validate.Validator;
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
public abstract class NodeValidator implements Validator {

    @Override
    public void validate(Element element, List<String> errors, List<String> nodeNames) {
        String name=element.getAttribute("name");
        if(StringUtils.isEmpty(name)){
            errors.add(getNodeName()+"节点未定义名称");
        }else{
            if(nodeNames.contains(name)){
                errors.add("有一个以上名为"+name+"的节点，在一个流程模版当中每个节点名都需要唯一");
            }else{
                nodeNames.add(name);
            }
        }
    }
}
