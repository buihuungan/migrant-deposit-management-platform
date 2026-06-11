package com.ontoweb.newworkflow.handler.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.core.model.BlobTemp;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.service.TaskService;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import com.ontoweb.newworkflow.handler.VO.CictOfflineDataVo;
import com.ontoweb.newworkflow.handler.VO.EduPlanDetailVo;
import com.ontoweb.newworkflow.handler.VO.ProcessUsersVo;
import com.ontoweb.newworkflow.handler.VO.ProcessVo;
import com.ontoweb.newworkflow.handler.assignee.entity.SysUser;
import com.ontoweb.newworkflow.handler.dao.BlobDao;
import com.ontoweb.newworkflow.handler.dao.BlobTempDao;
import com.ontoweb.newworkflow.handler.dao.ProcessInstanceDao;
import com.ontoweb.newworkflow.handler.dao.TaskDao;
import com.ontoweb.newworkflow.handler.entity.SysUserRole;
import com.ontoweb.newworkflow.handler.entity.TempRoleRelation;
import com.ontoweb.newworkflow.handler.mapper.SysUserRoleMapper;
import com.ontoweb.newworkflow.handler.mapper.TempRoleRelationMapper;
import com.ontoweb.newworkflow.handler.service.BusinessService;
import com.ontoweb.newworkflow.handler.service.DiagramService;
import com.ontoweb.newworkflow.handler.service.ProcessOperateService;
import com.ontoweb.newworkflow.handler.service.feign.*;
import com.ontoweb.newworkflow.handler.utils.CommonConstant;
import com.ontoweb.newworkflow.handler.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/11/27 上午11:26
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {
    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private TempRoleRelationMapper tempRoleRelationMapper;

    @Autowired
    private BlobTempDao blobTempDao;

    @Autowired
    private UserService userService;

    @Resource
    private BlobDao blobDao;

    @Autowired
    private ProcessService processService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ProcessInstanceDao processInstanceDao;

    @Autowired
    private ProcessOperateService processOperateService;

    @Autowired
    private DiagramService diagramService;

    @Autowired
    private EduService eduService;

    @Resource
    private ScadaService scadaService;

    @Resource
    private OrderService orderService;

    @Resource
    private ErpService erpService;

    @Override
    public Boolean grantRole(JSONObject jsonObject) {
        //删除原有角色
        String userId = jsonObject.getString("userId");
        LambdaQueryWrapper<TempRoleRelation> tempRoleRelationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tempRoleRelationLambdaQueryWrapper.eq(TempRoleRelation::getUserId, userId);
        List<TempRoleRelation> tempRoleRelations = tempRoleRelationMapper.selectList(tempRoleRelationLambdaQueryWrapper);
        for (TempRoleRelation tempRoleRelation : tempRoleRelations) {
            LambdaQueryWrapper<SysUserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userRoleLambdaQueryWrapper.eq(SysUserRole::getId, tempRoleRelation.getRoleId());
            userRoleMapper.delete(userRoleLambdaQueryWrapper);
        }

        tempRoleRelationMapper.delete(tempRoleRelationLambdaQueryWrapper);

        //授予新的角色

        SysUserRole sysUserRole = new SysUserRole(userId, jsonObject.getString("roleIds"));
        userRoleMapper.insert(sysUserRole);
        TempRoleRelation tempRoleRelation = new TempRoleRelation();
        tempRoleRelation.setRoleId(sysUserRole.getId());
        tempRoleRelation.setUserId(userId);
        tempRoleRelationMapper.insert(tempRoleRelation);

        return true;
    }

    @Override
    public List<JSONObject> getLists(String identification) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        List<BlobTemp> all = blobTempDao.findAllByOrderByCreateTimeDesc();
        for (BlobTemp blobTemp : all) {
            JSONObject jsonObject = new JSONObject();
            byte[] blobValue = blobTemp.getBlobValue();
            String s = new String(blobValue);
                    System.out.println("s的值"+s);
            JSONObject parse = JSONObject.parseObject(s);
            System.out.println("parse的值"+parse);
            String userId = parse.getString("user_id");
            String categories = parse.getString("category_id");
            SysUser result = userService.queryById(userId).getResult();
            String realname = result.getRealname();
            jsonObject.put("wareTeacher", realname);
            String[] split = categories.split("-");
            List<String> categoryList = new ArrayList<>(Arrays.asList(split));
            // 课程名称
            if (categoryList.contains(identification)) {
                jsonObject.put("wareId", blobTemp.getId());
                jsonObject.put("wareName", parse.getString("name"));
                jsonObject.put("fileName", blobTemp.getName());
                int domain = categoryList.indexOf("domain");
                int type = categoryList.indexOf("type");
                int industry = categoryList.indexOf("industry");
                int end = categoryList.size();
                List<String> domains = new ArrayList<>();
                List<String> types = new ArrayList<>();
                List<String> industries = new ArrayList<>();
                for (int i = 1; i < type; i++) {
                    domains.add(categoryList.get(i));
                }
                for (int i = type + 1; i < industry; i++) {
                    types.add(categoryList.get(i));
                }
                for (int i = industry + 1; i < end; i++) {
                    industries.add(categoryList.get(i));
                }
//                if (domain > -1 && type > -1) {
//                    for (int i = 1; i < type; i++) {
//                        domains.add(categoryList.get(i));
//                    }
//
//
//                }
//                if (domain > -1 && type < 0) {
//                    for (int i = 1; i < end; i++) {
//                        domains.add(categoryList.get(i));
//                    }
//                }
//
//                if (type > -1) {
//                    for (int i = type + 1; i < end; i++) {
//                        types.add(categoryList.get(i));
//                    }
//                }
                jsonObject.put("domains", domains);
                jsonObject.put("types", types);
                jsonObject.put("industries", industries);
                jsonObject.put("ware", parse);

                jsonObjects.add(jsonObject);
            }
        }
        return jsonObjects;
    }

    @Override
    public List<JSONObject> getWaresByCategory(String category, String userId)  {


        JSONObject json = eduService.selectWareIdByUserId(userId, category);
//        如果是管理员或者是老师，就查询所有的课件数据

        log.info("jsonObject:{}" , json);

        List<JSONObject> jsonObjects = new ArrayList<>();

        if (json.containsKey("code")&&json.getInteger("code")==500){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",401);
            jsonObject.put("message","没有该课程");
            jsonObjects.add(jsonObject);
            return jsonObjects;
        }


        //设计有问题，但是能跑  hhhh
        List<EduPlanDetailVo> list = JSONObject.parseArray(JSONObject.toJSONString(json.getJSONArray("list")), EduPlanDetailVo.class);
        log.info("list:{}",list);
        for (EduPlanDetailVo detailVo : list) {
            extracted(category, jsonObjects, detailVo);

        }

        return jsonObjects;
    }



    @Override
    public Double getPercentageByProceeeInsId(long processInstanceId) {
        double f1 = 0;
        if (processInstanceId == 0)
            return f1;

        Blob blob = blobDao.findblobProcessId(processService.getProcessInstanceById(processInstanceId).getProcessId());
        byte[] blobValue = blob.getBlobValue();
        String s = new String(blobValue);
        JSONObject parse = JSONObject.parseObject(s);
        List<JSONObject> list = JSONObject.parseArray(parse.getString("nodeList"), JSONObject.class);
        int nodeNum = 0;
        for (JSONObject jsonObject : list) {
            if ("task".equals(jsonObject.getString("type")))
                nodeNum++;
        }

        if (nodeNum > 0) {
            log.debug("nodeNum:{}" + nodeNum);
            int taskNum = taskDao.getTaskNum(processInstanceId);
            log.debug("taskNum:{}" + taskNum);
            Double result = (double) taskNum / nodeNum;
            BigDecimal bg = new BigDecimal(result * 100);
            f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        return f1;
    }

    /**
     * <p>
     * flag为0表示新开启的课程
     * flag为1表示继续上课
     * </p>
     *
     * @param processVo
     * @return
     */
    @Override
    public JSONObject startClassByProcessId(ProcessVo processVo,String wareId) {
        JSONObject jsonObject = new JSONObject();
        Long processId = processVo.getProcessId();
        String teachingMode = processVo.getTeachingMode();
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        // 写死
        loginUser = "1437275060307296257";
        String planId = processVo.getPlanId();
        String lessonId = processVo.getLessonId();

        ProcessInstance processInstance ;
        /*
        一些逻辑似乎对我无用，可以去掉
         */
//        if ("1".equals(teachingMode)) {
//            processInstance = processInstanceDao.isStartClass(loginUser, processId);
//        }else {
//            Result<?> result = eduService.getGroupId(loginUser, planId, lessonId);
//            String groupId = result.getResult().toString();
//            if (groupId==null){
//                log.error("该同学没有被分组");
//                jsonObject.put("error","-1");
//                return jsonObject;
//            }
//            log.debug("该同学("+loginUser+")在该计划下("+planId+")的从课程中("+lessonId+")的组号为("+groupId+")");
//            processInstance = processInstanceDao.isStartClass(groupId, processId);
//        }
        /*
        UFLO_PROCESS_INSTANCE 表 对应实例 processInstance
         */
        // 根据用户id （表字段 prooter）和流程id（表字段 process_id_）查询是否有该流程实例

        processInstance = processInstanceDao.isStartClass(loginUser, processId);

        if (processInstance != null) {
            JSONObject diagram = diagramService.drawDiagram(processInstance.getId());
            Task task = taskDao.getTop1Task(processInstance.getId());
            jsonObject.put("exampleId",processInstance.getExampleId());
            jsonObject.put("ware", diagram);
            jsonObject.put("taskId", task.getId());
            jsonObject.put("processInstanceId", processInstance.getId());
            // flag 字段作用是什么？
            jsonObject.put("flag", 1);
        } else {
            long example = System.currentTimeMillis();
            Random random = new Random();
            int anInt = random.nextInt(1000000);

            long exampleId = example+anInt;
            String s = String.valueOf(exampleId);
            //因为 部署的ID和未部署的ID是不同的
            String name = blobDao.getNumOfProcess(Long.valueOf(wareId)).getName();
            Long id = blobTempDao.getWareIdByName(name).getId();
            String wareIdString = id.toString();
            // 以下两行似乎对我无用，可以去掉
//            orderService.insertInitData(s,wareIdString);
//            erpService.insertInitData(s,wareIdString);

            // 给定 流程id 启动流程实例
            jsonObject = processOperateService.startBy(processVo,s);

            // 以下代码作用 似乎也不需要？
//            try {
//                scadaService.deleteOfflineData(new CictOfflineDataVo(loginUser,planId,lessonId,teachingMode));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            jsonObject.put("flag", 0);
            jsonObject.put("exampleId",s);
        }
        log.info("结束");

        return jsonObject;
    }

    @Override
    public String getTaskId(ProcessVo processVo) {
        Long processId = processVo.getProcessId();
        String teachingMode = processVo.getTeachingMode();
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        // 演示 写死
        loginUser = "1437275060307296257";
        String planId = processVo.getPlanId();
        String lessonId = processVo.getLessonId();
        ProcessInstance processInstance ;
        processInstance = processInstanceDao.isStartClass(loginUser, processId);
        // planId  lessonId  teachingMode 对我而言无用
//        if ("1".equals(teachingMode)) {
//            processInstance = processInstanceDao.isStartClass(loginUser, processId);
//        }else {
//            Result<?> result = eduService.getGroupId(loginUser, planId, lessonId);
//            String groupId = result.getResult().toString();
//            if (groupId==null){
//                log.error("该同学没有被分组");
//                return "-1";
//            }
//            log.debug("该同学("+loginUser+")在该计划下("+planId+")的从课程中("+lessonId+")的组号为("+groupId+")");
//            processInstance = processInstanceDao.isStartClass(groupId, processId);
//        }
        String taskId = taskDao.getNewTaskId(processInstance.getId());

        return taskId;
    }


    @Override
    public Boolean saveDesc(JSONObject jsonObject) {
        String desc = jsonObject.getString("description");
        long id = jsonObject.getLong("blobTempId");
        Integer bool = blobTempDao.updateDesc(id, desc);
        if (bool > 0)
            return true;

        return false;
    }

    @Override
    public Double getPercentagecms(ProcessVo processVo) {
        Long processId = processVo.getProcessId();
        String teachingMode = processVo.getTeachingMode();
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        String planId = processVo.getPlanId();
        String lessonId = processVo.getLessonId();
        ProcessInstance processInstance;
        if ("1".equals(teachingMode)) {
            processInstance = processInstanceDao.isStartClass(loginUser, processId);
        }else {
            Result<?> result = eduService.getGroupId(loginUser, planId, lessonId);
            String groupId = result.getResult().toString();
            if (groupId==null){
                log.error("该同学没有被分组");
                return Double.valueOf("-1");
            }
            log.debug("该同学("+loginUser+")在该计划下("+planId+")的从课程中("+lessonId+")的组号为("+groupId+")");
            processInstance = processInstanceDao.isStartClass(groupId, processId);
        }
        if (processInstance == null)
            return 0.0;
        Double percentageByProceeeInsId = this.getPercentageByProceeeInsId(processInstance.getId());
        log.debug("percentageByProceeeInsId:{}" + percentageByProceeeInsId);
        return percentageByProceeeInsId;
    }

    @Override
    public String getDesc(long processId) {
        Blob blob = blobDao.findblobProcessId(processId);
        String name = blob.getName();
        BlobTemp byFileId = blobTempDao.getByFileId(blobTempDao.findByName(name));

        return byFileId.getDesc();
    }

    @Override
    public String getDescById(long id) {
        BlobTemp byFileId = blobTempDao.getByFileId(id);
        if (byFileId.getDesc() == null)
            return "暂无课程描述信息";
        return byFileId.getDesc();
    }

    @Override
    public JSONObject getDefault(ProcessVo processVo) {
        JSONObject jsonObject = new JSONObject();
        Long processId = processVo.getProcessId();
        String teachingMode = processVo.getTeachingMode();
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        loginUser = "1437275060307296257";
        String planId = processVo.getPlanId();
        String lessonId = processVo.getLessonId();
        ProcessInstance processInstance;

        if ("1".equals(teachingMode)) {
            processInstance = processInstanceDao.isStartClass(loginUser, processId);
        }else {
            Result<?> result = eduService.getGroupId(loginUser, planId, lessonId);
            String groupId = result.getResult().toString();
            if (groupId==null){
                log.error("该同学没有被分组");
                jsonObject.put("error","-1");
                return jsonObject;
            }
            log.debug("该同学("+loginUser+")在该计划下("+planId+")的从课程中("+lessonId+")的组号为("+groupId+")");
            processInstance = processInstanceDao.isStartClass(groupId, processId);
        }

        if (processInstance != null) {
            JSONObject diagram = diagramService.drawDiagram(processInstance.getId());
            Task task = taskDao.getTop1Task(processInstance.getId());
            jsonObject.put("ware", diagram);
            jsonObject.put("processInstanceId", processInstance.getId());
        } else {
            Blob blob = blobDao.findblobProcessId(processId);
            byte[] blobValue = blob.getBlobValue();
            String s = new String(blobValue);
            JSONObject parse = JSONObject.parseObject(s);
            jsonObject.put("ware", parse);
            jsonObject.put("processInstanceId", 0);
        }

        return jsonObject;
    }

    @Override
    public JSONObject getRoleId(long processInstanceId) {
        ProcessInstance processInstanceById = processService.getProcessInstanceById(processInstanceId);
        String currentTask = processInstanceById.getCurrentTask();
        long processId = processInstanceById.getProcessId();
        Blob blob = blobDao.findblobProcessId(processId);
        byte[] blobValue = blob.getBlobValue();
        String s = new String(blobValue);
        JSONObject parse = JSONObject.parseObject(s);
        List<JSONObject> list = JSONObject.parseArray(parse.getString("nodeList"), JSONObject.class);
        String id = "";
        for (JSONObject jsonObject : list) {
            if (jsonObject.getString("name").equals(currentTask)) {
                String json = jsonObject.getString("assignee");
                List<JSONObject> nodeList = JSONArray.parseArray(json, JSONObject.class);
                for (JSONObject jsonObject1 : nodeList) {
                    id = jsonObject1.getString("id");
                }
            }
        }


        JSONObject jsonObject = new JSONObject();
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        // 写死
        loginUser = "1437275060307296257";
        List<String> result = userService.queryUserRole(loginUser).getResult();
        // 用户角色id
        if (result.size() > 0) {
            jsonObject.put("roleId", id);
        }
        List<JSONObject> result1 = userService.getUserDepartsList(loginUser).getResult();
        List<String> list1 = new ArrayList<>();
        for (JSONObject json : result1) {
            String departName = json.getString("title");
            list1.add(departName);
        }
        jsonObject.put("depart", list1);


        return jsonObject;
    }

    @Override
    public Long getId(Long processId) {
        Blob blob = blobDao.findblobProcessId(processId);
        String name = blob.getName();
        return blobTempDao.findByName(name);

    }

    @Override
    public JSONObject getNumOfProcess(Long identification) {
        JSONObject json = new JSONObject();
        BlobTemp byFileId = blobTempDao.getByFileId(identification);
        String name = byFileId.getName();
        Blob blob = blobDao.getIdByName(name);
        byte[] blobValue = blob.getBlobValue();
        String s = new String(blobValue);
        JSONObject parse = JSONObject.parseObject(s);
        String nodeList = parse.getString("nodeList");
        int num = 0;
        List<JSONObject> objectList = JSONObject.parseArray(nodeList, JSONObject.class);
        for (JSONObject jsonObject : objectList) {
            if ("task".equals(jsonObject.getString("type"))) {
                num++;
            }
        }
        json.put("num", num);
        return json;
    }

    @Override
    @Transactional
    public JSONObject shutDownTask(ProcessUsersVo processVo) {
        JSONObject jsonObject = new JSONObject();
        String wareName = processVo.getWareName();


        Long processId = blobDao.selectProcessIdByWareName(wareName).getProcessId();
        System.out.println("processId=======>"+processId);
        String teachingMode = processVo.getTeachingMode();
        String planId = processVo.getPlanId();
        String lessonId = processVo.getLessonId();
        ProcessInstance processInstance ;
        List<String> loginUserList = processVo.getLoginUserList();


        //个人模式
        try {
            for (String loginUser : loginUserList) {
                if ("1".equals(teachingMode)) {

                    processInstanceDao.shutDownTask(loginUser, processId);
                } else {
                    Result<?> result = eduService.getGroupId(loginUser, planId, lessonId);
                    String groupId = result.getResult().toString();
                    if (groupId == null) {
                        log.error("该同学没有被分组");
                        jsonObject.put("error", "-1");
                        return jsonObject;
                    }
                    log.debug("该同学(" + loginUser + ")在该计划下(" + planId + ")的从课程中(" + lessonId + ")的组号为(" + groupId + ")");
                    processInstanceDao.shutDownTask(groupId, processId);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("message","终止失败");
            jsonObject.put("code",false);

        }

        jsonObject.put("message","终止成功");
        jsonObject.put("code",true);


        return jsonObject;
    }

    @Override
    public List<JSONObject> getWaresByUserId(String userId) {
        JSONObject json = eduService.selectByUserId(userId);
//        如果是管理员或者是老师，就查询所有的课件数据

        log.debug("jsonObject:{}" + json);


        List<JSONObject> jsonObjects = new ArrayList<>();
        //设计有问题，但是能跑  hhhh

        List<EduPlanDetailVo> list = JSONObject.parseArray(JSONObject.toJSONString(json.getJSONArray("list")), EduPlanDetailVo.class);
        for (EduPlanDetailVo detailVo : list) {
            String category = detailVo.getCategory();
            extracted(category,jsonObjects, detailVo);

        }

        return jsonObjects;
    }

    @Override
    public JSONObject shutDownTaskByStudent(ProcessVo processVo) {

        JSONObject jsonObject = new JSONObject();
        Long processId = processVo.getProcessId();
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();

        // 写死
        loginUser = "1437275060307296257";
        String teachingMode = processVo.getTeachingMode();
        String planId = processVo.getPlanId();
        String lessonId = processVo.getLessonId();
        ProcessInstance processInstance ;

        //个人模式
        try {
                if ("1".equals(teachingMode)) {

                    processInstanceDao.shutDownTask(loginUser, processId);
                } else {
                    Result<?> result = eduService.getGroupId(loginUser, planId, lessonId);
                    String groupId = result.getResult().toString();
                    if (groupId == null) {
                        log.error("该同学没有被分组");
                        jsonObject.put("error", "-1");
                        return jsonObject;
                    }
                    log.debug("该同学(" + loginUser + ")在该计划下(" + planId + ")的从课程中(" + lessonId + ")的组号为(" + groupId + ")");
                    processInstanceDao.shutDownTask(groupId, processId);
                }


        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("message","终止失败");
            jsonObject.put("code",false);
            return jsonObject;

        }

        jsonObject.put("message","终止成功");
        jsonObject.put("code",true);


        return jsonObject;
    }


    private void extracted(String category, List<JSONObject> jsonObjects, EduPlanDetailVo detailVo) {
        String wareName = detailVo.getWareName();
        String planId = detailVo.getPlanId();
        String lessonId = detailVo.getLessonId();
        log.debug(wareName);
        Blob blob = blobDao.selectBlobByWareId(wareName);
        byte[] blobValue = blob.getBlobValue();
        String s = new String(blobValue);
        log.debug(s);
        JSONObject parse = JSONObject.parseObject(s);
        String categories = parse.getString("category_id");
        String[] split = categories.split("-");
        List<String> categoryList = new ArrayList<>(Arrays.asList(split));
        // 课程名称
        if (categoryList.contains(category)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("wareId", blob.getId());
            jsonObject.put("wareName", blob.getName());
            int domain = categoryList.indexOf("domain");
            int type = categoryList.indexOf("type");
            int industry = categoryList.indexOf("industry");
            int end = categoryList.size();
            List<String> domains = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<String> industries = new ArrayList<>();
            for (int i = 1; i < type; i++) {
                domains.add(categoryList.get(i));
            }
            for (int i = type + 1; i < industry; i++) {
                types.add(categoryList.get(i));
            }
            for (int i = industry + 1; i < end; i++) {
                industries.add(categoryList.get(i));
            }
            jsonObject.put("domains", domains);
            jsonObject.put("types", types);
            jsonObject.put("industries", industries);
            jsonObject.put("processId", blob.getProcessId());
            jsonObject.put("teachingMode", detailVo.getTeachingMode());
            jsonObject.put("planId", planId);
            jsonObject.put("lessonId", lessonId);
            jsonObject.put("teacherName",detailVo.getTeacherName());
            jsonObject.put("lessonname",detailVo.getLessonName());

            jsonObjects.add(jsonObject);
        }
    }
}
