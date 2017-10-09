package com.jdqm.ipcdemo.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jdqm.ipcdemo.Constants;

/**
 * Created by HP on 2017-10-9.
 */

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_Client:
                    String msg1 = msg.getData().getString("msg");
                    Log.d(TAG, "receive msg from client: " + msg1);
                    Messenger replyTo = msg.replyTo;
                    Message msg2 = Message.obtain(null, Constants.MSG_FROM_SERVER);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "嗯，收到你的消息了，稍后回复");
                    msg2.setData(bundle);
                    try {
                        replyTo.send(msg2);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Messenger messenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
