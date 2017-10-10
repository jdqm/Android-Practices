// ISecurityCenter.aidl
package com.jdqm.ipcdemo.aidl;

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}
