package com.wust.businessback.service.impl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.util.PoitlIOUtils;
import com.wust.businessback.entity.TextWaterMark;
import com.wust.businessback.service.PoiService;
import com.wust.businessback.utils.WordUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

@Service
public class PoiServiceImpl implements PoiService {

//    //默认水印文本
//    private final static String DEFAULT_WATERMARK = "武汉科技大学计算机学院";
//    private final static String DEFAULT_FONT_COLOR = "#d8d8d8";
//
//    // 字体大小
//    private static final String FONT_SIZE = "0.5pt";
//    // 文本旋转角度
//    private static final String STYLE_ROTATION = "-30";




    @Override
    public Boolean exportWord(Map<String,Object> mapText, Map<String,Object> mapPicture, TextWaterMark waterMarkInfo){
        boolean result = false;
//        //设置水印默认值
//        String watermark = StringUtils.isBlank(waterMarkInfo.getWaterText()) ? DEFAULT_WATERMARK : waterMarkInfo.getWaterText();
//        String color = StringUtils.isBlank(waterMarkInfo.getFontColor()) ? DEFAULT_FONT_COLOR : "#" + waterMarkInfo.getFontColor();
//        String fontSize = (null == waterMarkInfo.getFontSize()) ? FONT_SIZE : waterMarkInfo.getFontSize() + "pt";
//        String rotation = (null == waterMarkInfo.getRotation()) ? STYLE_ROTATION : "-" + waterMarkInfo.getRotation();
//
        String modelPath = "./opt/word/附件1：农民工工资保证金存储通知书（样本）.docx";
        String docxPath = "./opt/output.docx";
        String pdfPath = "./opt/output.pdf";
        String finalPath = "./opt/finalPath.pdf";

        //使用poi-tl
        try {
            //使用el 表达式配置
            ConfigureBuilder configureBuilder = Configure.builder().useSpringEL();
            Configure config = configureBuilder.build();

            //多选框操作 文本操作
            XWPFTemplate template = XWPFTemplate.compile(modelPath,config).render(mapText);
            FileOutputStream out1 = new FileOutputStream(docxPath);
            template.write(out1);

            //关闭poi-tl资源
            out1.close();
            out1.flush();
            PoitlIOUtils.closeQuietlyMulti(template, out1);

            //让程序生成文档
//            Thread.sleep(500);
        }catch (Exception e){
            e.printStackTrace();
        }

        //原生poi操作
        try {
            XWPFDocument xwpfDocument = new XWPFDocument(new FileInputStream(docxPath));
            //图片替换
            WordUtils.replaceTextAndImages(xwpfDocument, mapPicture);
//            //word添加水印
//            WordUtils.makeFullWaterMarkByWordArt(xwpfDocument, watermark, color, fontSize, rotation);
            FileOutputStream out2 = new FileOutputStream(docxPath);
            xwpfDocument.write(out2);

            //关闭poi资源
            out2.close();
            out2.flush();
            xwpfDocument.close();

            //让程序生成文档
//            Thread.sleep(500);
        }catch (Exception e){
            e.printStackTrace();
        }

        //doc 转 pdf
        WordUtils.documents4jWordToPdf(docxPath, pdfPath);
        //pdf 添加水印
        WordUtils.waterMarkPDF(pdfPath,finalPath,waterMarkInfo);

        return result;
    }
}
