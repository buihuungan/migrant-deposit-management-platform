package com.ontoweb.newworkflow.core.service;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:24
 * @description：开启流程信息
 * @modified By：
 * @version: $
 */
@XmlRootElement(name = "StartProcessInfo")
@Data
public class StartProcessInfo {
    public static final String KEY = StartProcessInfo.class.getName();
    private String tag;
    private String promoter;
    private String sequenceFlowName;
    private String businessId;
    private String subject;
    private String completeStartTaskOpinion;
    private Map<String, Object> variables;
    private boolean completeStartTask = true;

    public StartProcessInfo() {
    }

    public StartProcessInfo(String promoter) {
        this.promoter = promoter;
    }

    public StartProcessInfo setBusinessId(String businessId) {
        this.businessId = businessId;
        return this;
    }

    @XmlElement(name = "variables")
    @XmlJavaTypeAdapter(MapAdapter.class)
    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
