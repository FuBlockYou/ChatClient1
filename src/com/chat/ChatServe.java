package com.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServe {
    /**
     * 基于TCP的聊天的服务端
     *
     * @param args
     */
    public static void main(String[] args) {
        //服务启动前，标志位为false，启动之后为true
        boolean started = false;
        //链接标志位，如果链接成功则该标志位变为true
        boolean bConnected = false;
        ServerSocket serverSocket = null;
        Socket s = null;
        DataInputStream dis = null;

        try {
            serverSocket = new ServerSocket(8888);
        } catch (BindException e) {
            System.out.println("端口使用中......");
            System.out.println("请关闭相关程序,并重新启动");
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            started = true;

            while (started) {
                s = serverSocket.accept();
                //调试语句
                System.out.println("a client connnected");

                bConnected = true;

                dis = new DataInputStream(s.getInputStream());

                while (bConnected) {
                    String str = dis.readUTF();
                    System.out.println(str);
                }
//                dis.close();
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("客户端已关闭");
        } finally {
            try {
                if (dis != null)
                    dis.close();
                if (s != null)
                    s.close();
            } catch (IOException e1) {
                e1.printStackTrace();

                
            }
        }
    }
}
