package com.jdqm.ipcdemo.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jdqm.ipcdemo.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 * Created by HP on 2017-10-10.
 */

public class BinderPool {

    private static final String TAG = "BinderPool";

    public static final int SECURITY_BINDER = 0;
    public static final int COMPUTE_BINDER = 1;

    private Context context;

    private IBinderPool binderPool;
    private static volatile BinderPool sInstance;

    private CountDownLatch connectBinderPollCountDownLatch;

    private BinderPool(Context context) {
        this.context = context.getApplicationContext();
        connectBinderPoolServices(context);
    }

    public static BinderPool getInstance(Context context) {
        if (sInstance == null) {
            synchronized(BinderPool.class) {
                sInstance = new BinderPool(context);
            }
        }
        return  sInstance;
    }

    private synchronized void connectBinderPoolServices(Context context) {
        connectBinderPollCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(context, BinderPoolService.class);
        //Intent intent = new Intent("jdqm.intent.action.BINDER_POOL_LAUNCH");
        context.bindService(intent, binderPoolConnection, context.BIND_AUTO_CREATE);
        try {
            connectBinderPollCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection binderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected.");
            binderPool = IBinderPool.Stub.asInterface(service);
            try {
                binderPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            connectBinderPollCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected.");
        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d(TAG, "binderDied.");
            binderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            binderPool = null;
            //重连
            connectBinderPoolServices(context);
        }
    };


    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        if(binderPool != null) {
            try {
                binder = binderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;
    }
}
