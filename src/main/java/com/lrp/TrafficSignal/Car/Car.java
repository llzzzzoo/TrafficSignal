package com.lrp.TrafficSignal.Car;

import com.lrp.TrafficSignal.Road.Road;

import java.util.Random;

public class Car {
    private DIRECTION direction; // 车辆行驶方向
    private int decision; // 车辆目标地，左中右
    private final int color; // 车辆的颜色
    private int x; // 小车在图上的x坐标
    private int y; // 小车在图上的y坐标
    public final static int BLACK = 0; // 颜色常量
    public final static int GRAY = 1;
    public final static int LEFT = 0; // 方向常量
    public final static int STRAIGHT = 1;
    public final static int RIGHT = 3;
    private int turnMark = 0; // 为1表明已经发送了转向，为0表明还未有

    public Car(int location, int roadIndex) {
        Random random = new Random();
        if(random.nextInt(2) == 0){
            color = BLACK;
        }else {
            color = GRAY;
        }
        if(roadIndex == 1){
            this.decision = STRAIGHT;
        }
        // 根据所处位置，选择车的类型，即图片
        switch (location){
            case Road.EAST:
                this.x = -50;
                if(roadIndex == 0){
                    this.y = 400;
                    this.decision = LEFT;
                }else if(roadIndex == 1){
                    this.y = 475;
                }else if(roadIndex == 2){
                    this.y = 550;
                    this.decision = RIGHT;
                }
                this.direction = DIRECTION.RIGHT;
                break;
            case Road.SOUTH:
                this.y = -50;
                if(roadIndex == 0){
                    this.x = 125;
                    this.decision = RIGHT;
                }else if(roadIndex == 1){
                    this.x = 200;
                }else if(roadIndex == 2){
                    this.x = 275;
                    this.decision = LEFT;
                }
                this.direction = DIRECTION.DOWN;
                break;
            case Road.WEST:
                this.x = 725;
                if(roadIndex == 0){
                    this.y = 125;
                    this.decision = RIGHT;
                }else if(roadIndex == 1){
                    this.y = 200;
                }else if(roadIndex == 2){
                    this.y = 275;
                    this.decision = LEFT;
                }
                this.direction = DIRECTION.LEFT;
                break;
            case Road.NORTH:
                this.y = 725;
                if(roadIndex == 0){
                    this.x = 400;
                    this.decision = LEFT;
                }else if(roadIndex == 1){
                    this.x = 475;
                }else if(roadIndex == 2){
                    this.x = 550;
                    this.decision = RIGHT;
                }
                this.direction = DIRECTION.UP;
                break;
        }
    }

    public enum DIRECTION{
        UP, DOWN, LEFT, RIGHT
    }

    public int getColor() {
        return color;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDecision() {
        return decision;
    }

    public int getTurnMark() {
        return turnMark;
    }

    public void setTurnMark(int turnMark) {
        this.turnMark = turnMark;
    }
}
