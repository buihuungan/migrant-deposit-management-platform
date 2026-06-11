package cn.zhenglili.fmaking.desform.controller;

import cn.zhenglili.fmaking.desform.entity.DesForm;
import cn.zhenglili.fmaking.desform.entity.DesFormQuery;
import cn.zhenglili.fmaking.desform.service.DesFormService;
import cn.zhenglili.fmaking.utils.Result;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：DesFormController
 * @Description：TODO
 * @Author：zhenglili
 * @Date：2020/7/29 15:42
 **/
@Api(description = "表单设计器")
@Slf4j
@RestController
@RequestMapping("/admin/desform")
public class DesFormController {
    @Autowired
    private DesFormService desFormService;

    /**
     * 根据id查询表单设计content
     *
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询表单设计content")
    @GetMapping("/{id}/getConent")
    public Result<?> getConent(@PathVariable @ApiParam("表单ID") String id) {
        log.info("enter into /desform/" + id + "/getConent");
        Result result = new Result();
        DesForm desForm = desFormService.findOneById(id);
        if (desForm == null) {
            return Result.error(500, "表单不存在");
        }
        result.setSuccess(true);
        result.setResult(desForm.getContent());
        return result;
    }

    /**
     * 根据id 查询表单基础信息+content
     *
     * @param id
     * @return
     * @PathVariable：用于get请求把url地址中对应的部分转化为参数传入函数
     */
    @ApiOperation("根据id查询表单设计全部信息")
    @GetMapping("/{id}/getOne")
    public Result<?> getOne(@PathVariable @ApiParam("表单ID") String id) {
        log.info("enter into /desform/" + id + "/getOne");
        Result result = new Result();
        DesForm desForm = desFormService.findOneById(id);
        if (desForm == null) {
            return Result.error(500, "表单不存在");
        }
        result.setSuccess(true);
        result.setResult(desForm);
        return result;
    }

    /**
     * 返回表单列表信息(分页)，可模糊查询
     */
    @ApiOperation("分页查询表单基本信息+模糊查询")
    @PostMapping("search")
    public Result<?> search(DesFormQuery desFormQuery,
                            @RequestParam(value = "current", defaultValue = "1") @ApiParam("当前页数") Integer pageNo,
                            @RequestParam(value = "size", defaultValue = "20") @ApiParam("页面大小") Integer pageSize,
                            @RequestParam(value = "userId", defaultValue = "c6d7cb4deeac411cb3384b1b31278596") String userId,
                            HttpServletRequest request) {
        log.info("enter into /desform/search");
        log.info("parameter:" + desFormQuery.toString());
        log.info("/pageNo:" + pageNo + "/pageSize:" + pageSize + "/userId:" + request.getHeader("userId"));
        Result result = new Result();
//        userId = request.getHeader("userId");
        String tenantId = request.getHeader("tenant_id");
//        tenantId = "0";
//        userId = "c6d7cb4deeac411cb3384b1b31278596";
        if (StringUtils.isEmpty(userId)) {
            return Result.error(500, "用户Id不可为空");
        }
        List<DesForm> list = desFormService.desFormList(desFormQuery, userId, pageNo, pageSize, tenantId);
        Map<String, Object> map = new HashMap<>();
//        List<DesForm> list = desFormService.desFormList(desFormQuery, userId, pageNo, pageSize);
        map.put("list", list);
        Integer total = desFormService.count(userId);
        map.put("total", total);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }

    /**
     * 返回表单列表信息
     */
    @ApiOperation("查询表单基本信息")
    @PostMapping("searchAll")
    public Result<?> searchAll(DesFormQuery desFormQuery,
                            @RequestParam(value = "userId", defaultValue = "c6d7cb4deeac411cb3384b1b31278596") String userId,
                            HttpServletRequest request) {

        Result result = new Result();
        String tenantId = request.getHeader("tenant_id");
        if (StringUtils.isEmpty(userId)) {
            return Result.error(500, "用户Id不可为空");
        }
        List<DesForm> list = desFormService.allDesFormList(desFormQuery, userId, tenantId);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        Integer total = desFormService.count(userId);
        map.put("total", total);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }


    /**
     * 新增表单设计基础信息
     *
     * @param
     * @return
     */
    @ApiOperation("创建表单设计基本信息")
    @PostMapping("/create")
    public Result create(@RequestParam @ApiParam(value = "表单设计名称", required = true) String name,
                         @RequestParam @ApiParam(value = "表单设计编码", required = true) String encoding,
                         @RequestParam @ApiParam(value = "表单设计图标", required = true) String icon,
                         @RequestParam(value = "userId", defaultValue = "c6d7cb4deeac411cb3384b1b31278596") String userId,
                         HttpServletRequest request) {
        log.info("enter into /desform/create");
        log.info("name:" + name + "/encoding:" + encoding + "/icon:" + icon + "/userId:" + request.getHeader("userId"));
//        String userId = request.getHeader("userId");
        String tenantId = request.getHeader("tenant_id");
        if (StringUtils.isEmpty(userId)) {
            return Result.error(500, "userId为空");
        }
        DesForm desForm = new DesForm(name, encoding, icon, userId,tenantId);
//        DesForm desForm = new DesForm(name, encoding, icon, userId);
        Integer cnt = desFormService.createDesForm(desForm);
        if (cnt != 1) {
            return Result.error(500, "新增失败");
        }
        return Result.ok("新增成功");
    }

    /**
     * 更新表单设计基础信息
     */
    @ApiOperation("更新表单设计基本信息")
    @PutMapping("update")
    public Result update(@RequestParam @ApiParam(value = "表id", required = true) String id,
                         @RequestParam @ApiParam(value = "表单设计名称", required = true) String name,
                         @RequestParam @ApiParam(value = "表单设计编码", required = true) String encoding,
                         @RequestParam @ApiParam(value = "表单设计图标", required = true) String icon
    ) {
        log.info("enter into /desform/update");
        log.info("id:" + id + "/name:" + name + "/encoding:" + encoding + "/icon:" + icon);
//        DesForm desForm = new DesForm(name, encoding, icon, null,null);
        DesForm desForm = new DesForm(name, encoding, icon, null);
        desForm.setId(id);
        Integer cnt = desFormService.updateDesForm(desForm);
        return cnt != 1 ? Result.error(500, "更新失败") : Result.ok("更新成功");
    }

    /**
     * 更新设计表单content
     */
    @ApiOperation("更新表单设计content")
    @PostMapping("/updateContent")
    public Result updateContent(@RequestParam @ApiParam(value = "表单ID", required = true) String id,
                                @RequestBody @ApiParam(value = "表单设计content", required = true) JSONObject jsonObject) {
        String content = jsonObject.toJSONString();
        log.info("enter into /desform/updateContent");
        log.info("id:" + id + "/content:" + content);
        return desFormService.updateDesFormContent(id, content);
    }

    @ApiOperation("更新表单设计deleteFlag")
    @PostMapping("/updateDeleteFlag")
    public Result updateDeleteFlag(@RequestParam @ApiParam(value = "表单ID", required = true) String id,
                                   @RequestParam @ApiParam(value = "删除标识", required = true) int deleteFlag) {
        log.info("enter into /desform/updateDeleteFlag" + "///id:" + id + "/deleteFlag:" + deleteFlag);
        Integer cnt = desFormService.updateDeleteFlag(id, deleteFlag);
        return cnt != 1 ? Result.error(500, "修改删除标识失败") : Result.ok("更新删除标识成功");
    }

    /**
     * 删除表单设计
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id删除表单设计")
    @DeleteMapping("/{id}/delete")
    public Result delete(@PathVariable String id) {
        log.info("enter into /desform/" + id + "/delete");
        Integer cnt = desFormService.deleteDesForm(id);
        return cnt != 1 ? Result.error(500, "删除失败") : Result.ok("删除成功");
    }

    /**
     * 批量删除表单设计
     */
    @ApiOperation("批量删除表单设计")
    @DeleteMapping("/ids/delete")
    public Result delete(@RequestBody @ApiParam(value = "ID列表", required = true) JSONObject jsonIds) {
        log.info("enter into /desform/ids/delete" + "/ids=" + jsonIds);
        JSONArray strIds = jsonIds.getJSONArray("ids");
        List<String> ids = strIds.toJavaList(String.class);
        Integer cnt = desFormService.deleteDesFormByIds(ids);
        return Result.ok("删除" + cnt + "条表单设计数据");
    }

    /**
     * 校验content
     */
    @PostMapping("/checkContent")
    public Result checkContent(@RequestBody JSONObject jsonObject) {
        log.info("enter into admin/desform/checkContent");
        try {
            return desFormService.checkContent(jsonObject);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }


}
