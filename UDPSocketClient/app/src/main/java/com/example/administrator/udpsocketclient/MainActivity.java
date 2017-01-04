package com.example.administrator.udpsocketclient;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DatagramSocket socketClient;
    private Button mSendButton;
    private EditText mText;
    private String sendText;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //为避免部分手机不能直接接受UDP包的情况，实例化WifiManager.MulticastLock对象lock
        WifiManager manager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
        WifiManager.MulticastLock loack = manager.createMulticastLock("text wifi");
        //在调用广播发送接受报文之前先调用lock.acquire()方法，
        // 并在用完之后即使调用lock.release释放资源，否则多次调用lock.acquire程序可能会崩溃
        mText = (EditText)findViewById(R.id.text);
        mSendButton = (Button)findViewById(R.id.send);
        mSendButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if (mText.getText().toString() != null){
            sendText = mText.getText().toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (socketClient == null){
                            socketClient = new DatagramSocket(8888);
                        }
                        InetAddress serverAddress = InetAddress.getByName("192.168.3.15");
                        byte[] data = sendText.getBytes();
                        Log.v("------>","-------" + data.length);
                        DatagramPacket packet = new DatagramPacket(data,data.length,serverAddress,8888);
                        socketClient.send(packet);
                        socketClient.receive(packet);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
