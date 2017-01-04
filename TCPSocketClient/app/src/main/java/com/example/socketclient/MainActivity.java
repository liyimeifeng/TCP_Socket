package com.example.socketclient;

import com.android.chat.utils.TcpManager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

/**
 * @描述         Socket通信简易客户端
 * @项目名称      SocketClient
 */

public class MainActivity extends Activity implements View.OnClickListener {
    private EditText mEtContent;
    private TcpManager manager;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = TcpManager.getInstance();
        manager.connect(mHandler);
        mEtContent = (EditText) findViewById(R.id.et);
        mButton = (Button)findViewById(R.id.send);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(mEtContent.getText() != null){
            manager.sendMessage(mEtContent.getText().toString());
            Log.v("------------>","发送的内容---->" + mEtContent.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        manager.disConnect();
        super.onDestroy();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case TcpManager.STATE_FROM_SERVER_OK:
                    String result = (String) msg.obj;
//                ToastUtil.show(MainActivity.this, result);
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

}



