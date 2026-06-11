//package cn.zhenglili.fmaking.desform.service.impl;
//
//import cn.zhenglili.fmaking.desform.dao.GenerateStructureDTO;
//import cn.zhenglili.fmaking.desform.mapper.AutoNumberMapper;
//import cn.zhenglili.fmaking.desform.service.AutoNumberService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * @author ：Ethan yan
// * @date ：Created in 2021/1/15 下午4:28
// * @description：自动编号的视线类
// * @modified By：
// * @version: 1.0$
// */
//@Service
//@Slf4j
//public class AutoNumberServiceImpl implements AutoNumberService {
//
//    @Resource
//    private AutoNumberMapper autoNumberMapper;
//
//    @Override
//    public String getGenerateCode(GenerateStructureDTO generateStructureDTO) {
//
//        String generateCode = generateStructureDTO.getGenerateCode();
//        int codeLength = generateCode.length();
//
//        //获取数据表中最新的一条编号记录
//        String topFieldValue = autoNumberMapper.getTopFieldValue(generateStructureDTO.getTableName(), generateStructureDTO.getFieldName());
//
//        String dateTime = new SimpleDateFormat("yyyyMMdd").format(new Date());
//
//        //当天的第一个号码
//        String initNumber = generateCode + "-" + dateTime + "-" + "0001";
//
//        //不为空
//        if (topFieldValue != null && !"".equals(topFieldValue)) {
//            //获取日期信息
//            String dateTimeInfo = topFieldValue.substring(codeLength + 1, 9 + codeLength);
//            //为当天日期
//            if (dateTime.equals(dateTimeInfo)) {
//                //获取编号
//                String number = topFieldValue.substring(10 + codeLength);
//                log.info("number{}" + number);
//                initNumber = generateCode + "-" + dateTime + "-" + String.format("%04d", Integer.parseInt(number) + 1);
//            }
//        }
//
//        return initNumber;
//    }
//}
