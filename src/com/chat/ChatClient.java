package com.chat;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatClient extends Frame {

    TextField textField = new TextField();
    TextArea textArea = new TextArea();

    public static void main(String[] str) {
        new ChatClient().launchFrame();

    }

    public void launchFrame(){
        setLocation(400,300);
        this.setSize(300,300);
        add(textField, BorderLayout.SOUTH);
        add(textArea, BorderLayout.NORTH);
        pack();
        this.addWindowListener(new WindowAdapter() {
            //关闭窗口操作实现
            //采用匿名内部类实现
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
    }


}
