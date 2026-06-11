package cn.zhenglili.fmaking.desform.service;

import cn.zhenglili.fmaking.desform.entity.DesForm;
import cn.zhenglili.fmaking.desform.entity.DesFormQuery;
import cn.zhenglili.fmaking.utils.Result;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName：DesFormService
 * @Description：TODO
 * @Author：zhenglili
 * @Date：2020/7/29 15:44
 **/
public interface DesFormService {
    /**
     * 根据表单id查询表单基本信息+content
     *
     * @param id
     * @return
     */
    DesForm findOneById(String id);

    /**
     * 根据用户名+表单编码/表单名称查询表单基本信息+content列表
     *
     * @param desFormQuery
     * @return
     */
    List<DesForm> desFormList(DesFormQuery desFormQuery, String userId, Integer pageNo, Integer pageSize, String tenantId);

    /**
     * 根据用户名+表单编码/表单名称查询表单基本信息+content列表
     *
     * @param desFormQuery
     * @return
     */
    List<DesForm> allDesFormList(DesFormQuery desFormQuery, String userId, String tenantId);

    /**
     * 新增表单设计基础信息
     *
     * @param desForm
     * @return
     */
    Integer createDesForm(DesForm desForm);

    /**
     * 修改表单设计基础信息
     *
     * @param desForm
     * @return
     */
    Integer updateDesForm(DesForm desForm);

    /**
     * 修改表单设计content
     *
     * @param id
     * @param content
     * @return
     */
    Result updateDesFormContent(String id, String content);

    /**
     * 修改表单设计删除标识
     *
     * @param id
     * @param deleteFlag
     * @return
     */
    Integer updateDeleteFlag(String id, int deleteFlag);

    /**
     * 删除表单
     *
     * @param id
     * @return
     */
    Integer deleteDesForm(String id);

    /**
     * 批量删除表单设计
     *
     * @param ids
     * @return
     */
    Integer deleteDesFormByIds(List<String> ids);

    /**
     * 检验content
     *
     * @param jsonObject
     * @return
     */
    Result checkContent(JSONObject jsonObject);

    Integer count(@Param("userId") String userId);
}
