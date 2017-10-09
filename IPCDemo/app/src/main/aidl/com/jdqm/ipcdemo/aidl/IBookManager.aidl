// IBookManager.aidl
package com.jdqm.ipcdemo.aidl;

import com.jdqm.ipcdemo.aidl.Book;
import com.jdqm.ipcdemo.aidl.INewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerNewBookArrivedListener(INewBookArrivedListener listener);
    void unRegisterNewBookArrivedListener(INewBookArrivedListener listener);
}
