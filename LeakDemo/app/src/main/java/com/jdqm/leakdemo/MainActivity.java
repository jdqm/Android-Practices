package com.jdqm.leakdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements TaskListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //将当前Activity注册，理应在onDestroy中解注册，否则Activity将被此单例引用，导致泄漏
        TaskManager.getInstance().registerTaskListener(this);
    }

    @Override
    protected void onDestroy() {
        //解注册后就不会泄漏
        TaskManager.getInstance().unRegisterTaskListener(this);
        super.onDestroy();
    }

    public void handlerLeak(View view) {
        startActivity(new Intent(this, HandlerLeakActivity.class));
    }

    public void staticHandler(View view) {
        startActivity(new Intent(this, StaticHandlerActivity.class));
    }
}
