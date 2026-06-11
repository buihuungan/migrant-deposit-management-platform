package com.ontoweb.newworkflow.core.diagram;

import java.io.Serializable;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午9:29
 * @description：
 * @modified By：
 * @version: $
 */
public class Diagram implements Serializable {
    private static final long serialVersionUID = -3750416786636570270L;
    private String name;
    private String label;
    private String fontColor="50, 50, 50";
    private int fontSize;
    private int borderWidth;
    private String borderColor;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public int getBorderWidth() {
        return borderWidth;
    }
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }
    public String getBorderColor() {
        return borderColor;
    }
    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }
    public String getFontColor() {
        return fontColor;
    }
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
    public int getFontSize() {
        return fontSize;
    }
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
