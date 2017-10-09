// INewBookArrivedListener.aidl
package com.jdqm.ipcdemo.aidl;

import com.jdqm.ipcdemo.aidl.Book;

// Declare any non-default types here with import statements

interface INewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
