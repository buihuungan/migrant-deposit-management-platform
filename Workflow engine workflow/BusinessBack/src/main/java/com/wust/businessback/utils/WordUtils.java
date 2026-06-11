package com.wust.businessback.utils;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.microsoft.schemas.office.office.CTLock;
import com.microsoft.schemas.vml.*;
import com.wust.businessback.entity.TextWaterMark;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class WordUtils {

    //替换文本判断位
    private static Boolean tag = false;
    private static Integer index = 0;

    /**
     * 艺术字水印参数
     */
    private static final String fontName = "PingFang SC"; // word字体
    private static final int widthPerWord = 10; // 一个字平均长度，单位pt，用于：计算文本占用的长度（文本总个数*单字长度）


    /**
     * 使用占位符替换文本和图片。
     *
     * @param xwpfDocument Word文档对象
     * @param params       需要替换的文本和图片 Map，其中键为占位符，值可以是替换的文本或图片路径
     */
    public static void replaceTextAndImages(XWPFDocument xwpfDocument, Map<String, Object> params) {
        // 遍历所有段落
        xwpfDocument.getParagraphs().forEach(paragraph -> replaceInParagraph(paragraph, params));

        // 遍历所有表格
        xwpfDocument.getTables().stream()
                .flatMap(table -> table.getRows().stream())
                .flatMap(row -> row.getTableCells().stream())
                .flatMap(cell -> cell.getParagraphs().stream())
                .forEach(paragraph -> replaceInParagraph(paragraph, params));
    }

    /**
     * 在段落中替换文本或插入图片。
     *
     * @param paragraph 段落对象
     * @param params    需要替换的文本和图片 Map
     */
    private static void replaceInParagraph(XWPFParagraph paragraph, Map<String, Object> params) {

        // 遍历替换参数
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map){ // 图片插入

                //匹配替换符
                getEqualIndx(paragraph,key);
                if (tag){
                    //获取图片参数
                    Map<String, Object> photoData = (Map<String, Object>) value;
                    String imagePath = (String) photoData.get("imagePath");
                    int width = (int)photoData.get("width");  // 设置图片宽度
                    int height = (int)photoData.get("height"); // 设置图片高度
                    int leftOffset = (int)photoData.get("leftOffset");//水平偏移
                    int topOffset = (int)photoData.get("topOffset");//垂直偏移
                    int picType = getPictureType(getFileExtension(imagePath));

                    // 插入图片
                    //EMU（英制公制单位）。1 EMU = 1/914400英寸= 1/36000 mm
                    try (FileInputStream imageInputStream = new FileInputStream(imagePath)) {
                        //指定位置插入图片
                        XWPFRun newRunrun = paragraph.insertNewRun(index);
                        newRunrun.addPicture(imageInputStream, picType, imagePath, Units.toEMU(width), Units.toEMU(height));
                        // 2. 获取到图片数据
                        CTDrawing drawing = newRunrun.getCTR().getDrawingArray(0);
                        CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();
                        //拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
                        CTAnchor anchor = getAnchorWithGraphic(graphicalobject, "QrImg" + 1,
                                Units.toEMU(width), Units.toEMU(height),//图片大小
                                Units.toEMU(leftOffset), Units.toEMU(topOffset), false);//相对当前段落位置 需要计算段落已有内容的左偏移
                        drawing.setAnchorArray(new CTAnchor[]{anchor});//添加浮动属性
                        drawing.removeInline(0);//删除行内属性

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }else if(value instanceof String){
                //匹配替换符
                getEqualIndx(paragraph,key);
                if (tag){
                    XWPFRun newRunrun = paragraph.insertNewRun(index);
                    newRunrun.setText((String) value);
                }
            }
        }
    }

    /**
     * 获取文件扩展名。
     *
     * @param path 文件路径
     * @return 文件扩展名
     */
    private static String getFileExtension(String path) {
        int lastIndexOfDot = path.lastIndexOf(".");
        return (lastIndexOfDot == -1) ? "" : path.substring(lastIndexOfDot + 1);
    }

    /**
     * 根据图片类型，取得对应的图片类型代码
     *
     * @param picType 图片类型（如 "png", "jpg"）
     * @return int 图片类型代码
     */
    private static int getPictureType(String picType) {
        switch (picType.toLowerCase()) {
            case "png":
                return XWPFDocument.PICTURE_TYPE_PNG;
            case "dib":
                return XWPFDocument.PICTURE_TYPE_DIB;
            case "emf":
                return XWPFDocument.PICTURE_TYPE_EMF;
            case "jpg":
            case "jpeg":
                return XWPFDocument.PICTURE_TYPE_JPEG;
            case "wmf":
                return XWPFDocument.PICTURE_TYPE_WMF;
            default:
                return XWPFDocument.PICTURE_TYPE_PICT;
        }
    }

    /**
     * @param ctGraphicalObject 图片数据
     * @param deskFileName      图片描述
     * @param width             宽
     * @param height            高
     * @param leftOffset        水平偏移 left
     * @param topOffset         垂直偏移 top
     * @param behind            文字上方，文字下方
     * @return
     * @throws Exception
     */
    public static CTAnchor getAnchorWithGraphic(CTGraphicalObject ctGraphicalObject,
                                                String deskFileName, int width, int height,
                                                int leftOffset, int topOffset, boolean behind) {
        System.out.println(">>width>>"+width+"; >>height>>>>"+height);
        String anchorXML =
                "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                        + "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"" + ((behind) ? 1 : 0) + "\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
                        + "<wp:simplePos x=\"0\" y=\"0\"/>"
                        + "<wp:positionH relativeFrom=\"column\">"
                        + "<wp:posOffset>" + leftOffset + "</wp:posOffset>"
                        + "</wp:positionH>"
                        + "<wp:positionV relativeFrom=\"paragraph\">"
                        + "<wp:posOffset>" + topOffset + "</wp:posOffset>" +
                        "</wp:positionV>"
                        + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
                        + "<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/>"
                        + "<wp:wrapNone/>"
                        + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"" + deskFileName + "\"/><wp:cNvGraphicFramePr/>"
                        + "</wp:anchor>";

        CTDrawing drawing = null;
        try {
            drawing = CTDrawing.Factory.parse(anchorXML);
        } catch (XmlException e) {
            e.printStackTrace();
        }
        CTAnchor anchor = drawing.getAnchorArray(0);
        anchor.setGraphic(ctGraphicalObject);
        return anchor;
    }

    /**
     * 配置替换符
     * @param text 匹配的key
     */
    private static void getEqualIndx(XWPFParagraph paragraph , String text){
        List<XWPFRun> runs = paragraph.getRuns();

        int start = -1;
        int end = -1;
        String str = "";

        for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(i);
            String runText = run.getText(0);
            if (StringUtils.isEmpty(runText)) continue;
            if ('$' == runText.charAt(0) && '{' == runText.charAt(1)) start = i;
            if (start != -1) str += runText;
            if ('}' == runText.charAt(runText.length() - 1)) {
                if (start != -1 && str.equals(text)) {
                    end = i;
                    break;
                }else{
                    start = -1;
                    str = "";
                }
            }
        }

        if(str.equals(text)){
            tag = true;
            index = start;
            for (int i = start; i <= end; i++) {
                paragraph.removeRun(i);
                i--;
                end--;
            }
        }else{
            tag = false;
        }
    }

    /**
     * word文字水印(调用poi封装的createWatermark方法)
     * @param doc XWPFDocument对象
     * @param markStr 水印文字
     */
    public static void setWordWaterMark(XWPFDocument doc, String markStr,String fontColor) {
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
        if (headerFooterPolicy == null) {
            headerFooterPolicy = doc.createHeaderFooterPolicy();
        }
        headerFooterPolicy.createWatermark(markStr);
        XWPFHeader header = headerFooterPolicy.getHeader(XWPFHeaderFooterPolicy.DEFAULT);
        paragraph = header.getParagraphArray(0);
        paragraph.getCTP().newCursor();
        org.apache.xmlbeans.XmlObject[] xmlobjects = paragraph.getCTP().getRArray(0).getPictArray(0).selectChildren(
                new javax.xml.namespace.QName("urn:schemas-microsoft-com:vml", "shape"));
        if (xmlobjects.length > 0) {
            CTShape ctshape = (CTShape) xmlobjects[0];
            ctshape.setFillcolor(fontColor);
            ctshape.setStyle(ctshape.getStyle() + ";rotation:315");
        }
    }

    /**
     * 以艺术字方式加上水印(平铺)
     * @param docx XWPFDocument对象
     * @param customText 水印文字
     */
    public static void makeFullWaterMarkByWordArt(XWPFDocument docx, String customText,String fontColor,String fontSize,String styleRotation) {
        customText = customText + repeatString(" ", 16); // 水印文字之间使用8个空格分隔
        customText = repeatString(customText, 10); // 一行水印重复水印文字次数
        String styleTop = "0pt";  // 与顶部的间距

        if (docx == null) {
            return;
        }
        // 遍历文档，添加水印
        for (int lineIndex = -10; lineIndex < 20; lineIndex++) {
            styleTop = 200 * lineIndex + "pt";
            waterMarkDocXDocument(docx, customText, styleTop, 1,fontColor, fontSize ,styleRotation);
        }
    }

    /**
     * 以艺术字方式加上水印(单个)
     * @param docx XWPFDocument对象
     * @param customText 水印文字
     */
    public static void makeWaterMarkByWordArt(XWPFDocument docx, String customText,String fontColor,String fontSize,String rotation) {
        String styleTop = "100pt";  // 与顶部的间距

        if (docx == null) {
            return;
        }
        // 添加水印
        waterMarkDocXDocument(docx, customText, styleTop, 2,fontColor,fontSize,rotation);
    }

    /**
     * 将指定的字符串重复repeats次.
     * @param pattern 字符串
     * @param repeats 重复次数
     * @return 生成的字符串
     */
    private static String repeatString(String pattern, int repeats) {
        StringBuilder buffer = new StringBuilder(pattern.length() * repeats);
        Stream.generate(() -> pattern).limit(repeats).forEach(buffer::append);
        return new String(buffer);
    }

    /**
     * 为文档添加水印
     * 实现参考了{@link XWPFHeaderFooterPolicy#(String, int)}
     * @param doc 需要被处理的docx文档对象
     * @param customText 水印文本
     * @param type 类型：1.平铺；2.单个
     */
    private static void waterMarkDocXDocument(XWPFDocument doc, String customText, String styleTop, int type,String fontColor,String fontSize,String rotation) {
        XWPFHeader header = doc.createHeader(HeaderFooterType.DEFAULT); // 如果之前已经创建过 DEFAULT 的Header，将会复用之
        int size = header.getParagraphs().size();
        if (size == 0) {
            header.createParagraph();
        }
        CTP ctp = header.getParagraphArray(0).getCTP();
        byte[] rsidr = doc.getDocument().getBody().getPArray(0).getRsidR();
        byte[] rsidrdefault = doc.getDocument().getBody().getPArray(0).getRsidRDefault();
        ctp.setRsidP(rsidr);
        ctp.setRsidRDefault(rsidrdefault);
        CTPPr ppr = ctp.addNewPPr();
        ppr.addNewPStyle().setVal("Header");
        // 创建文本框形状对象
        CTR ctr = ctp.addNewR();
        CTRPr ctrpr = ctr.addNewRPr();
        ctrpr.addNewNoProof();
        CTGroup group = CTGroup.Factory.newInstance();
        CTShapetype shapetype = group.addNewShapetype();
        CTTextPath shapeTypeTextPath = shapetype.addNewTextpath();
        shapeTypeTextPath.setOn(STTrueFalse.T);
        shapeTypeTextPath.setFitshape(STTrueFalse.T);
        // 通过 shapetype 设置锁定属性
        CTLock lock = shapetype.addNewLock();
        lock.setExt(STExt.VIEW);
        CTShape shape = group.addNewShape();
        shape.setId("PowerPlusWaterMarkObject");
        shape.setSpid("_x0000_s102");
        shape.setType("#_x0000_t136");
        if(type != 2){
            shape.setStyle(getShapeStyle(customText, styleTop,rotation)); // 设置形状样式（旋转，位置，相对路径等参数）
        }else{
            shape.setStyle(getShapeStyle()); // 设置形状样式（旋转，位置，相对路径等参数）
        }
        shape.setFillcolor(fontColor);
        shape.setStroked(STTrueFalse.FALSE); // 字体设置为实心
        CTTextPath shapeTextPath = shape.addNewTextpath(); // 绘制文本的路径
        shapeTextPath.setStyle("font-family:" + fontName + ";font-size:" + fontSize); // 设置文本字体与大小
        shapeTextPath.setString(customText);
        CTPicture pict = ctr.addNewPict();
        pict.set(group);
    }

    /**
     * 构建Shape的样式参数
     * @param customText 水印文本
     * @return
     */
    private static String getShapeStyle(String customText, String styleTop,String styleRotation) {
        StringBuilder sb = new StringBuilder();
        sb.append("position: ").append("absolute"); // 文本path绘制的定位方式
        sb.append(";width: ").append(customText.length() * widthPerWord).append("pt"); // 计算文本占用的长度（文本总个数*单字长度）
        sb.append(";height: ").append("20pt"); // 字体高度
        sb.append(";z-index: ").append("-251654144");
        sb.append(";mso-wrap-edited: ").append("f");
        sb.append(";margin-top: ").append(styleTop);
        sb.append(";mso-position-horizontal-relative: ").append("margin");
        sb.append(";mso-position-vertical-relative: ").append("margin");
        sb.append(";mso-position-vertical: ").append("left");
        sb.append(";mso-position-horizontal: ").append("center");
        sb.append(";rotation: ").append(styleRotation);
        return sb.toString();
    }

    /**
     * 构建Shape的样式参数
     * @return
     */
    private static String getShapeStyle() {
        StringBuilder sb = new StringBuilder();
        sb.append("position: ").append("absolute"); // 文本path绘制的定位方式
        sb.append(";left: ").append("opt");
        sb.append(";width: ").append("500pt"); // 计算文本占用的长度（文本总个数*单字长度）
        sb.append(";height: ").append("150pt"); // 字体高度
        sb.append(";z-index: ").append("-251654144");
        sb.append(";mso-wrap-edited: ").append("f");
        sb.append(";margin-left: ").append("-50pt");
        sb.append(";margin-top: ").append("270pt");
        sb.append(";mso-position-horizontal-relative: ").append("margin");
        sb.append(";mso-position-vertical-relative: ").append("margin");
        sb.append(";mso-width-relative: ").append("page");
        sb.append(";mso-height-relative: ").append("page");
        sb.append(";rotation: ").append("-2949120f");
        return sb.toString();
    }

    /**
     * doc转换为docx
     *
     * @param doc doc类型的文档
     * @return int 图片类型代码
     */
    public static XWPFDocument convertDocToDocx(HWPFDocument doc) {
        Range range = doc.getRange();
        // 创建新的 docx 文档
        XWPFDocument docx = new XWPFDocument();

        // 遍历原 doc 中的段落并添加到 docx
        for (int i = 0; i < range.numParagraphs(); i++) {
            XWPFParagraph paragraph = docx.createParagraph();
            paragraph.createRun().setText(range.getParagraph(i).text());
        }
        return docx;
    }

    /**
     * 通过documents4j 实现word转pdf
     *
     * @param sourcePath 源文件地址 如 /root/example.doc
     * @param targetPath 目标文件地址 如 /root/example.pdf
     */
    public static void documents4jWordToPdf(String sourcePath, String targetPath)  {
        File inputWord = new File(sourcePath);
        File outputFile = new File(targetPath);
        try {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            boolean execute = converter.convert(docxInputStream)
                    .as(DocumentType.DOCX)
                    .to(outputStream)
                    .as(DocumentType.PDF).schedule().get();
            outputStream.close();
            docxInputStream.close();

//            log.info("转换完毕 targetPath = {}", outputFile.getAbsolutePath());
            System.out.println("转换完毕 targetPath = " + outputFile.getAbsolutePath());
            converter.shutDown();
        } catch (Exception e) {
//            log.error("[documents4J] word转pdf失败:{}", e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 通过documents4j 实现word转pdf
     *
     * @param sourcePath 源文件地址 如 /root/example.doc
     * @param targetPath 目标文件地址 如 /root/example.pdf
     */
    public static void waterMarkPDF(String sourcePath, String targetPath, TextWaterMark waterMarkInfo) {
        try {
            // 读取原始 PDF 文件
            PdfReader reader = new PdfReader(sourcePath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(targetPath));

            // 获取 PDF 中的页数
            int pageCount = reader.getNumberOfPages();

            // 添加水印
            // 遍历每一页添加水印
            for (int i = 1; i <= pageCount; i++) {
                PdfContentByte contentByte = stamper.getUnderContent(i); // 获取底层内容，覆盖水印

                // 获取页面的宽度和高度
                float pageWidth = reader.getPageSize(i).getWidth();
                float pageHeight = reader.getPageSize(i).getHeight();

                // 水印的文本内容
                String watermarkText = waterMarkInfo.getWaterText();

                // 设置水印字体和大小
                contentByte.beginText();
                BaseFont baseFont = BaseFont.createFont("./opt/fonts/STXIHEI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                contentByte.setFontAndSize(baseFont, waterMarkInfo.getWaterSize());
                contentByte.setColorFill(waterMarkInfo.getWaterColor()); // 浅灰色

                // 水印的横向和纵向间距（根据页面大小调整）
                float xSpacing = waterMarkInfo.getXSpacing(); // 水平间距
                float ySpacing = waterMarkInfo.getYSpacing(); // 垂直间距

                // 水印旋转角度
                float rotation = waterMarkInfo.getWaterRotation();

                // 计算水印的排布，确保覆盖整个页面
                for (float x = 0; x < pageWidth; x += xSpacing) {
                    for (float y = 0; y < pageHeight; y += ySpacing) {
                        contentByte.showTextAligned(Element.ALIGN_CENTER, watermarkText, x + xSpacing / 2, y + ySpacing / 2, rotation);
                    }
                }

                contentByte.endText();
            }

            // 保存修改后的 PDF 文件并关闭文件流
            stamper.flush();
            stamper.close();
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (DocumentException e){
            e.printStackTrace();
        }
    }

}
