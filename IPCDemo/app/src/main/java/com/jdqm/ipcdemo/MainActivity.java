package com.jdqm.ipcdemo;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jdqm.ipcdemo.aidl.Book;
import com.jdqm.ipcdemo.aidl.BookManagerService;
import com.jdqm.ipcdemo.aidl.IBookManager;
import com.jdqm.ipcdemo.aidl.INewBookArrivedListener;
import com.jdqm.ipcdemo.binderpool.BinderPoolActivity;
import com.jdqm.ipcdemo.messenger.MessengerService;
import com.jdqm.ipcdemo.provider.ProviderActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MessengerServiceConn conn;
    private BookManagerServiceConn bmsConn;

    private IBookManager bookManager;

    private Messenger mService;

    public void query(View view) {
        ContentResolver contentResolver = getContentResolver();
        contentResolver.query(Uri.parse("content://com.jdqm.ipcdemo.Proveder"),
                null, null, null, null);
    }

    public void providerActivity(View view) {
        startActivity(new Intent(this, ProviderActivity.class));
    }

    public void binderPoolActivity(View view) {
        startActivity(new Intent(this, BinderPoolActivity.class));
    }

    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case Constants.MSG_FROM_SERVER:
                    String reply = msg.getData().getString("reply");
                    Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void statSecondActivity(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    public void bindMessengerService(View view) {
        Intent intent = new Intent(this, MessengerService.class);
        conn = new MessengerServiceConn();
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private class MessengerServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            mService = new Messenger(service);
            Message message = Message.obtain(null, 0);
            Bundle data = new Bundle();
            data.putString("msg", "Hello this is client.");
            message.setData(data);
            message.replyTo = new Messenger(new ClientHandler());
            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


    public void bindBookManagerService(View view) {
        Log.d(TAG, "bindBookManagerService: ");
        Intent intent = new Intent(this, BookManagerService.class);
        bmsConn = new BookManagerServiceConn();
        bindService(intent, bmsConn, BIND_AUTO_CREATE);

    }

    public void addBook(View view) {
        try {
            Book book = new Book(4, "Java");
            bookManager.addBook(book);
            Log.d(TAG, "addBook: " + book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getBookList(View view) {
        try {
            List<Book> bookList = bookManager.getBookList();
            Log.d(TAG, "getBookList: " + bookList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private class BookManagerServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IBookManager.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected: " + bookManager);
            bookListener = new BookListener();
            try {
                bookManager.registerNewBookArrivedListener(bookListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    }

    private BookListener bookListener;

    class BookListener extends INewBookArrivedListener.Stub {

        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Log.d(TAG, "onNewBookArrived: " + book);
        }
    }


    @Override
    protected void onDestroy() {
        if (conn != null) {
            unbindService(conn);
        }

        if (bmsConn != null) {
            unbindService(bmsConn);
        }
        super.onDestroy();
    }
}
