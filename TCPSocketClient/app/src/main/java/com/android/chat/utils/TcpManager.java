package com.android.chat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @描述 使用socket实现长连接
 * @项目名称 App_Chat
 * @包名 com.android.chat.utils
 */
public class TcpManager {
    public static final int STATE_FROM_SERVER_OK = 0;
    private static String host = "192.168.3.15";
    private static int port = 10002;
    private static Socket client;

    private static TcpManager instance;

    private TcpManager() {

    };

    public static TcpManager getInstance() {
        if (instance == null) {
            synchronized (TcpManager.class) {
                if (instance == null) {
                    instance = new TcpManager();
                }
            }
        }
        return instance;
    }

    /**
     * 连接
     * 
     * @return
     */
    public boolean connect(final Handler handler) {

        if (client == null || client.isClosed()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.v("-------->","正在连接中........");
                    try {
                        client = new Socket(host, port);
                        Log.v("------>","socket对象创建");
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException("连接错误: " + e.getMessage());
                    }

                    try {
                        // 输入流，为了获取服务端发来的数据
                        InputStream is = client.getInputStream();
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            final String result = new String(buffer, 0, len);
                            Message msg = Message.obtain();
                            msg.obj = result;
                            msg.what = STATE_FROM_SERVER_OK;
                            handler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return true;
    }

    /**
     * 发送信息
     * 
     * @param content
     */
    public void sendMessage(String content) {
        OutputStream os = null;
        try {
            if (client != null) {
                os = client.getOutputStream();
                os.write(content.getBytes());
                os.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("发送失败:" + e.getMessage());
        } 
        //此处不能关闭
//      finally {
//          if (os != null) {
//              try {
//                  os.close();
//              } catch (IOException e) {
//                  throw new RuntimeException("未正常关闭输出流:" + e.getMessage());
//              }
//          }
//      }
    }

    /**
     * 关闭连接
     */
    public void disConnect() {
        if (client != null && !client.isClosed()) {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException("关闭异常:" + e.getMessage());
            }
            client = null;
        }
    }
}