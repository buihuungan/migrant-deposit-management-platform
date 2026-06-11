//package cn.zhenglili.fmaking.desform.controller;
//
//import cn.zhenglili.fmaking.desform.dao.GenerateStructureDTO;
//import cn.zhenglili.fmaking.desform.service.AutoNumberService;
//import cn.zhenglili.fmaking.utils.Result;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
///**
// * @author ：Ethan yan
// * @date ：Created in 2021/1/15 下午3:12
// * @description：按照规则自增
// * @modified By：
// * @version: $1.0
// */
//@RestController
//@Slf4j
//@RequestMapping("/aotoGenerate")
//public class AutoNumberController {
//    @Resource
//    private AutoNumberService autoNumberService;
//
//    @PostMapping("/getAutoNumer")
//    public Result<?> getAutoNumer(@RequestBody GenerateStructureDTO generateStructureDTO) {
//        Result result = new Result<>();
//
//        String generateCode = autoNumberService.getGenerateCode(generateStructureDTO);
//        result.setResult(generateCode);
//
//        return result;
//    }
//}
