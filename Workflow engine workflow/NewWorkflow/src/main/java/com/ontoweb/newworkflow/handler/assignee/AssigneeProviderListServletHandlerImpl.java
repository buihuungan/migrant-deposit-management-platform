package com.ontoweb.newworkflow.handler.assignee;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.process.assign.PageQuery;
import com.ontoweb.newworkflow.handler.assignee.assignInherit.InheritAssignee;
import com.ontoweb.newworkflow.handler.assignee.assignInherit.InheritAssigneeInfo;
import com.ontoweb.newworkflow.handler.assignee.assignInherit.InheritAssigneeProvider;
import com.ontoweb.newworkflow.handler.assignee.assignInherit.InheritEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @Author Ethan yan
 * @Date 20201220
 * @Desc 重写assigneeProvider并没有进入增加的方法，需要重写其调用类
 */
@Service
@Slf4j
public class AssigneeProviderListServletHandlerImpl implements ApplicationContextAware {

    Map<String, InheritAssigneeProvider> assigneeProviderMaps = new HashMap();

    public JSONObject getAssignee(JSONObject jsonObject) {
        JSONObject jsonObjectReturn = new JSONObject();

        // 前端没有传递jsonObject时，new一个对象，否则会空错误
        if (jsonObject == null)
            jsonObject = new JSONObject();

        String pageIndex = jsonObject.getString("pageIndex");
        if (StringUtils.isEmpty(pageIndex)) {
            pageIndex = "1";
        }

        String pageSize = jsonObject.getString("pageSize");
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "1000";
        }

        String parentId = jsonObject.getString("parentId");
        if (parentId != null && parentId.equals("null")) {
            parentId = null;
        }

        String providerId = jsonObject.getString("providerId");
        log.info("providerId{}" + providerId);
        PageQuery<InheritEntity> pageQuery = new PageQuery(Integer.valueOf(pageIndex), Integer.valueOf(pageSize));
        if (StringUtils.isNotEmpty(providerId)) {
            InheritAssigneeInfo info = this.buildAssigneeInfo(parentId, pageQuery, providerId, true);
            jsonObjectReturn.put("result", info);
            //this.writeObjectToJson(response, info);
        } else {
            List<InheritAssigneeInfo> result = new ArrayList();
            Iterator var10 = this.assigneeProviderMaps.keySet().iterator();

            while (var10.hasNext()) {
                String beanId = (String) var10.next();
                InheritAssigneeInfo info = this.buildAssigneeInfo(parentId, pageQuery, beanId, false);
                result.add(info);
            }

            jsonObjectReturn.put("result", result);
        }

        return jsonObjectReturn;
    }

    private InheritAssigneeInfo buildAssigneeInfo(String parentId, PageQuery<InheritEntity> pageQuery, String beanId, boolean buildEntity) {
        InheritAssigneeProvider provider = (InheritAssigneeProvider)this.assigneeProviderMaps.get(beanId);
        provider.queryInheritEntities(pageQuery, parentId);
        InheritAssigneeInfo info = new InheritAssigneeInfo();
        info.setName(provider.getName());
        info.setTree(provider.isTree());
        info.setProviderId(beanId);
        if (buildEntity) {
            List<InheritAssignee> assignees = new ArrayList();
            Collection<InheritEntity> entitys = pageQuery.getResult();
            if (entitys != null) {
                Iterator var9 = entitys.iterator();

                while(var9.hasNext()) {
                    InheritEntity entity = (InheritEntity) var9.next();
                    InheritAssignee assignee = new InheritAssignee();
                    assignee.setId(entity.getId());
                    assignee.setName(entity.getName());
                    assignee.setAddProperty(entity.getAddProperty());
                    assignee.setProviderId(beanId);
                    assignee.setChosen(entity.isChosen());
                    assignees.add(assignee);
                }
            }

            log.info("PageQuery{}" + pageQuery);
            info.setInheritAssignee(assignees);
            info.setCount(pageQuery.getRecordCount());
        }

        return info;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, InheritAssigneeProvider> map = applicationContext.getBeansOfType(InheritAssigneeProvider.class);
        Iterator var3 = map.keySet().iterator();
        log.info("var3{}" + var3);
        while(var3.hasNext()) {
            String beanId = (String)var3.next();
            InheritAssigneeProvider provider = (InheritAssigneeProvider)map.get(beanId);
            if (!provider.disable()) {
                this.assigneeProviderMaps.put(beanId, provider);
            }
        }
    }
}
