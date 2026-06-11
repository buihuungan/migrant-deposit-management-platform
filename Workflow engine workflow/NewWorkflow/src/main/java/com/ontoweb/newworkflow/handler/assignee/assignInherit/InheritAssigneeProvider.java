package com.ontoweb.newworkflow.handler.assignee.assignInherit;

import com.ontoweb.newworkflow.core.process.assign.AssigneeProvider;
import com.ontoweb.newworkflow.core.process.assign.PageQuery;

/**
 * @Author Ethan yan
 * @Date 2020/12/19
 * @Desc 指定参与者显示内容过少，需要重写接口
 */
public interface InheritAssigneeProvider extends AssigneeProvider {
    //增加方法
    void queryInheritEntities(PageQuery<InheritEntity> pageQuery, String parentId);
}
