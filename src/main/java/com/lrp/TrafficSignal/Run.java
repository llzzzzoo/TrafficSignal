package com.lrp.TrafficSignal;

import com.lrp.TrafficSignal.Car.Car;
import com.lrp.TrafficSignal.GUI.TrafficPanel;
import com.lrp.TrafficSignal.Road.Road;
import com.lrp.TrafficSignal.Road.RoadThread;
import com.lrp.TrafficSignal.Signal.Signal;
import com.lrp.TrafficSignal.Signal.SignalThread;
import com.lrp.TrafficSignal.Util.Time;

import java.util.Queue;


/**
 * 存放运行相关的方法，并且包含了图像相关
 */
public class Run {
    private final RoadThread eastRoad;
    private final RoadThread southRoad;
    private final RoadThread westRoad;
    private final RoadThread northRoad;
    private final SignalThread eastSignal;
    private final SignalThread southSignal;
    private final SignalThread westSignal;
    private final SignalThread northSignal;
    public static final int STEP = 25; // 一次移动的距离
    public static final double degreeOfCrowd = 0.01; // 拥塞程度，[0, 1)
    private static final int[][] referTable = new int[][]{
            {1,0,0,0,1,0,0,0},
            {0,1,0,0,0,0,1,0},
            {0,0,1,0,0,0,1,0},
            {1,0,0,1,0,0,0,0},
            {1,0,0,0,1,0,0,0},
            {0,0,1,0,0,1,0,0},
            {0,0,1,0,0,0,1,0},
            {0,0,0,0,1,0,0,1}};
    private static final int ROW_NUM = 8; // referTable的行长
    private int readCount = 0; // 标记这次应该读第几行
    private static int signalTime = 50 * TrafficPanel.FRAME_TIME; // 倒计时

    public Run() {
        eastRoad = new RoadThread(Road.EAST);// 指向东边
        southRoad = new RoadThread(Road.SOUTH);// 指向南边
        westRoad = new RoadThread(Road.WEST);// 指向西边
        northRoad = new RoadThread(Road.NORTH);// 指向北边
        eastSignal = new SignalThread(Road.EAST); // 位于东边
        southSignal = new SignalThread(Road.SOUTH); // 位于南边
        westSignal = new SignalThread(Road.WEST); // 位于西边
        northSignal = new SignalThread(Road.NORTH); // 位于北边
        initSignal();
    }

    public void play(){
        // Signal
        if(signalTime == 0){
            changeSignal();
            signalTime = 50 * TrafficPanel.FRAME_TIME;
        }
        signalTime -= TrafficPanel.FRAME_TIME;
        // createCar
        createCarRandom(eastRoad);
        createCarRandom(southRoad);
        createCarRandom(westRoad);
        createCarRandom(northRoad);
        // sendRun
        sendToRun(eastRoad);
        sendToRun(southRoad);
        sendToRun(westRoad);
        sendToRun(northRoad);
        // sendDisplay
        sendToDisplay(eastRoad);
        sendToDisplay(southRoad);
        sendToDisplay(westRoad);
        sendToDisplay(northRoad);
        // refreshDisplay
        refreshDisplay(eastRoad);
        refreshDisplay(southRoad);
        refreshDisplay(westRoad);
        refreshDisplay(northRoad);
        // refreshRun
        refreshRun(eastRoad);
        refreshRun(southRoad);
        refreshRun(westRoad);
        refreshRun(northRoad);
        // checkRunOut
        solveCarOut(eastRoad);
        solveCarOut(southRoad);
        solveCarOut(westRoad);
        solveCarOut(northRoad);
    }

    public void createCarRandom(RoadThread roadThread){
        for (Road road :
                roadThread.getRoadQueue()) {
            // 每条道随机产生车
            if(Math.random() >= 1 - degreeOfCrowd){
                road.getBackUpCarQueue().add(new Car(road.getLocation(), road.getIndex()));
            }
        }
    }

    /**
     * 让就绪队列中的车辆进入行驶队列
     */
    public void sendToRun(RoadThread roadThread){
        int i = 0; // 决定交通灯的哪个灯变绿
        for (Road road :
                roadThread.getRoadQueue()) {
            if(road.getDisplayCarQueue().size() > 0){
                int location = road.getLocation();
                Car car = road.getDisplayCarQueue().peek();
                if (car == null) return;
                // 交通灯的条件
                // 绿灯、坐标到达才能运行
                switch (location){
                    case Road.EAST:
                        if(car.getDecision() == Car.LEFT){
                            i = 0;
                        } else if(car.getDecision() == Car.STRAIGHT){
                            i = 1;
                        } else if(car.getDecision() == Car.RIGHT){
                            road.getRunCarQueue().offer(road.getDisplayCarQueue().poll());
                            break;
                        }
                        if(car.getX() == 75 && checkSignal(eastSignal, i)){
                            // 一个出队离开显示队列，一个入队进入运行队列
                            road.getRunCarQueue().offer(road.getDisplayCarQueue().poll());
                        }
                        break;
                    case Road.SOUTH:
                        if(car.getDecision() == Car.LEFT){
                            i = 1;
                        } else if(car.getDecision() == Car.STRAIGHT){
                            i = 0;
                        } else if(car.getDecision() == Car.RIGHT){
                            road.getRunCarQueue().offer(road.getDisplayCarQueue().poll());
                            break;
                        }
                        if(car.getY() == 75 && checkSignal(southSignal, i)){
                            road.getRunCarQueue().offer(road.getDisplayCarQueue().poll());
                        }
                        break;
                    case Road.WEST:
                        if(car.getDecision() == Car.LEFT){
                            i = 1;
                        } else if(car.getDecision() == Car.STRAIGHT){
                            i = 0;
                        } else if(car.getDecision() == Car.RIGHT){
                            road.getRunCarQueue().offer(road.getDisplayCarQueue().poll());
                            break;
                        }
                        if(car.getX() == 600 && checkSignal(westSignal, i)){
                            road.getRunCarQueue().offer(road.getDisplayCarQueue().poll());
                        }
                        break;
                    case Road.NORTH:
                        if(car.getDecision() == Car.LEFT){
                            i = 0;
                        } else if(car.getDecision() == Car.STRAIGHT){
                            i = 1;
                        } else if(car.getDecision() == Car.RIGHT){
                            road.getRunCarQueue().offer(road.getDisplayCarQueue().poll());
                            break;
                        }
                        if(car.getY() == 600 && checkSignal(northSignal, i)){
                            road.getRunCarQueue().offer(road.getDisplayCarQueue().poll());
                        }
                        break;
                }
            }
        }
    }

    /**
     * 将后备队列的车送到就绪队列去，如果显示队列未满且后备队列不为空
     */
    public void sendToDisplay(RoadThread roadThread){
        for (Road road :
                roadThread.getRoadQueue()) {
            if(road.getDisplayCarQueue().size() < 2 && road.getBackUpCarQueue().size() != 0){
                Car car = road.getBackUpCarQueue().poll();
                if(road.getDisplayCarQueue().size() == 1){
                    if(car != null) delayCar(car, road.getDisplayCarQueue().element());
                }
                road.getDisplayCarQueue().offer(car);
            }
        }
    }

    /**
     * 避免车的重叠，要延迟car的移动
     */
    public void delayCar(Car car, Car frontCar){
        if(car.getDirection() == Car.DIRECTION.UP){
            if(frontCar.getY() == 700){
                car.setY(750);
            }
        } else if(car.getDirection() == Car.DIRECTION.DOWN){
            if(frontCar.getY() == -25){
                car.setY(-75);
            }
        } else if(car.getDirection() == Car.DIRECTION.LEFT){
            if(frontCar.getX() == -25){
                car.setX(-75);
            }
        } else if(car.getDirection() == Car.DIRECTION.RIGHT){
            if(frontCar.getX() == 700){
                car.setX(750);
            }
        }
    }

    public boolean checkSignal(SignalThread signalThread, int judgeIndex){
        return signalThread.getSignalQueue().get(judgeIndex).getColor() == Signal.GREEN;
    }

    /**
     * 更新就绪队列，即更新小车的坐标
     */
    public void refreshDisplay(RoadThread roadThread) {
        for (Road road :
                roadThread.getRoadQueue()) {
            int i = 0; // 仔细次数，判断前方是否有车
            int location = road.getLocation();
            for (Car car :
                    road.getDisplayCarQueue()) {
                switch (location) {
                    case Road.EAST:
                        if(car.getX() < 75) {
                            if(!(i == 1) || !(car.getX() == 0)){
                                car.setX(car.getX() + STEP);
                            }
                        }
                        break;
                    case Road.SOUTH:
                        if(car.getY() < 75) {
                            if(!(i == 1) || !(car.getY() == 0)){
                                car.setY(car.getY() + STEP);
                            }
                        }
                        break;
                    case Road.WEST:
                        if(car.getX() > 600) {
                            if(!(i == 1) || !(car.getX() == 675)){
                                car.setX(car.getX() - STEP);
                            }
                        }
                        break;
                    case Road.NORTH:
                        if(car.getY() > 600) {
                            if(!(i == 1) || !(car.getY() == 675)){
                                car.setY(car.getY() - STEP);
                            }
                        }
                        break;
                }
                i++;
            }
        }
    }

    /**
     * 更新运行队列，即更新小车的坐标
     */
    public void refreshRun(RoadThread roadThread){
        for (Road road :
                roadThread.getRoadQueue()) {
            int location = road.getLocation();
            for (Car car :
                    road.getRunCarQueue()) {
                switch (location) {
                    case Road.EAST:
                        // 要把判断放在前面，因为此时已经到了应该转向的位置
                        if(car.getDecision() != Car.STRAIGHT){
                            if(car.getTurnMark() == 0 && isChangeLocation(car, location)){
                                changeDirection(car);
                                car.setTurnMark(1);
                            }
                            if(car.getTurnMark() == 1){
                                if(car.getDecision() == Car.LEFT){
                                    car.setY(car.getY() - 25);
                                } else if(car.getDecision() == Car.RIGHT){
                                    car.setY(car.getY() + 25);
                                }
                            } else {
                                car.setX(car.getX() + 25);
                            }
                        } else {
                            car.setX(car.getX() + 25);
                        }
                        break;
                    case Road.SOUTH:
                        if(car.getDecision() != Car.STRAIGHT){
                            if(car.getTurnMark() == 0 && isChangeLocation(car, location)){
                                changeDirection(car);
                                car.setTurnMark(1);
                            }
                            if(car.getTurnMark() == 1){
                                if(car.getDecision() == Car.LEFT){
                                    car.setX(car.getX() + 25);
                                } else if(car.getDecision() == Car.RIGHT){
                                    car.setX(car.getX() - 25);
                                }
                            } else {
                                car.setY(car.getY() + 25);
                            }
                        } else {
                            car.setY(car.getY() + 25);
                        }
                        break;
                    case Road.WEST:
                        if(car.getDecision() != Car.STRAIGHT){
                            if(car.getTurnMark() == 0 && isChangeLocation(car, location)){
                                changeDirection(car);
                                car.setTurnMark(1);
                            }
                            if(car.getTurnMark() == 1){
                                if(car.getDecision() == Car.LEFT){
                                    car.setY(car.getY() + 25);
                                } else if(car.getDecision() == Car.RIGHT){
                                    car.setY(car.getY() - 25);
                                }
                            } else {
                                car.setX(car.getX() - 25);
                            }
                        } else {
                            car.setX(car.getX() - 25);
                        }
                        break;
                    case Road.NORTH:
                        if(car.getDecision() != Car.STRAIGHT){
                            if(car.getTurnMark() == 0 && isChangeLocation(car, location)){
                                changeDirection(car);
                                car.setTurnMark(1);
                            }
                            if(car.getTurnMark() == 1){
                                if(car.getDecision() == Car.LEFT){
                                    car.setX(car.getX() - 25);
                                } else if(car.getDecision() == Car.RIGHT){
                                    car.setX(car.getX() + 25);
                                }
                            } else {
                                car.setY(car.getY() - 25);
                            }
                        } else {
                            car.setY(car.getY() - 25);
                        }
                        break;
                }
            }
        }
    }

    /**
     * 初始化信道等，让右下和左上先为绿色
     */
    public void initSignal(){
        resetSignal();
        // 设置右下和左上的颜色为绿，并设置全部信号灯的时间
        setSignal(eastSignal, 1, Signal.GREEN, 20 * Time.UNIT_TIME);
        setSignal(westSignal, 0, Signal.GREEN, 20 * Time.UNIT_TIME);
    }

    public void resetSignal(){
        setSignal(eastSignal, 0, Signal.RED, 20 * Time.UNIT_TIME);
        setSignal(eastSignal, 1, Signal.RED, 20 * Time.UNIT_TIME);
        setSignal(westSignal, 0, Signal.RED, 20 * Time.UNIT_TIME);
        setSignal(westSignal, 1, Signal.RED, 20 * Time.UNIT_TIME);
        setSignal(southSignal, 0, Signal.RED, 20 * Time.UNIT_TIME);
        setSignal(southSignal, 1, Signal.RED, 20 * Time.UNIT_TIME);
        setSignal(northSignal, 0, Signal.RED, 20 * Time.UNIT_TIME);
        setSignal(northSignal, 1, Signal.RED, 20 * Time.UNIT_TIME);
    }

    /**
     * 以组的形式变化
     */
    public void changeSignal(){
        resetSignal();
        for (int i = 0; i < referTable[readCount].length; i++) {
            int j = referTable[readCount][i];
            if(j == 1){
                switch (i){
                    case 0:
                        setSignal(westSignal, 0, Signal.GREEN, 20 * Time.UNIT_TIME);
                        break;
                    case 1:
                        setSignal(westSignal, 1, Signal.GREEN, 20 * Time.UNIT_TIME);
                        break;
                    case 2:
                        setSignal(northSignal, 1, Signal.GREEN, 20 * Time.UNIT_TIME);
                        break;
                    case 3:
                        setSignal(northSignal, 0, Signal.GREEN, 20 * Time.UNIT_TIME);
                        break;
                    case 4:
                        setSignal(eastSignal, 1, Signal.GREEN, 20 * Time.UNIT_TIME);
                        break;
                    case 5:
                        setSignal(eastSignal, 0, Signal.GREEN, 20 * Time.UNIT_TIME);
                        break;
                    case 6:
                        setSignal(southSignal, 0, Signal.GREEN, 20 * Time.UNIT_TIME);
                        break;
                    case 7:
                        setSignal(southSignal, 1, Signal.GREEN, 20 * Time.UNIT_TIME);
                        break;
            }
            }
        }
        if(readCount < ROW_NUM - 1){
            readCount += 1;
        } else {
            readCount = 0;
        }
    }

    /**
     * 设置指定位置的灯变为，另外一个灯就会变红
     * @param signalThread 控制信号的线程
     * @param signalIndex 两个灯之间的指定位置
     */
    public void setSignal(SignalThread signalThread, int signalIndex, int color, long time){
        Signal s =  signalThread.getSignalQueue().get(signalIndex);
        s.setColor(color);
        s.setTime(time);
    }

    public boolean isChangeLocation(Car car, int location){
        if(location == Road.EAST){
            if(car.getDecision() == Car.LEFT){
                return car.getX() == 400;
            } else if(car.getDecision() == Car.RIGHT){
                return car.getX() == 125;
            }
        }else if(location == Road.SOUTH){
            if(car.getDecision() == Car.LEFT){
                return car.getY() == 400;
            } else if(car.getDecision() == Car.RIGHT){
                return car.getY() == 125;
            }
        }else if(location == Road.WEST){
            if(car.getDecision() == Car.LEFT){
                return car.getX() == 275;
            } else if(car.getDecision() == Car.RIGHT){
                return car.getX() == 550;
            }
        }else if(location == Road.NORTH){
            if(car.getDecision() == Car.LEFT){
                return car.getY() == 275;
            } else if(car.getDecision() == Car.RIGHT){
                return car.getY() == 550;
            }
        }
        return false;
    }

    /**
     * 将出界的车辆从运行队列删除
     */
    public void solveCarOut(RoadThread roadThread){
        for (Road road:
             roadThread.getRoadQueue()) {
            Queue<Car> runQueue = road.getRunCarQueue();
            Car car = runQueue.peek();
            if(car != null){
                if(checkOutBoundary(car)){
                    runQueue.poll();
                }
            }
        }
    }

    public boolean checkOutBoundary(Car car){
        return car.getX() < -50 || car.getX() > 725 || car.getY() < -50 || car.getY() > 725;
    }

    public void changeDirection(Car car){
        if (car.getDecision() == Car.LEFT) {
            if(car.getDirection() == Car.DIRECTION.RIGHT){
                car.setDirection(Car.DIRECTION.UP);
            } else if(car.getDirection() == Car.DIRECTION.UP){
                car.setDirection(Car.DIRECTION.LEFT);
            } else if(car.getDirection() == Car.DIRECTION.LEFT){
                car.setDirection(Car.DIRECTION.DOWN);
            } else if(car.getDirection() == Car.DIRECTION.DOWN){
                car.setDirection(Car.DIRECTION.RIGHT);
            }
        } else if(car.getDecision() == Car.RIGHT){
            if(car.getDirection() == Car.DIRECTION.RIGHT){
                car.setDirection(Car.DIRECTION.DOWN);
            } else if(car.getDirection() == Car.DIRECTION.UP){
                car.setDirection(Car.DIRECTION.RIGHT);
            } else if(car.getDirection() == Car.DIRECTION.LEFT){
                car.setDirection(Car.DIRECTION.UP);
            } else if(car.getDirection() == Car.DIRECTION.DOWN){
                car.setDirection(Car.DIRECTION.LEFT);
            }
        }
    }

    public RoadThread getEastRoad() {
        return eastRoad;
    }

    public RoadThread getSouthRoad() {
        return southRoad;
    }

    public RoadThread getWestRoad() {
        return westRoad;
    }

    public RoadThread getNorthRoad() {
        return northRoad;
    }

    public SignalThread getEastSignal() {
        return eastSignal;
    }

    public SignalThread getSouthSignal() {
        return southSignal;
    }

    public SignalThread getWestSignal() {
        return westSignal;
    }

    public SignalThread getNorthSignal() {
        return northSignal;
    }
}
