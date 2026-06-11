package com.ontoweb.newworkflow.handler.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ontoweb.newworkflow.handler.VO.ProcessUsersVo;
import com.ontoweb.newworkflow.handler.VO.ProcessVo;
import com.ontoweb.newworkflow.handler.entity.FilePath;
import com.ontoweb.newworkflow.handler.service.BusinessService;
import com.ontoweb.newworkflow.handler.service.FilePathService;
import com.ontoweb.newworkflow.handler.utils.CommonUtils;
import com.ontoweb.newworkflow.handler.utils.Result;
import com.ontoweb.newworkflow.handler.utils.oConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/11/22 下午3:03
 * @description：一些业务处理方法
 * @modified By：
 * @version: $
 */
@Api(tags = "business operation", value = "一些业务处理方法")
@RestController
@RequestMapping("/business")
@Slf4j
public class BusinessController {
    @Autowired
    WebApplicationContext applicationContext;
    @Autowired
    private BusinessService businessService;
    @Resource
    private FilePathService filePathService;
    @Value(value = "${upload.path.upload}")
    private String uploadpath;

    @ApiOperation("getAllUrl")
    @RequestMapping(value = "/getAllURL", method = RequestMethod.POST)
    public Object getAllURL() {
        List<Map<String, String>> resultList = new ArrayList<>();

        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> mappingInfoHandlerMethodEntry : map.entrySet()) {
            Map<String, String> resultMap = new LinkedHashMap<>();

            RequestMappingInfo requestMappingInfo = mappingInfoHandlerMethodEntry.getKey();
            HandlerMethod handlerMethod = mappingInfoHandlerMethodEntry.getValue();

            // 类名
            resultMap.put("className",handlerMethod.getMethod().getDeclaringClass().getName());
            Annotation[] parentAnnotations = handlerMethod.getBeanType().getAnnotations();
            for (Annotation annotation : parentAnnotations) {
                if (annotation instanceof Api) {
                    Api api = (Api) annotation;
                    resultMap.put("classDesc",api.value());
                } else if (annotation instanceof RequestMapping) {
                    RequestMapping requestMapping = (RequestMapping) annotation;
                    if (null != requestMapping.value() && requestMapping.value().length > 0) {
                        //类URL
                        resultMap.put("classURL",requestMapping.value()[0]);
                    }
                }
            }
            // 方法名
            resultMap.put("methodName", handlerMethod.getMethod().getName());
            Annotation[] annotations = handlerMethod.getMethod().getDeclaredAnnotations();
            if (annotations != null) {
                // 处理具体的方法信息
                for (Annotation annotation : annotations) {
                    if (annotation instanceof ApiOperation) {
                        ApiOperation methodDesc = (ApiOperation) annotation;
                        String desc = methodDesc.value();
                        //接口描述
                        resultMap.put("methodDesc",desc);
                    }
                }
            }
            PatternsRequestCondition p = requestMappingInfo.getPatternsCondition();
            for (String url : p.getPatterns()) {
                //请求URL
                resultMap.put("methodURL",url);
            }
            RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                //请求方式：POST/PUT/GET/DELETE
                resultMap.put("requestType",requestMethod.toString());
            }
            resultList.add(resultMap);
        }


        return JSONArray.parseArray(JSONObject.toJSONString(resultList));
    }

    @ApiOperation("获取课件")
    @GetMapping("/getWares/{identification}")
    public Result getWareDomainWares(@PathVariable String identification){
        Result result = new Result();
        List<JSONObject> list = businessService.getLists(identification);

        result.setResult(list);
        result.setSuccess(true);

        return result;
    }

    /**
     * 根据类别获取课件，前提是该课程已经部署了，参数是传递的领域或者类型
     * @return
     */
    @CrossOrigin
    @ApiOperation("根据类别获取课件")
    @GetMapping("/getByCategory/{category}/{userId}")
    public Result getWares(@PathVariable("category") String category,@PathVariable("userId") String userId){
        Result result = new Result();
        List<JSONObject> jsonObjects = null;

        jsonObjects = businessService.getWaresByCategory(category,userId);

        result.setResult(jsonObjects);
        result.setSuccess(true);

        return result;
    }

    /**
     * 参数是流程实例ID
     * @return
     */
    @ApiOperation("获取进度条数值")
    @GetMapping("/percentage/{processInstanceId}")
    public Result getPercentage(@PathVariable long processInstanceId) {
        Result result = new Result();
        Double percentage = businessService.getPercentageByProceeeInsId(processInstanceId);
        result.setResult(percentage);
        result.setSuccess(true);

        return result;
    }

    @ApiOperation("开启课程")
    @PostMapping("/startClass/{wareId}")
    public Result StartClass(@RequestBody ProcessVo processVo,@PathVariable("wareId") String wareId) {
        /*
        processId 为流程id
        wareId 为 uflo_blob表中的Id字段 ，为流程id uflo_blob表中为已部署的 流程
         */
        Result result = new Result();
        JSONObject resultJson = businessService.startClassByProcessId(processVo,wareId);
        result.setResult(resultJson);
        result.setSuccess(true);

        return result;
    }

    @ApiOperation("终止此次的操作，就是将之前的该同学的操作进行删除")
    @PostMapping("shutDownTask/ByStudent")
    @Transactional
    public Result<?> shutDownTaskByStudent(@RequestBody ProcessVo processVo){
        Result<Object> result = new Result<>();
        JSONObject jsonObject = businessService.shutDownTaskByStudent(processVo);
        Boolean code = jsonObject.getBoolean("code");
        String message = jsonObject.getString("message");
        if (code){
            result.setSuccess(true);
            result.setMessage(message);
        }else {
            result.setSuccess(false);
            result.setMessage(message);
        }

        return result;

    }

    @ApiOperation("终止此次的操作，就是将之前的该同学的操作进行删除")
    @PostMapping("shutDownTask/byTeacher")
    public Result<?> shutDownTaskByteacher(@RequestBody ProcessUsersVo processVo){
        Result<Object> result = new Result<>();
        JSONObject jsonObject = businessService.shutDownTask(processVo);
        Boolean code = jsonObject.getBoolean("code");
        String message = jsonObject.getString("message");
        if (code){
            result.setSuccess(true);
            result.setMessage(message);
        }else {
            result.setSuccess(false);
            result.setMessage(message);
        }

        return result;

    }

    @ApiOperation("获取最新任务ID")
    @PostMapping("/getNewListTaskId")
    public Result getNewListTaskId(@RequestBody ProcessVo processVo) {
        Result result = new Result();
        String taskId = businessService.getTaskId(processVo);
        result.setResult(taskId);
        result.setSuccess(true);

        return result;
    }

    /**
     * json传递html代码就行
     * @param jsonObject
     * @return
     */
    @ApiOperation("保存课程描述")
    @PostMapping("/saveWareDesc")
    public Result saveWareDesc(@RequestBody JSONObject jsonObject) {
        Result result = new Result();
        Boolean bool = businessService.saveDesc(jsonObject);
        result.setSuccess(bool);
        return  result;
    }

    @ApiOperation("根据id获取课程描述")
    @GetMapping("/getDescById/{id}")
    public Result getDescById(@PathVariable long id) {
        Result result = new Result();
        String desc = businessService.getDescById(id);
        result.setResult(desc);
        result.setSuccess(true);

        return result;
    }

    @ApiOperation("获取课程描述")
    @PostMapping("/getWareDesc/{processId}")
    public Result getWareDesc(@PathVariable long processId) {
        Result result = new Result();
        String str = businessService.getDesc(processId);
        result.setResult(str);
        result.setSuccess(true);

        return  result;
    }

    @ApiOperation("cms获取课程进度条")
    @PostMapping("/getPercentageFromCMS")
    public Result getPercentageFromCMS(@RequestBody ProcessVo processVo) {
        Result result = new Result();
        Double d = businessService.getPercentagecms(processVo);
        result.setResult(d);
        result.setSuccess(true);

        return result;
    }

    @ApiOperation("获取默认信息")
    @PostMapping("/getDefaultInformation")
    public Result getDefaultInformation(@RequestBody ProcessVo processVo) {
        Result result = new Result();
        JSONObject jsonObject = businessService.getDefault(processVo);
        result.setResult(jsonObject);
        result.setSuccess(true);

        return result;
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getRoleId/{processInstanceId}")
    public Result getRoleIdByUserId(@PathVariable long processInstanceId) {
        Result result = new Result();
        JSONObject jsonObject = businessService.getRoleId(processInstanceId);
        result.setSuccess(true);
        result.setResult(jsonObject);

        return result;
    }

    @ApiOperation("获取该课件的相关信息")
    @GetMapping("/getNumOfProcess/{identification}")
    public Result<?> getNumOfProcess(@PathVariable Long identification){
        Result<Object> result = new Result<>();
        JSONObject jsonObject = businessService.getNumOfProcess(identification);
        result.setResult(jsonObject);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation("查看该用户所有选择的模型课")
    @GetMapping("/getByCategory/{userId}")
    public Result getWaresByUserId(@PathVariable("userId") String userId){
        Result result = new Result();
        List<JSONObject> jsonObjects = businessService.getWaresByUserId(userId);
        result.setResult(jsonObjects);
        result.setSuccess(true);

        return result;
    }



    /**
     * 本地文件上传
     *
     * @param multipartFile 文件
     * @param path          自定义路径
     * @return
     */
    @PostMapping("/uploadLessonFile")
    public Result<?> uploadLocal(@RequestParam("file") MultipartFile multipartFile, @RequestParam(required = false, defaultValue = "/",name = "path") String path,@RequestParam("lessonId") String lessonId) {

        FilePath name = filePathService.selectFileIfExist(lessonId);
        Result<Object> result = new Result<>();


        Map<String, String> map = new HashMap<>();

        try {
            String ctxPath = uploadpath;
            String fileName = null;
            File file = new File(ctxPath + File.separator + path + File.separator);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            String orgName = multipartFile.getOriginalFilename();// 获取文件名
            orgName = CommonUtils.getFileName(orgName);
            if (orgName.contains(".")) {
                fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            } else {
                fileName = orgName + "_" + System.currentTimeMillis();
            }
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(multipartFile.getBytes(), savefile);
            String dbpath = null;
            if (oConvertUtils.isNotEmpty(path)) {
                dbpath = path + File.separator + fileName;
            } else {
                dbpath = fileName;
            }
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            map.put("fileName", fileName);
            map.put("dbpath", dbpath);
            System.out.println(map);
            FilePath filePath = new FilePath();
            filePath.setFileName(ctxPath+File.separator+fileName);
            filePath.setLessonId(lessonId);
            if (name!=null) {
                filePath.setId(name.getId());
                filePathService.updateById(filePath);
                result.setCode(201);
                result.setMessage("更新成功");
            }else {
                filePathService.save(filePath);
                result.setCode(200);
                result.setResult("添加成功");
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return result;

    }

    @GetMapping("/downLoadLessonFile")
    public String downLoad(HttpServletRequest request, HttpServletResponse response, @RequestParam("processId") Long processId) throws UnsupportedEncodingException {

        Long id = businessService.getId(processId);
        String name = filePathService.getOne(new LambdaQueryWrapper<FilePath>().eq(FilePath::getLessonId,id)).getFileName();
        System.out.println(name);

        File file = new File(name);
        String substring = name.substring(name.lastIndexOf("/") + 1);

        String fileName = substring.split("_")[0]+substring.substring(substring.lastIndexOf("."));
        fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

        response.setHeader("content-type", "application/octet-stream");
        // 设置强制下载不打开
        response.setContentType("application/force-download");
        // 设置文件名
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

        byte[] buffer = new byte[1024];
        InputStream fis = null;
        BufferedInputStream bis = null;

        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "文件下载成功";
    }
//
//    @PostMapping("/test")
//    public Result<?> test(@RequestBody JSONObject jsonObject){
//        List<JSONObject> test = businessService.test(jsonObject);
//        return Result.ok(test);
//    }

}
