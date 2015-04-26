package com.example.uninstall.ics466app;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

/**
 * Created by uninstall on 4/25/15.
 */
public class MyDBManager extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bookInfo.db";
    public static final String TABLE_TEXTBOOKS = "bookInfo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ISBN = "isbn";

    public MyDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_TEXTBOOKS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY " +
                COLUMN_ISBN + " " +
                ");";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
