package com.example.uninstall.ics466app;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MyDBManager extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bookInfo.db";
    public static final String TABLE_TEXTBOOKS = "bookInfo";
    public static final String TABLE_USERTEXTBOOKS = "userBookInfo";
    public static final String COLUMN_ISBN = "isbn";
    public static final String COLUMN_DATE = "dateAdded";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_TYPE = "type";

    public MyDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBookTableQuery = "CREATE TABLE " + TABLE_TEXTBOOKS + "(" +
                COLUMN_ISBN + " BIGINT(35) PRIMARY KEY ASC, " +
                COLUMN_TITLE + " varchar(255), " +
                COLUMN_AUTHOR + " varchar(100), " +
                COLUMN_TYPE + " varchar(6) " +
                ");";

        String createUserTableQuery = "CREATE TABLE " + TABLE_USERTEXTBOOKS + "(" +
                COLUMN_ISBN + " LONG(13), " +
                COLUMN_USER + " varchar(50), " +
                COLUMN_PRICE + " FLOAT(3, 2), " +
                COLUMN_DATE + " INT(8), " +
                "PRIMARY KEY(" + COLUMN_ISBN + ", " + COLUMN_USER + ") " +
                ");";

        db.execSQL(createBookTableQuery);
        db.execSQL(createUserTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXTBOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTEXTBOOKS);
        onCreate(db);
    }

    //Add new row in textbook table
    public void addTextBook(TextBooks textBooks) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISBN, textBooks.get_isbn());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TEXTBOOKS, null, values);
        db.close();
    }

    //Delete a textBook from database (probably only use if book is super outdated eg:20 years
    public void deleteTextBook(int ISBN) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TEXTBOOKS + " WHERE " + COLUMN_ISBN + "=" + ISBN + ";" );
    }

    //Add new row in user textbook table
    public void addUserTextBook(TextBooks textBook) {

    }

    //Delete a user posting from the database
    public void deleteUserTextBook(long ISBN, String user) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERTEXTBOOKS + " WHERE " + COLUMN_ISBN + "=" + ISBN +
                " AND " + COLUMN_USER + "=\"" + user + "\";" );
    }

    //To string method for testing
    public String textbookToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TEXTBOOKS + " WHERE 1";
        String query2 = "SELECT * FROM " + TABLE_USERTEXTBOOKS + " WHERE 1";

        //Cursor point to a location in results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in results
        c.moveToFirst();

        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex("ISBN")) != null){
                dbString += c.getInt(c.getColumnIndex("ISBN"));
                dbString += "\n";
            }
        }

        db.close();
        return dbString;
    }
}
