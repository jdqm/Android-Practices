package com.jdqm.ipcdemo.binderpool;

import android.os.RemoteException;

import com.jdqm.ipcdemo.aidl.ICompute;

/**
 * Created by HP on 2017-10-10.
 */

public class ComputeImp extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
