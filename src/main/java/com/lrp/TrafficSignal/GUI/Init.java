package com.lrp.TrafficSignal.GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Init {
    public static URL iconURL = Init.class.getClassLoader().getResource("images/java.png");

    public void initUI(){
        JFrame frame = new JFrame("Traffic Signal Simulation");
        ImageIcon icon;
        if(iconURL != null){
            icon = new ImageIcon(iconURL);
            frame.setIconImage(icon.getImage());
        }
        // 原图标尺寸为725,725，此处考虑到了窗口
        frame.setSize(new Dimension(731, 760));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 界面位于面上
        JPanel jPanel = new TrafficPanel();
        frame.add(jPanel);
        frame.setVisible(true);
    }
}
