package com.jdqm.ipcdemo.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.jdqm.ipcdemo.IBinderPool;

/**
 * Created by HP on 2017-10-10.
 */

public class BinderPoolService extends Service {


    private static final IBinder binderPool = new BinderPoolImp();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binderPool;
    }

    private static class BinderPoolImp extends IBinderPool.Stub{
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            if (binderCode == BinderPool.SECURITY_BINDER) {
                binder = new SecurityCenterImp();
            } else if (binderCode == BinderPool.COMPUTE_BINDER) {
                binder = new ComputeImp();
            }
            return binder;
        }
    }
}
