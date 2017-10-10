package com.jdqm.ipcdemo.binderpool;

import android.os.RemoteException;

import com.jdqm.ipcdemo.aidl.ISecurityCenter;

/**
 * Created by HP on 2017-10-10.
 */

public class SecurityCenterImp extends ISecurityCenter.Stub {

    private static final char SECRET_CODE = '$';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            chars[i] ^= SECRET_CODE; //异或操作（当初学习数字电路时的知识）
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
