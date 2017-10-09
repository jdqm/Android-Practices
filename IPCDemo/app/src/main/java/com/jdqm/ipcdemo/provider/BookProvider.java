package com.jdqm.ipcdemo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by HP on 2017-10-9.
 */

public class BookProvider extends ContentProvider {

    private static final String TAG = "BookProvider";

    private static final String AUTHORITY = "com.jdqm.ipcdemo.Proveder";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "book", 0);
        sUriMatcher.addURI(AUTHORITY, "user", 1);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: " + Thread.currentThread());
        db = new DBOpenHelper(getContext()).getWritableDatabase();
        db.delete(DBOpenHelper.BOOK_TABLE_NAME, null, null);
        db.delete(DBOpenHelper.USER_TABLE_NAME, null, null);
        db.execSQL("insert into book values(1, 'java')");
        db.execSQL("insert into book values(2, 'C++')");
        db.execSQL("insert into user values(3, '小明')");
        db.execSQL("insert into user values(4, 'Jdqm')");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: " + Thread.currentThread());
        String tableName = getTableName(uri);
        if (null == tableName) {
            throw new IllegalArgumentException("UnSupported Uri");
        }
        Cursor cursor= db.query(tableName, projection, selection, null, null, sortOrder, null);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        if (null == tableName) {
            throw new IllegalArgumentException("UnSupported Uri");
        }
        db.insert(tableName, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (null == tableName) {
            throw new IllegalArgumentException("UnSupported Uri");
        }
        int count = db.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (null == tableName) {
            throw new IllegalArgumentException("UnSupported Uri");
        }
        int row = db.update(tableName, values, selection, selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    private String getTableName(Uri uri) {
        int match = sUriMatcher.match(uri);
        if (match == 0) {
            return DBOpenHelper.BOOK_TABLE_NAME;
        } else if (match == 1) {
            return DBOpenHelper.USER_TABLE_NAME;
        }
        return null;
    }
}
