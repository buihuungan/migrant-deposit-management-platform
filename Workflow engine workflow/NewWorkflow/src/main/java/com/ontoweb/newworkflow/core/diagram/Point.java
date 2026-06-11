package com.ontoweb.newworkflow.core.diagram;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午9:32
 * @description：
 * @modified By：
 * @version: $
 */
public class Point implements Cloneable{
    private int x;
    private int y;

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
