package com.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ChatServe {

    //服务启动前，标志位为false，启动之后为true
    boolean started = false;
    ServerSocket serverSocket = null;
    //List内装所有的Client对象，也就是说有多少个客户端窗口就在List中存入多少个Client对象
    List<Client> clients = new ArrayList<>();

    /**
     * 基于TCP的聊天的服务端
     *
     * @param args
     */
    public static void main(String[] args) {
        new ChatServe().start();
    }

    /**
     * 在静态方法内创建动态对象的解决方法
     */
    public void start() {

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
                Socket s = serverSocket.accept();
                Client c = new Client(s);
                new Thread(c).start();
                //1.先保存
                clients.add(c);
                //调试语句
                System.out.println("a client connnected");
            }
        } catch (IOException e) {

            System.out.println("客户端已关闭");
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 线程: 每一个客户端对应一个线程
     */
    class Client implements Runnable {
        private Socket s;
        //接受信息
        private DataInputStream dis = null;
        //输出信息
        private DataOutputStream dos = null;

        private boolean bConnected = false;

        public Client(Socket s) {
            this.s = s;
            try {
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                bConnected = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 发送消息
         */
        public void send(String str) {
            try {
                dos.writeUTF(str);
            }catch (SocketException e){
                clients.remove(this);
                System.out.println("有客户端退了,11");
            }catch (IOException e) {
                clients.remove(this);
                System.out.println("有客户端退了");
            }
        }

        @Override
        public void run() {
            Client c = null;
            try {
                while (bConnected) {
                    String str = dis.readUTF();
                    System.out.println(str);
                    //2.按照List中的客户端挨个发送所拿到的内容
                    for (int i =0;i<clients.size();i++){
                        c = clients.get(i);
                        c.send(str);

                    }
                }
            }catch (SocketException e){
                clients.remove(this);
                System.out.println("一个用户已退出了聊天室");
            }catch (EOFException e){
                System.out.println("服务器端已关闭");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dis != null)
                        dis.close();
                    if (s != null)
                        s.close();
                    if (dos !=null)
                        dos.close();


                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (c!=null){
                    clients.remove(c);
                }
            }
        }
    }
}
