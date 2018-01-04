package com.jdqm.leakdemo;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;

public class StaticHandlerActivity extends AppCompatActivity {

    private MyHandler myHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_handler);
        //同样的演示10秒，在onDestroy中也没有移除消息，但因为Handler对Activity的引用为弱引用，弱引用的对象是可以被回收的，所以不会导致其泄漏
        myHandler.sendEmptyMessageDelayed(0, 10000);
    }

    static class MyHandler extends Handler {
        private WeakReference<Activity> mActivity;

        public MyHandler(Activity Activity) {
            this.mActivity = new WeakReference<>(Activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            StaticHandlerActivity activity = (StaticHandlerActivity) mActivity.get();
            if (activity != null) {
                activity.setText();
            }
        }
    }

    public void setText() {
        //
    }
}
