package com.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @描述 Socket服务器端 TCP连接
 * @项目名称 SocketServer
 * @包名 com.java.socket.server
 * @类名 TCPServer
 */
public class TCPSocketServer {
    public static void main(String[] args) {
        int port = 10002;
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                // 获得客户端连接
                // 阻塞式方法
                System.out.println("准备阻塞...");
                final Socket client = server.accept();
                System.out.println("阻塞完成...客户端  " + client.getLocalAddress() + " 连接了进来");
                new Thread(new Runnable() {
                    public void run() {
                    	 try {
                             // 输入流，为了获取客户端发送的数据
                             InputStream is = client.getInputStream();
                             //打开输出流，向客户端输出数据
                             OutputStream out = client.getOutputStream();
                             byte[] buffer = new byte[1024];
                             int len = -1;
                             System.out.println("准备read...");
                             while ((len = is.read(buffer)) != -1) {
                                 String text = new String(buffer, 0, len);
                                 System.out.println("客户端说：   " + text);
                                 //回复信息
                                 out.write("收到信息,over".getBytes());
                             }
                         }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}