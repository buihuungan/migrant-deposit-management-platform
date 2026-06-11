package com.ontoweb.newworkflow.core.diagram;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午9:28
 * @description：流程图形
 * @modified By：
 * @version: $
 */
public class ProcessDiagram extends Diagram implements Cloneable{
    private static final long serialVersionUID = 4764263010367898866L;
    private int width;
    private int height;
    private boolean showTime;
    private List<NodeDiagram> nodeDiagrams;

    public List<NodeDiagram> getNodeDiagrams() {
        return nodeDiagrams;
    }

    public void setNodeDiagrams(List<NodeDiagram> nodeDiagrams) {
        this.nodeDiagrams = nodeDiagrams;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ProcessDiagram diagram=(ProcessDiagram)super.clone();
        List<NodeDiagram> list=new ArrayList<NodeDiagram>();
        for(NodeDiagram d:nodeDiagrams){
            list.add((NodeDiagram)d.clone());
        }
        diagram.setNodeDiagrams(list);
        return diagram;
    }
}
