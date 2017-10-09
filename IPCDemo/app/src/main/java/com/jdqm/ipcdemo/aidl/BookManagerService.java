package com.jdqm.ipcdemo.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by HP on 2017-10-9.
 */

public class BookManagerService extends Service {

    private static final String TAG = "BookManagerService";

    private BookManager bookManager;

    private AtomicBoolean serviceDestroyed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> books = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<INewBookArrivedListener> listeners = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        bookManager = new BookManager();
        books.add(new Book(1, "Android"));
        books.add(new Book(2, "IOS"));
        books.add(new Book(3, "Kotlin"));
        new Thread(new ServiceWork()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");

        //增加客户端权限验证
        int check = checkCallingOrSelfPermission("jdqm.permission.ACCESS_BOOK_MANAGER");
        Log.d(TAG, "check: " + (check == PackageManager.PERMISSION_DENIED));
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return bookManager;
    }

    private class BookManager extends IBookManager.Stub {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return books;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            //这个方法运行在Binder线程池，如果客户端在主线程调用，线程被挂起，这个方法耗时较长的话，就有可能引起ARN异常
            //SystemClock.sleep(10000);
            books.add(book);
        }

        @Override
        public void registerNewBookArrivedListener(INewBookArrivedListener listener) throws RemoteException {
            boolean register = listeners.register(listener);
            Log.d(TAG, "registerNewBookArrivedListener: " + register);
        }

        @Override
        public void unRegisterNewBookArrivedListener(INewBookArrivedListener listener) throws RemoteException {
            boolean unregister = listeners.unregister(listener);
            Log.d(TAG, "unRegisterNewBookArrivedListener: " + unregister);
        }
    }

    class ServiceWork implements Runnable {

        @Override
        public void run() {

            while (!serviceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Book book = new Book(5, "HTML");
                books.add(book);
                int N = listeners.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    try {
                        INewBookArrivedListener l = listeners.getBroadcastItem(i);
                        l.onNewBookArrived(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                listeners.finishBroadcast();
            }

        }
    }

    @Override
    public void onDestroy() {
        serviceDestroyed.set(false);
        super.onDestroy();
    }
}
