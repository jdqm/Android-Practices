// IBookManager.aidl
package com.jdqm.ipcdemo.aidl;

import com.jdqm.ipcdemo.aidl.Book;
interface IBookManager {

    List<Book> getBookList();
    void addBook(in Book book);
}
