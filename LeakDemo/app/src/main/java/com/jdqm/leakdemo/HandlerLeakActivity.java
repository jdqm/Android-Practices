package com.jdqm.leakdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HandlerLeakActivity extends AppCompatActivity {

    private MyHandler myHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_leak);
        //发送一个延时10秒的消息，这个时候Message被投递到MessageQueue中，而Message的target引用了Handler，
        //该Handler又是非静态内部类，持有外部类的引用，从而导致Activity无法回收
        myHandler.sendEmptyMessageDelayed(0, 10000);
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除消息队列中的对应的消息，释放Handler，从而释放Activity
        //myHandler.removeMessages(0);
    }
}
