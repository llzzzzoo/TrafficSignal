package com.lrp.TrafficSignal.Road;

import java.util.ArrayList;

public class RoadThread {
    private final ArrayList<Road> roadQueue = new ArrayList<>();
    public static final int ROAD_NUM = 3; // 一个Thread负责的道路数

    public RoadThread(int location) {
        for (int i = 0; i < ROAD_NUM; i++) {
            this.roadQueue.add(new Road(location, i));
        }
    }

    public ArrayList<Road> getRoadQueue() {
        return roadQueue;
    }
}
