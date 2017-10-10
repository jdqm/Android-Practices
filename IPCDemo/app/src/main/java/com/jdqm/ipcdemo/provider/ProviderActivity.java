package com.jdqm.ipcdemo.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jdqm.ipcdemo.R;
import com.jdqm.ipcdemo.aidl.Book;

public class ProviderActivity extends AppCompatActivity {

    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("_id", 3);
        values.put("name", "Kotlin");
        contentResolver.insert(BookProvider.BOOK_CONTENT_URI, values);

        Cursor cursor = contentResolver.query(BookProvider.BOOK_CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                Book book = new Book(id, name);
                Log.d(TAG, "book: " + book);
            }
            cursor.close();
        }


        ContentValues values2 = new ContentValues();
        values2.put("name", "张三");
        int update = contentResolver.update(BookProvider.USER_CONTENT_URI, values2, null, null);
        Log.d(TAG, "update: " + update);
        int delete = contentResolver.delete(BookProvider.USER_CONTENT_URI, "_id=?", new String[]{"0"});
        Log.d(TAG, "delete: " + delete);

        Cursor query = contentResolver.query(BookProvider.USER_CONTENT_URI, null, null, null, null);
        if(query !=null) {
          while (query.moveToNext()) {
              int id = query.getInt(0);
              String name = query.getString(1);
              int sex = query.getInt(2);
              Log.d(TAG, "id=" + id + ", name=" + name + ",sex=" + sex);
          }
          query.close();
        }

    }
}
