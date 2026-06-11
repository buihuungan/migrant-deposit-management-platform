package cn.zhenglili.fmaking.desform.dao;

import cn.zhenglili.fmaking.desform.entity.DesForm;
import cn.zhenglili.fmaking.desform.entity.DesFormQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName：DesFormDao
 * @Description：TODO
 * @Author：zhenglili
 * @Date：2020/7/29 15:47
 **/
@Mapper
@Component
public interface DesFormDao {
    /**
     * 根据表单id查看表单基本信息+content
     *
     * @param id
     * @return
     */
    DesForm selectOne(@Param("id") String id);

    /**
     * 根据用户名+表单编码/表单名称查询表单基本信息+content列表
     */
    List<DesForm> selectDesFormList(@Param("desFormQuery") DesFormQuery desFormQuery,
                                    @Param("userId") String userId,
                                    @Param("pageOff") Integer pageOff,
                                    @Param("pageSize") Integer pageSize,
                                    @Param("tenantId") String tenantId);

    /**
     * 根据用户名+表单编码/表单名称查询表单基本信息+content列表 全量数据
     */
    List<DesForm> selectAllDesFormList(@Param("desFormQuery") DesFormQuery desFormQuery,
                                    @Param("userId") String userId,
                                    @Param("tenantId") String tenantId);

    /**
     * 新增表单设计基础信息
     *
     * @param desForm
     * @return
     */
    Integer insertDesForm(DesForm desForm);

    /**
     * 更新表单设计基础信息
     *
     * @param desForm
     * @return
     */
    Integer updateDesForm(DesForm desForm);

    /**
     * 更新表单设计content
     *
     * @param desForm
     * @return
     */
    Integer updateDesFormContent(DesForm desForm);

    /**
     * 更新表单设计删除标识
     *
     * @param id
     * @param deleteFlag
     * @return
     */
    Integer updateDeleteFlag(@Param("id") String id, @Param("deleteFlag") int deleteFlag);

    /**
     * 根据id删除表单
     *
     * @param id
     * @return
     */
    Integer deleteById(@Param("id") String id);

    /**
     * 批量删除设计的表单
     */
    Integer deleteBatchIds(@Param("ids") List<String> ids);

    /**
     * 查询总数
     * @param userId
     * @return
     */
    Integer count(@Param("userId") String userId);
}
