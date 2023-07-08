package com.lrp.TrafficSignal.Signal;

import java.util.ArrayList;

public class SignalThread {
    private final ArrayList<Signal> signalQueue = new ArrayList<>();
    public static final int SIGNAL_NUM = 2; // 一个Thread管辖的Signal的数目

    public SignalThread(int location) {
        for (int i = 0; i < SIGNAL_NUM; i++) {
            signalQueue.add(new Signal(Signal.RED, location));
        }
    }

    public ArrayList<Signal> getSignalQueue() {
        return signalQueue;
    }
}
