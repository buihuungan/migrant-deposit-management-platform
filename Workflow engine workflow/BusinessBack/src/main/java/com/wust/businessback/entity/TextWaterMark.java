package com.wust.businessback.entity;

import com.itextpdf.text.BaseColor;
import lombok.Data;

@Data
public class TextWaterMark {
    //水印文本
    private String waterText = "武汉科技大学计算机学院";
    //字体颜色
    private String fontColor;
    //字体大小
    private String fontSize;
    //旋转角度
    private String rotation;


    //iText JAR包里面的字体颜色设置
    private BaseColor waterColor = BaseColor.LIGHT_GRAY;
    //iText JAR包里面的字体大小设置
    private float waterSize = 36f;
    //iText JAR包里面的旋转角度
    private float waterRotation = 45f;
    //iText JAR包里面的水平间距
    private float xSpacing = 600f;
    //iText JAR包里面的垂直间距
    private float ySpacing = 400f;

}
