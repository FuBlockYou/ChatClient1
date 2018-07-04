package com.chat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class ChatClient extends Frame {
    Socket socket = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    private boolean bConnected = false;
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
                disconnect();
                System.exit(0);

            }
        });
        textField.addActionListener(new TFListener());
        setVisible(true);
        connect();

        new Thread(new RecvThread()).start();
    }

    /**
     * 链接服务器端的方法
     */
    private void connect() {
        try {
            socket = new Socket("127.0.0.1", 8888);
            //初始化DataOutputStream
            dos = new DataOutputStream(socket.getOutputStream());
            //初始化DataInputStream
            dis = new DataInputStream(socket.getInputStream());
            //连接成功，修改连接标志位
            bConnected = true;
            //测试语句
            System.out.println("链接成功!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * textField监听器
     * 当在textField中输入内容完毕后，敲击回车即可将内容显示在textArea中
     */
    private class TFListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String str = textField.getText().trim();
            textArea.setText(str);
            textField.setText("");
            //网络发字符串
            try {
                sendMessage(str);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 网络发字符串
     */
    private void sendMessage(String s) throws IOException {

            dos.writeUTF(s);
            dos.flush();
            //dos.close();

    }

    /**
     * 关闭链接
     */
    private void disconnect() {
        try {
            dos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 接受网络消息
     */
    private class RecvThread implements Runnable{

        @Override
        public void run() {
            while (bConnected){
                try {

                    String str = new Date() +": "+"  "+ dis.readUTF();

                    System.out.println(str);
                    textArea.setText('\n'+"  "+textArea.getText()+str+'\n');
                } catch(SocketException e){
                    System.out.println("退出了");
                }catch (EOFException E){
                    System.out.println("退出了，bye");
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
