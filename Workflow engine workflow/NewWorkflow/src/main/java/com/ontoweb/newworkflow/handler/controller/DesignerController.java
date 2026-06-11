package com.ontoweb.newworkflow.handler.controller;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.core.model.BlobTemp;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.handler.dao.BlobTempDao;
import com.ontoweb.newworkflow.handler.provider.ProcessProvider;
import com.ontoweb.newworkflow.handler.service.BlobService;
import com.ontoweb.newworkflow.handler.service.DesignerService;
import com.ontoweb.newworkflow.handler.service.GetBlobTempsService;
import com.ontoweb.newworkflow.handler.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/4 下午3:11
 * @description：流程设计相关的控制器
 * <P>
 *     包含了部署、打开、删除等方法
 * </P>
 * @modified By：
 * @version: 1.0$
 */
@Api(tags = "file operation", value = "流程文件操作")
@RestController
@RequestMapping("/design")
@Slf4j
public class DesignerController {
    @Autowired
    private DesignerService designerService;
    @Autowired
    private ProcessProvider processProvider;
    @Autowired
    private GetBlobTempsService getBlobTempsService;

    @Autowired
    private BlobService blobService;

    @Autowired
    private BlobTempDao blobTempDao;


    /**
     * 流程部署，在原有基础上需要进行修改，验证、保存、部署都需要进行修改
     * @param jsonObject json参数名称是fileName
     * @return
     */
    @ApiOperation(value = "部署流程", notes = "根据文件名称部署流程")
    @PostMapping("/deploy")
    public Result deploy(@RequestBody JSONObject jsonObject) {
        Result result = new Result();
        String name = jsonObject.get("fileName").toString();

        //判断是否与uflo_blob表中的课件名称重复，若重复，表示已经部署过了，返回错误信息
        List<Blob> blobs = blobService.getListByName(name);
        if(blobs!= null && blobs.size() >0) {
            log.info("blob {}", blobs);
            log.info("blob size:{}", blobs.size());
            result.setSuccess(false);
            result.setMessage("该流程已经部署过！");
            result.setResult(false);
            return result;
        }

        Boolean deployResult = designerService.deployByFileName(jsonObject);
        result.setResult(deployResult);

        return result;
    }

    /**
     * 原本的是将工作流保存为一个文件，新的工作流可以直接存放在数据库中
     * @param jsonObject
     * <p>
     *     (需要接收三个参数)
     *     1、文件名称
     *     2、文件内容：json
     *     3、是否是更新的
     * </p>
     * @return
     */
    @ApiOperation(value = "保存流程", notes = "将产生的json保存在数据库中")
    @PostMapping("/saveFile")
    public Result saveFile(@RequestBody JSONObject jsonObject) {

        Result result = new Result();
        //判断是否与uflo_blob表中的课件名称重复，若重复，表示已经部署过了，返回错误信息
        String name = jsonObject.get("name").toString();
        boolean update = (boolean) jsonObject.get("update");
        List<Blob> blobs = blobService.getListByName(name);
        if(blobs.size() >0) {
            result.setSuccess(false);
            result.setMessage("该流程已经部署过！");
            result.setResult(false);
            return result;
        }


        //判断是否与uflo_blob_temp表中的课件名称重复，若重复，返回错误信息
        //if是ture的话，表示的是更新
        List<BlobTemp> blobTemps = blobTempDao.getListByName(name);
        if(!update) {
            if (blobTemps.size() > 0) {
                result.setSuccess(false);
                // 501 表示流程 名称重复。
                result.setCode(501);
                result.setMessage("流程名称重复！");
                result.setResult(false);
                return result;
            }
        }
        Boolean saveResult = processProvider.saveProcess(jsonObject);
        result.setResult(saveResult);

        return result;
    }

    /**
     * 获取可部署流程列表
     * @param
     * @return
     * <p>
     *     参数是json格式，后期可加上分类等内容
     * </p>
     */
    @ApiOperation(value = "获取可部署流程列表", notes = "将可以部署的blob文件展示给前端")
    @PostMapping("/getDeploy")
    public Result getBlobTemps() {
        Result result = new Result();
        List<BlobTemp> blobTempList = getBlobTempsService.getTemps();

        result.setResult(blobTempList);
        return result;
    }

    /**
     * 删除可部署的流程
     * @param
     * @return
     */

    @GetMapping("/deleteTempBlob/{id}")
    @ApiOperation("删除可部署的流程")
    public Result deleteTempBlob(@PathVariable Long id) {
        Result result = new Result();
        Boolean bool = designerService.deleteFile(id);
        if (bool == false) {
            result.setMessage("删除失败");
            result.setSuccess(false);
        }

        result.setResult(bool);

        return result;
    }

    @PostMapping("/modifyFile")
    @ApiOperation("修改流程模版")
    public Result modifyTempBlob(@RequestParam Long id) {
        Result result = new Result();

        BlobTemp blobTemp = blobTempDao.getListById(id);
        List<Blob> blobs = blobService.getListByName(blobTemp.getName());
        if(blobs.size() >0) {
            result.setSuccess(false);
        }else{
            result.setSuccess(true);
        }

        JSONObject jsonObject = designerService.modifyFile(id);

        result.setResult(jsonObject);

        return result;
    }


    @ApiOperation(value = "operate", notes = "转义工具")
    @PostMapping("/test")
    public String changeStr(@RequestParam String input) {
        String result = input.replace("\"","\\\"");

        log.info("result:{}" + result);
        return result;
    }

}
