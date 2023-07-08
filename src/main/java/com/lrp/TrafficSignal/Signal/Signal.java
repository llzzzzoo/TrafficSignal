package com.lrp.TrafficSignal.Signal;

public class Signal {
    private int color; // 信号灯的颜色
    private long time; // 信道灯此时显示的时间
    private int location; // 信号灯所处的位置
    public final static int RED = 0; // 颜色常量
    public final static int GREEN = 1;

    public Signal(int color, int location) {
        this.color = color;
        this.location = location;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLocation() {
        return location;
    }
}
