package com.lrp.TrafficSignal.GUI;

import com.lrp.TrafficSignal.Car.Car;
import com.lrp.TrafficSignal.Road.Road;
import com.lrp.TrafficSignal.Road.RoadThread;
import com.lrp.TrafficSignal.Run;
import com.lrp.TrafficSignal.Signal.Signal;
import com.lrp.TrafficSignal.Signal.SignalThread;
import com.lrp.TrafficSignal.Util.Time;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Queue;

public class TrafficPanel extends JPanel implements ActionListener {
    private int page_mark = 0; // 标记当前应该在哪个界面
    private static final int ROAD_LONG = 725;
    private static final int ROAD_SHORT = 200;
    private static final int SIGNAL_V_WIDTH = 10;
    private static final int SIGNAL_V_HEIGHT = 50;
    private static final int SIGNAL_H_WIDTH = 50;
    private static final int SIGNAL_H_HEIGHT = 10;
    public static final int FRAME_TIME = Time.UNIT_TIME / 10;
    private final Timer timer = new Timer(FRAME_TIME, this); // 定时器
    private final Run newPage; // 界面运行产生的新界面

    public TrafficPanel() {
        initialInterface();
        newPage = new Run();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 清屏
        if(page_mark == 2){
            super.paintComponent(g); // 清屏
            initRunPage(g);
            run(g);
            timer.start();
        } else if (page_mark == 1) {
            this.removeAll();
            this.repaint();
            this.revalidate();
            page_mark = 2;
        }
    }

    protected void initialInterface(){
        JButton startBtn = new JButton("START");
        JButton exitBtn = new JButton("EXIT");
        JButton blankBtn = new JButton(" ");
        startBtn.setContentAreaFilled(false);
        startBtn.setBorderPainted(false);
        startBtn.setFont(new java.awt.Font("Microsoft YaHei", Font.BOLD, 100));
        startBtn.setFocusPainted(false);
        blankBtn.setFont(new java.awt.Font("Microsoft YaHei", Font.BOLD, 70));
        blankBtn.setContentAreaFilled(false);
        blankBtn.setBorderPainted(false);
        blankBtn.setFocusPainted(false);
        exitBtn.setContentAreaFilled(false);
        exitBtn.setBorderPainted(false);
        exitBtn.setFont(new java.awt.Font("Microsoft YaHei", Font.BOLD, 100));
        exitBtn.setFocusPainted(false);
        startBtn.setAlignmentX(CENTER_ALIGNMENT);
        blankBtn.setAlignmentX(CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(CENTER_ALIGNMENT);
        setBtn(startBtn, blankBtn, 1);
        setBtn(exitBtn, blankBtn, 0);

        Box box = Box.createVerticalBox();
        this.add(box);
        box.add(Box.createVerticalStrut(150));
        box.add(startBtn);
        box.add(blankBtn);
        box.add(exitBtn);
    }

    private void setBtn(JButton btn, JButton blankBtn, int mark) {
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(mark == 1){
                    responseBtn(1);
                }else {
                    responseBtn(0);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                if(mark == 1) {
                    blankBtn.setFont(new Font("Microsoft YaHei", Font.BOLD, 51));
                }
                btn.setFont(new Font("Microsoft YaHei", Font.BOLD, 120));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if(mark == 1) {
                    blankBtn.setFont(new Font("Microsoft YaHei", Font.BOLD, 70));
                }
                btn.setFont(new Font("Microsoft YaHei", Font.BOLD, 100));
            }
        });
    }

    public void responseBtn(int mark){
        if(mark == 1){
            page_mark = 1;
        }else {
            System.exit(0);
        }
    }

    public void initRunPage(Graphics g){
        Color red = new Color(0xff6b68);
        g.setColor(Color.WHITE);
        g.fillRect(0, 125, ROAD_LONG, ROAD_SHORT);
        g.fillRect(0, 400, ROAD_LONG, ROAD_SHORT);
        g.fillRect(125, 0, ROAD_SHORT, ROAD_LONG);
        g.fillRect(400, 0, ROAD_SHORT, ROAD_LONG);
        g.fillRect(325, 325, 75, 75);
        g.setColor(red);
        g.fillRect(595, 400, SIGNAL_V_WIDTH, SIGNAL_V_HEIGHT); // 右下的左控灯
        g.fillRect(595, 475, SIGNAL_V_WIDTH, SIGNAL_V_HEIGHT); // 右下的直控灯
        g.fillRect(130, 200, SIGNAL_V_WIDTH, SIGNAL_V_HEIGHT); // 左上的直控灯
        g.fillRect(130, 275, SIGNAL_V_WIDTH, SIGNAL_V_HEIGHT); // 左上的左控灯
        g.fillRect(400, 130, SIGNAL_H_WIDTH, SIGNAL_H_HEIGHT); // 右上的左控灯
        g.fillRect(475, 130, SIGNAL_H_WIDTH, SIGNAL_H_HEIGHT); // 右上的直控灯
        g.fillRect(200, 595, SIGNAL_H_WIDTH, SIGNAL_H_HEIGHT); // 左下的直控灯
        g.fillRect(275, 595, SIGNAL_H_WIDTH, SIGNAL_H_HEIGHT); // 左下的左控灯
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 修改相关的值
        newPage.play();
        repaint();
    }

    /**
     * It's the showtime!
     */
    public void run(Graphics g){
        // 执行了一次，代表一个新的界面
        // 处理车辆行驶相关的界面
        RoadThread eastRoad = newPage.getEastRoad();
        RoadThread southRoad = newPage.getSouthRoad();
        RoadThread westRoad = newPage.getWestRoad();
        RoadThread northRoad = newPage.getNorthRoad();
        RoadThread[] roadArr = new RoadThread[]{eastRoad, southRoad, westRoad, northRoad};
        // 处理交通的界面
        SignalThread eastSignal = newPage.getEastSignal();
        SignalThread southSignal = newPage.getSouthSignal();
        SignalThread westSignal = newPage.getWestSignal();
        SignalThread northSignal = newPage.getNorthSignal();
        SignalThread[] signalArr = new SignalThread[]{eastSignal, southSignal, westSignal, northSignal};
        for (RoadThread roadThread : roadArr) {
            changeRoadThread(roadThread, g);
        }
        for (SignalThread signalThread : signalArr) {
            changeSignalInfo(signalThread, g);
        }
    }

    public void changeRoadThread(RoadThread roadThread, Graphics g){
        for (int i = 0; i < RoadThread.ROAD_NUM; i++) {
            changeRoadInfo(roadThread, i, g);
        }
    }

    public void changeRoadInfo(RoadThread roadThread, int roadIndex, Graphics g){
        Road road = roadThread.getRoadQueue().get(roadIndex);
        // 将此路上的三条队列全部打印出来
        displayCar(road.getRunCarQueue(), g);
        displayCar(road.getDisplayCarQueue(), g);
        displayCar(road.getBackUpCarQueue(), g);
    }

    /**
     * 这个Queue是java.util.Queue包下的，而不是awt包下的
     */
    public void displayCar(Queue<Car> queue, Graphics g){
        for (Car car : queue) {
            int color = car.getColor();
            int x = car.getX();
            int y = car.getY();
            if (car.getDirection() == Car.DIRECTION.UP){
                if (color == Car.BLACK){
                    Data.blackUp.paintIcon(this, g, x, y);
                } else if(color== Car.GRAY){
                    Data.grayUp.paintIcon(this, g, x, y);
                }
            } else if(car.getDirection() == Car.DIRECTION.DOWN){
                if (color == Car.BLACK){
                    Data.blackDown.paintIcon(this, g, x, y);
                } else if(color== Car.GRAY){
                    Data.grayDown.paintIcon(this, g, x, y);
                }
            } else if(car.getDirection() == Car.DIRECTION.LEFT){
                if (color == Car.BLACK){
                    Data.blackLeft.paintIcon(this, g, x, y);
                } else if(color== Car.GRAY){
                    Data.grayLeft.paintIcon(this, g, x, y);
                }
            } else if(car.getDirection() == Car.DIRECTION.RIGHT){
                if (color == Car.BLACK){
                    Data.blackRight.paintIcon(this, g, x, y);
                } else if(color== Car.GRAY){
                    Data.grayRight.paintIcon(this, g, x, y);
                }
            }
        }
    }

    public void changeSignalInfo(SignalThread signalThread, Graphics g){
        displaySignal(signalThread.getSignalQueue(), g);
    }

    public void displaySignal(ArrayList<Signal> s, Graphics g){
        int mark = 0;
        Color red = new Color(0xff6b68);
        Color green = new Color(0x499c54);
        for (Signal signal :
                s) {
            if (signal.getColor() == Signal.RED){
                g.setColor(red);
            } else if(signal.getColor() == Signal.GREEN){
                g.setColor(green);
            }
            if(signal.getLocation() == Road.EAST){
                if (mark == 0){
                    g.fillRect(595, 400, SIGNAL_V_WIDTH, SIGNAL_V_HEIGHT); // 右下的左控灯
                } else {
                    g.fillRect(595, 475, SIGNAL_V_WIDTH, SIGNAL_V_HEIGHT); // 右下的直控灯
                }
            } else if(signal.getLocation() == Road.SOUTH){
                if (mark == 0){
                    g.fillRect(200, 595, SIGNAL_H_WIDTH, SIGNAL_H_HEIGHT); // 左下的直控灯
                } else {
                    g.fillRect(275, 595, SIGNAL_H_WIDTH, SIGNAL_H_HEIGHT); // 左下的左控灯
                }
            } else if(signal.getLocation() == Road.WEST){
                if (mark == 0){
                    g.fillRect(130, 200, SIGNAL_V_WIDTH, SIGNAL_V_HEIGHT); // 左上的直控灯
                } else {
                    g.fillRect(130, 275, SIGNAL_V_WIDTH, SIGNAL_V_HEIGHT); // 左上的左控灯
                }
            } else if(signal.getLocation() == Road.NORTH){
                if (mark == 0){
                    g.fillRect(400, 130, SIGNAL_H_WIDTH, SIGNAL_H_HEIGHT); // 右上的左控灯
                } else {
                    g.fillRect(475, 130, SIGNAL_H_WIDTH, SIGNAL_H_HEIGHT); // 右上的直控灯
                }
            }
            mark++;
        }
    }
}
