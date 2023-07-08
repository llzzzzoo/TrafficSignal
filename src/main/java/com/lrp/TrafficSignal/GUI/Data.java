package com.lrp.TrafficSignal.GUI;

import javax.swing.*;
import java.net.URL;

public class Data {
    public static URL blackUpURL = Data.class.getClassLoader().getResource("CarImage/" +
            "black-car-up.png");
    public static URL blackDownURL = Data.class.getClassLoader().getResource("CarImage/" +
            "black-car-down.png");
    public static URL blackLeftURL = Data.class.getClassLoader().getResource("CarImage/" +
            "black-car-left.png");
    public static URL blackRightURL = Data.class.getClassLoader().getResource("CarImage/" +
            "black-car-right.png");
    public static URL grayUpURL = Data.class.getClassLoader().getResource("CarImage/" +
            "gray-car-up.png");
    public static URL grayDownURL = Data.class.getClassLoader().getResource("CarImage/" +
            "gray-car-down.png");
    public static URL grayLeftURL = Data.class.getClassLoader().getResource("CarImage/" +
            "gray-car-left.png");
    public static URL grayRightURL = Data.class.getClassLoader().getResource("CarImage/" +
            "gray-car-right.png");
    public static ImageIcon blackUp = new ImageIcon(blackUpURL);
    public static ImageIcon blackDown = new ImageIcon(blackDownURL);
    public static ImageIcon blackLeft = new ImageIcon(blackLeftURL);
    public static ImageIcon blackRight = new ImageIcon(blackRightURL);
    public static ImageIcon grayUp = new ImageIcon(grayUpURL);
    public static ImageIcon grayDown = new ImageIcon(grayDownURL);
    public static ImageIcon grayLeft = new ImageIcon(grayLeftURL);
    public static ImageIcon grayRight = new ImageIcon(grayRightURL);
}
