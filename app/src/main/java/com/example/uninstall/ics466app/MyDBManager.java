package com.example.uninstall.ics466app;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.media.MediaPlayer;

public class MyDBManager extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "bookInfo.db";

    public static final String TABLE_TEXTBOOKS = "bookInfo";
    public static final String TABLE_USERTEXTBOOKS = "userBookInfo";
    public static final String TABLE_USERINFO = "userInfo";

    public static final String COLUMN_TB_ISBN = "tb_isbn";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_EDITION = "edition";
    public static final String COLUMN_BINDING = "binding";

    public static final String COLUMN_UR_ISBN = "ur_isbn";
    public static final String COLUMN_TB_USER = "tb_user";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DATE = "dateAdded";

    public static final String COLUMN_INFO_USER = "info_user";

    public MyDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBookTableQuery = "CREATE TABLE " + TABLE_TEXTBOOKS + " (" +
                COLUMN_TB_ISBN + " BIGINT(35) PRIMARY KEY ASC, " +
                COLUMN_TITLE + " VARCHAR(255), " +
                COLUMN_AUTHOR + " VARCHAR(100), " +
                COLUMN_EDITION + " FLOAT(2, 1) " +
                COLUMN_BINDING + " VARCHAR(10) " +
                ");";

        String createUserBookTableQuery = "CREATE TABLE " + TABLE_USERTEXTBOOKS + " (" +
                COLUMN_UR_ISBN + " BIGINT(13), " +
                COLUMN_TB_USER + " VARCHAR(50), " +
                COLUMN_PRICE + " FLOAT(3, 2), " +
                COLUMN_DATE + " DATE, " +
                "PRIMARY KEY(" + COLUMN_UR_ISBN + ", " + COLUMN_TB_USER + "), " +
                "FOREIGN KEY(" + COLUMN_UR_ISBN + ") REFERENCES " + TABLE_TEXTBOOKS + " (" + COLUMN_TB_ISBN + "), " +
                "FOREIGN KEY(" + COLUMN_TB_USER + ") REFERENCES " + TABLE_USERINFO + " (" + COLUMN_INFO_USER + ") " +
                ");";

        String createUserTableQuery = "CREATE TABLE " + TABLE_USERINFO + " (" +
                COLUMN_INFO_USER + " VARCHAR(50) PRIMARY KEY " +
                ");";

        db.execSQL(createBookTableQuery);
        db.execSQL(createUserBookTableQuery);
        db.execSQL(createUserTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXTBOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTEXTBOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERINFO);
        onCreate(db);
    }

    //Add new row in textbook table
    public void addTextBook(TextBooks textBooks) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TB_ISBN, textBooks.get_isbn());
        values.put(COLUMN_PRICE, textBooks.get_price());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TEXTBOOKS, null, values);
        db.close();
    }

    //Delete a textBook from database (probably only use if book is super outdated eg:20 years
    public void deleteTextBook(int ISBN) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TEXTBOOKS + " WHERE " + COLUMN_TB_ISBN + "=" + ISBN + ";" );
    }

    //Add new row in user textbook table
    public void addUserTextBook(TextBooks textBook) {

    }

    //Delete a user posting from the database
    public void deleteUserTextBook(long ISBN, String user) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERTEXTBOOKS + " WHERE " + COLUMN_TB_ISBN + "=" + ISBN +
                " AND " + COLUMN_TB_ISBN + "=\"" + user + "\";");
    }

    //To string method for testing
    public String textbookToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        /*String query = "SELECT * FROM " + TABLE_TEXTBOOKS + " WHERE 1";
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
        }*/

        String[] columns = new String[]{COLUMN_TB_ISBN};
        Cursor c = db.query(TABLE_TEXTBOOKS, columns, null, null, null, null, null);

        int iISBN = c.getColumnIndex(COLUMN_TB_ISBN);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            dbString = dbString + c.getString(iISBN) + "\n";
        }
        db.close();
        return dbString;
    }
}
