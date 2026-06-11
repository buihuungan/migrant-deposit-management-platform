package cn.zhenglili.fmaking.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：CheckUtils
 * @Description：TODO
 * @Author：zhenglili
 * @Date：2020/8/7 13:32
 **/
public class CheckUtils {

    private static Map<String, List<String>> typeRules = new HashMap<>();

    static {
        /**
         * 单行文本控件
         */
        List<String> inputType = new ArrayList<>();
        inputType.add("String");
        inputType.add("Date");
        inputType.add("Integer");
        inputType.add("Double");
        typeRules.put("input", inputType);

        /**
         * 多行文本控件
         */
        List<String> textareaType = new ArrayList<>();
        textareaType.add("Text");
        typeRules.put("textarea", textareaType);

        /**
         * 计数器控件
         */
        List<String> numberType = new ArrayList<>();
        numberType.add("Integer");
        typeRules.put("number", numberType);

        /**
         * 单选框组控件
         */
        List<String> radioType = new ArrayList<>();
        radioType.add("String");
        typeRules.put("radio", radioType);

        /**
         * 多选框组控件
         */
        List<String> checkboxType = new ArrayList<>();
        checkboxType.add("Text");
        typeRules.put("checkout", checkboxType);

        /**
         * 时间选择器控件
         */
        List<String> timeType = new ArrayList<>();
        timeType.add("Date");
        typeRules.put("time", timeType);

        /**
         * 日期选择器控件
         */
        List<String> dateType = new ArrayList<>();
        dateType.add("Date");
        typeRules.put("date", dateType);

        /**
         * 评分控件
         */
        List<String> rateType = new ArrayList<>();
        rateType.add("Integer");
        typeRules.put("rate", rateType);

        /**
         * 颜色选择器
         */
        List<String> colorType = new ArrayList<>();
        colorType.add("String");
        typeRules.put("color", colorType);

        /**
         * 下拉框选择器
         */
        List<String> selectType = new ArrayList<>();
        selectType.add("String");
        typeRules.put("select", selectType);


        /**
         * 开关控件
         */
        List<String> switchType = new ArrayList<>();
        switchType.add("Integer");
        typeRules.put("switch", switchType);

        /**
         * 滑块控件
         */
        List<String> sliderType = new ArrayList<>();
        sliderType.add("Integer");
        typeRules.put("slider", sliderType);

        /**
         * html控件
         */
        List<String> htmlType = new ArrayList<>();
        htmlType.add("Text");
        typeRules.put("html", htmlType);

        /**
         * 文件上传控件
         */
        List<String> fileUploadType = new ArrayList<>();
        fileUploadType.add("String");
        typeRules.put("fileupload", fileUploadType);

        /**
         * 图片上传控件
         */
        List<String> imgUploadType = new ArrayList<>();
        imgUploadType.add("String");
        typeRules.put("imgupload", imgUploadType);

        /**
         * 编辑器控件
         */
        List<String> editorType = new ArrayList<>();
        editorType.add("Text");
        typeRules.put("editor", editorType);
    }


    public static boolean checkType(String btnType, String dbType) {
        List<String> dbTypes = typeRules.get(btnType);
        return dbTypes.contains(dbType);
    }

}
