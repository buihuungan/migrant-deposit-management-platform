package com.ontoweb.newworkflow.core.deploy.validate.impl;

import com.ontoweb.newworkflow.core.process.node.DecisionType;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午10:41
 * @description：
 * @modified By：
 * @version: $
 */
public class DecisionNodeValidator extends NodeValidator{
    @Override
    public void validate(Element element, List<String> errors, List<String> nodeNames) {
        super.validate(element, errors,nodeNames);
        String type=element.getAttribute("decision-type");
        if(StringUtils.isEmpty(type)){
            errors.add("路由决策节点必须要指定决策条件判断方式");
        }else{
            DecisionType decisionType=DecisionType.valueOf(type);
            if(decisionType.equals(DecisionType.Expression)){
                if(StringUtils.isEmpty(element.getAttribute("expression"))){
                    NodeList nodeList=element.getChildNodes();
                    boolean hasExpr=false;
                    for(int i=0;i<nodeList.getLength();i++){
                        Node node=nodeList.item(i);
                        if(!(node instanceof Element)){
                            continue;
                        }
                        Element childElement=(Element)node;
                        if(childElement.getNodeName().equals("expression")){
                            hasExpr=true;
                            break;
                        }
                    }
                    if(!hasExpr){
                        errors.add("路由决策节点条件判断方式表达式时，必须要指定一个具体表达式");
                    }
                }
            }
            if(decisionType.equals(DecisionType.Handler) && StringUtils.isEmpty(element.getAttribute("handler-bean"))){
                errors.add("路由决策节点条件判断方式实现类Bean时，必须要指定一个具体实现类Bean");
            }
        }
    }

    @Override
    public String getNodeName() {
        return "路由决策";
    }

    @Override
    public boolean support(Element element) {
        return element.getNodeName().equals("decision");
    }
}
