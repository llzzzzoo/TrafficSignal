package com.lrp.TrafficSignal.Road;

import com.lrp.TrafficSignal.Car.Car;

import java.util.LinkedList;
import java.util.Queue;

public class Road {
    private final int location; // Road所在的位置
    public static final int EAST = 0; // 东
    public static final int SOUTH = 1; // 南
    public static final int WEST = 2; // 西
    public static final int NORTH = 3; // 北
    private final int index; // 道路的下标，默认上/左为0
    private final Queue<Car> backUpCarQueue; // 车的后备队列
    private final Queue<Car> displayCarQueue; // 显示的车队列，就那能看到的两台
    private final Queue<Car> runCarQueue; // 运行的车队列，已经在道路上行驶了

    public Road(int location, int index) {
        this.location = location;
        this.index = index;
        this.backUpCarQueue = new LinkedList<>();
        this.displayCarQueue = new LinkedList<>();
        this.runCarQueue = new LinkedList<>();
    }

    public int getLocation() {
        return location;
    }

    public int getIndex() {
        return index;
    }

    public Queue<Car> getBackUpCarQueue() {
        return backUpCarQueue;
    }

    public Queue<Car> getDisplayCarQueue() {
        return displayCarQueue;
    }

    public Queue<Car> getRunCarQueue() {
        return runCarQueue;
    }
}
