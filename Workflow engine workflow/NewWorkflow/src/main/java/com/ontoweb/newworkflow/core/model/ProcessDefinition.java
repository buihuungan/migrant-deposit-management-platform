package com.ontoweb.newworkflow.core.model;

import com.ontoweb.newworkflow.core.diagram.ProcessDiagram;
import com.ontoweb.newworkflow.core.process.node.Node;
import com.ontoweb.newworkflow.core.process.node.StartNode;
import com.ontoweb.newworkflow.core.process.security.ComponentAuthority;
import com.ontoweb.newworkflow.core.process.swimlane.Swimlane;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:13
 * @description：流程定义信息
 * @modified By：
 * @version: $
 */
@Entity
@Table(name="UFLO_PROCESS")
@Data
public class ProcessDefinition implements Serializable {
    private static final long serialVersionUID = -1328642749306459546L;

    @Id
    @Column(name="ID_")
    private long id;

    @Column(name="NAME_",length=60)
    private String name;

    @Column(name="KEY_",length=60)
    private String key;

    @Column(name="START_PROCESS_URL_",length=120)
    private String startProcessUrl;

    @Column(name="VERSION_")
    private int version;

    @Column(name="CREATE_DATE_")
    private Date createDate;

    @Column(name="EFFECT_DATE_")
    private Date effectDate;

    @Column(name="CATEGORY_ID_",length=60)
    private String categoryId;

    @Column(name="DESCRIPTION_")
    private String description;

    @Column(name="PROCESS_MAIN_TABLE_")
    private String processMainTable;

    @Column(name="PROCESS_MAIN_KEY_")
    private String processMainKey;

    @Column(name="MAIN_TABLE_ASSOCIATION_KEY_")
    private String mainTableAssociationKey;

    @Column(name="PROCESS_ASSOCIATION_TABLE_")
    private String processAssociationTable;

    @Column(name="PROCESS_ASSOCIATION_KEY_")
    private String processAssociationKey;

    @Transient
    private String eventHandlerBean;

    @Transient
    private StartNode startNode;

    @Transient
    private List<Node> nodes;

    @Transient
    private List<Swimlane> swimlanes;

    @Transient
    private List<ComponentAuthority> componentSecuritys;

    @Transient
    private ProcessDiagram diagram;

    public Node getNode(String name){
        for(Node node:nodes){
            if(node.getName().equals(name)){
                return node;
            }
        }
        return null;
    }

    public Swimlane getSwimlane(String name){
        for(Swimlane swimlane:swimlanes){
            if(swimlane.getName().equals(name)){
                return swimlane;
            }
        }
        return null;
    }
}
