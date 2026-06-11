package cn.zhenglili.fmaking.desform.service.impl;

import cn.zhenglili.fmaking.desform.dao.DesFormDao;
import cn.zhenglili.fmaking.desform.entity.DesForm;
import cn.zhenglili.fmaking.desform.entity.DesFormQuery;
import cn.zhenglili.fmaking.desform.service.DesFormService;
import cn.zhenglili.fmaking.feign.online.service.OnlineFeignService;
import cn.zhenglili.fmaking.utils.CheckUtils;
import cn.zhenglili.fmaking.utils.Result;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName：DesFormServiceImpl
 * @Description：TODO
 * @Author：zhenglili
 * @Date：2020/7/29 15:45
 **/
@Service
@Slf4j
public class DesFormServiceImpl implements DesFormService {
    @Autowired
    private DesFormDao desFormDao;

    @Autowired
    private OnlineFeignService onlineFeignService;


    @Override
    public DesForm findOneById(String id) {
        return desFormDao.selectOne(id);
    }

    @Override
    public List<DesForm> desFormList(DesFormQuery desFormQuery, String userId, Integer pageNo, Integer pageSize, String tenantId) {
        Integer pageOff = (pageNo - 1) * pageSize;
        return desFormDao.selectDesFormList(desFormQuery, userId, pageOff, pageSize, tenantId);
//        return desFormDao.selectDesFormList(desFormQuery, userId, pageOff, pageSize);
    }

    @Override
    public List<DesForm> allDesFormList(DesFormQuery desFormQuery, String userId, String tenantId) {
        return desFormDao.selectAllDesFormList(desFormQuery, userId, tenantId);
    }

    @Override
    public Integer createDesForm(DesForm desForm) {
        String id = UUID.randomUUID().toString().replace("-", "");
        desForm.setId(id);
        desForm.setCreateTime(new Date());
        desForm.setUpdateTime(new Date());
        return desFormDao.insertDesForm(desForm);
    }

    @Override
    public Integer updateDesForm(DesForm desForm) {
        desForm.setUpdateTime(new Date());
        return desFormDao.updateDesForm(desForm);
    }

    @Override
    public Result updateDesFormContent(String id, String content) {
        DesForm desForm = new DesForm();
        desForm.setId(id);
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(content);
//            Result result = this.checkContent(jsonObject);
//            if (!result.isSuccess()) {
//                return result;
//            }
//        } catch (JSONException e) {
//            return Result.error(500, "content-json格式不合法");
//        } catch (Exception e) {
//            return Result.error(500, e.getMessage());
//        }
        desForm.setContent(content);
        desForm.setUpdateTime(new Date());
        log.info("desForm:[{}]", desForm);
        int i = desFormDao.updateDesFormContent(desForm);
        return i != 1 ? Result.error(500, "更新content失败") : Result.ok("更新content成功");
    }

    @Override
    public Integer updateDeleteFlag(String id, int deleteFlag) {
        return desFormDao.updateDeleteFlag(id, deleteFlag);
    }

    @Override
    public Integer deleteDesForm(String id) {
        return desFormDao.deleteById(id);
    }

    @Override
    public Integer deleteDesFormByIds(List<String> ids) {
        return desFormDao.deleteBatchIds(ids);
    }

    @Override
    public Result checkContent(JSONObject jsonObject) {
        // 获取绑定的表Id
        String tableId = jsonObject.getJSONObject("config").getString("tableId");
        if (StringUtils.isEmpty(tableId)) {
            return Result.error(500, "绑定表Id为空");
        }
        // 微服务间调用online,查询表的基本信息
        Result<?> tableDetailResult = onlineFeignService.listByHeadId(tableId);
        if (tableDetailResult.getCode() != 200) {
            // 表不存在返回500,校验失败
            return Result.error(500, tableDetailResult.getMessage());
        }
        // 表存在，获取详细信息
        List<Map<String, Object>> list = (List<Map<String, Object>>) tableDetailResult.getResult();

        Result<?> result = onlineFeignService.queryById(tableId);
        if (result.getCode() != 200) {
            return Result.error(500, tableDetailResult.getMessage());
        }
        Map<String, Object> tableMap = (Map<String, Object>) result.getResult();
        // 为表的每个属性设置校验字段
        list.forEach(c -> c.put("flag", 0));

        // 获取jsonObject中的list
        JSONArray jsonList = jsonObject.getJSONArray("list");
        // 获取JSON数组中的某一个json实体
        // 注: 直接.get获取到一个Object
        for (int index = 0; index < jsonList.size(); index++) {
            // 获取控件属性
            JSONObject btn = jsonList.getJSONObject(index);
            // 控件类型
            String type = btn.getString("type");
            // 获取对应数据表属性
            // 子表-递归
            if ("table".equals(type)) {
                String subTableId = btn.getString("tableId");

                String subTableStr = tableMap.get("subTableStr").toString();
                if (!subTableStr.contains(subTableId)) {
                    return Result.error("该表无此子表");
                }

                Result subResult = this.checkSubTableContent(btn);
                if (!result.isSuccess()) {
                    return result;
                }
            } else {
                // 简单控件类型
                String dbId = btn.getString("tableColId");
                for (Map<String, Object> map : list) {
                    if (dbId.equals(map.get("id").toString())) {
                        String dbType = map.get("dbType").toString();
                        if (!CheckUtils.checkType(type, dbType)) {
                            String message = "第" + (index + 1) + "个属性类型出错:" + type + "类型控件不可匹配数据库" + dbType + "类型";
                            return Result.error(500, message);
                        }
                        map.put("flag", Integer.parseInt(map.get("flag").toString()) + 1);
                    }
                }
            }
        }
        // 校验绑定表的所有属性是否都正确绑定了控件
        for (Map<String, Object> map : list) {
            int flag = Integer.parseInt(map.get("flag").toString());
            String filedName = map.get("dbFieldName").toString();
            if (flag == 0) {
                if (!"id".equals(filedName) && !"create_time".equals(filedName) &&
                        !"create_by".equals(filedName) && !"update_time".equals(filedName) && !"update_by".equals(filedName)) {
                    String message = map.get("dbFieldName") + "字段没有对应控件";
                    return Result.error(500, message);
                }
            }
            if (flag > 1) {
                String message = map.get("dbFieldName") + "字段对应多个控件";
                return Result.error(500, message);
            }
        }
        return Result.ok("校验合法");

    }

    @Override
    public Integer count(String userId) {
        return desFormDao.count(userId);
    }

    public Result checkSubTableContent(JSONObject jsonObject) {
        // 获取绑定的表Id
        String tableId = jsonObject.getJSONObject("config").getString("tableId");
        if (StringUtils.isEmpty(tableId)) {
            return Result.error(500, "绑定表Id为空");
        }
        // 微服务间调用online,查询表的基本信息
        Result<?> tableDetailResult = onlineFeignService.listByHeadId(tableId);
        if (tableDetailResult.getCode() != 200) {
            // 表不存在返回500,校验失败
            return Result.error(500, tableDetailResult.getMessage());
        }
        // 表存在，获取详细信息
        List<Map<String, Object>> list = (List<Map<String, Object>>) tableDetailResult.getResult();

        Result<?> result = onlineFeignService.queryById(tableId);
        if (result.getCode() != 200) {
            return Result.error(500, tableDetailResult.getMessage());
        }
        Map<String, Object> tableMap = (Map<String, Object>) result.getResult();
        // 为表的每个属性设置校验字段
        list.forEach(c -> c.put("flag", 0));

        // 获取jsonObject中的list
        JSONArray jsonList = jsonObject.getJSONArray("tableColumns");
        // 获取JSON数组中的某一个json实体
        // 注: 直接.get获取到一个Object
        for (int index = 0; index < jsonList.size(); index++) {
            // 获取控件属性
            JSONObject btn = jsonList.getJSONObject(index);
            // 控件类型
            String type = btn.getString("type");
            // 获取对应数据表属性
            // 子表-递归
            if ("table".equals(type)) {
                String subTableId = btn.getString("tableId");

                String subTableStr = tableMap.get("subTableStr").toString();
                if (!subTableStr.contains(subTableId)) {
                    return Result.error("该表无此子表");
                }

                Result subResult = this.checkSubTableContent(btn);
                if (!result.isSuccess()) {
                    return result;
                }
            } else {
                // 简单控件类型
                String dbId = btn.getString("tableColId");
                for (Map<String, Object> map : list) {
                    if (dbId.equals(map.get("id").toString())) {
                        String dbType = map.get("dbType").toString();
                        if (!CheckUtils.checkType(type, dbType)) {
                            String message = "第" + (index + 1) + "个属性类型出错:" + type + "类型控件不可匹配数据库" + dbType + "类型";
                            return Result.error(500, message);
                        }
                        map.put("flag", Integer.parseInt(map.get("flag").toString()) + 1);
                    }
                }
            }
        }
        // 校验绑定表的所有属性是否都正确绑定了控件
        for (Map<String, Object> map : list) {
            int flag = Integer.parseInt(map.get("flag").toString());
            String filedName = map.get("dbFieldName").toString();
            if (flag == 0) {
                if (!"id".equals(filedName) && !"create_time".equals(filedName) &&
                        !"create_by".equals(filedName) && !"update_time".equals(filedName) && !"update_by".equals(filedName)) {
                    String message = map.get("dbFieldName") + "字段没有对应控件";
                    return Result.error(500, message);
                }
            }
            if (flag > 1) {
                String message = map.get("dbFieldName") + "字段对应多个控件";
                return Result.error(500, message);
            }
        }
        return Result.ok("校验合法");
    }
}
