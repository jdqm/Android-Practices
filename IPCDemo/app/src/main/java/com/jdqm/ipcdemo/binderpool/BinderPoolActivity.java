package com.jdqm.ipcdemo.binderpool;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jdqm.ipcdemo.R;
import com.jdqm.ipcdemo.aidl.ICompute;
import com.jdqm.ipcdemo.aidl.ISecurityCenter;

public class BinderPoolActivity extends AppCompatActivity {

    private static final String TAG = "BinderPoolActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }


    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(getApplicationContext());
        IBinder securityBinder = binderPool.queryBinder(BinderPool.SECURITY_BINDER);
        ISecurityCenter securityCenter = ISecurityCenter.Stub.asInterface(securityBinder);
        Log.d(TAG, "access ISecurity.");
        String content = "代码艺术";
        Log.d(TAG, "content: " + content);
        try {
            String encrypt = securityCenter.encrypt(content);
            Log.d(TAG, "encrypt: " + encrypt);
            String decrypt = securityCenter.decrypt(encrypt);
            Log.d(TAG, "decrypt: " + decrypt);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder computeBinder = binderPool.queryBinder(BinderPool.COMPUTE_BINDER);
        ICompute iCompute = ICompute.Stub.asInterface(computeBinder);
        try {
            int add = iCompute.add(3, 5);
            Log.d(TAG, "3 + 5 = " + add);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
