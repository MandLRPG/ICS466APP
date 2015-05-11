package com.example.uninstall.ics466app;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import java.util.ArrayList;

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

    //Creates the database with the following tables if the database doesn't exist/has a newer version
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBookTableQuery = "CREATE TABLE " + TABLE_TEXTBOOKS + " (" +
                COLUMN_TB_ISBN + " BIGINT(35) PRIMARY KEY ASC, " +
                COLUMN_TITLE + " VARCHAR(255), " +
                COLUMN_AUTHOR + " VARCHAR(100), " +
                COLUMN_EDITION + " FLOAT(2, 1), " +
                COLUMN_BINDING + " VARCHAR(10) " +
                ");";

        //Foreign keys prevents creating any destroying of links between tables
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

    //When the database verison is changed, this method is called to update the database
    //Simply deletes all old tables.  Shouldn't do this on an actual database.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXTBOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTEXTBOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERINFO);
        onCreate(db);
    }

    //ALL OF THE METHODS TO ADD ROWS INTO DIFFERENT TABLES
    //Add new row in textbook table
    public void addTextBook(TextBooks textBooks) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TB_ISBN, textBooks.get_isbn());
        values.put(COLUMN_TITLE, textBooks.get_title());
        values.put(COLUMN_AUTHOR, textBooks.get_author());
        values.put(COLUMN_EDITION, Float.parseFloat(textBooks.get_edition()));
        values.put(COLUMN_BINDING, textBooks.get_binding());

        SQLiteDatabase db = getWritableDatabase();
        //Inserts a row into the textbooks database
        db.insert(TABLE_TEXTBOOKS, null, values);
        db.close();
    }

    //Add new row into the user-textbook relational table
    public void addUserTextBook(TextBooks textBooks){
        ContentValues values = new ContentValues();
        values.put(COLUMN_UR_ISBN, textBooks.get_isbn());
        values.put(COLUMN_TB_USER, textBooks.get_user());
        values.put(COLUMN_PRICE, textBooks.get_price());
        values.put(COLUMN_DATE, textBooks.get_dateAdded());

        SQLiteDatabase db = getWritableDatabase();
        //Inserts a row into the textbooks database
        db.insert(TABLE_USERTEXTBOOKS, null, values);
        db.close();
    }

    //Add new row to the users present in the system
    //Should also have information such as user name, email, tel#, etc.  NOT PASSWORDS though.
    public void addUserInfo(String userName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_INFO_USER, userName);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_USERINFO, null, values);
        db.close();
    }

    //ALL OF THE METHODS TO DELETE ROWS IN DIFFERENT TABLES
    //Delete a textBook from database (probably only use if book is super outdated eg:20 years
    public void deleteTextBook(long ISBN) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TEXTBOOKS + " WHERE " + COLUMN_TB_ISBN + "=" + ISBN + ";");
        db.close();
    }

    //Delete a user posting from the database
    public void deleteUserTextBook(long ISBN, String user) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERTEXTBOOKS + " WHERE " + COLUMN_UR_ISBN + "=" + ISBN +
                " AND " + COLUMN_TB_USER + "=\"" + user + "\";");
        db.close();
    }

    //Only used if a user is no longer supported by the system. (EX: gradated 5 years ago)
    //Should not be called by anything on the device so this method should be depreciated.
    //Instead, a server-side script should be used to handle this.
    /*public void removeUserInfo(String userName) {

    }*/

    //ALL OF THE METHODS TO GET ROWS IN DIFFERENT TABLES
    //Get the information of a row
    public ArrayList getRows(String query) {
        char i = 0;
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast() && i < 10) {
            result.add(String.valueOf(c.getLong(0)));
            result.add(String.valueOf(c.getFloat(1)));
            result.add(c.getString(2));
            result.add(c.getString(3));
            result.add(String.valueOf(c.getFloat(4)));
            result.add(c.getString(5));
            c.moveToNext();
            i++;
        }

        c.close();
        db.close();
        return result;
    }

    public String[] getTxtBookRow(long isbnNumber) {
        String[] bookInfo = {"", "", "", ""};
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT title, author, edition, binding " +
                "FROM bookInfo WHERE tb_isbn=" + isbnNumber, null);
        c.moveToFirst();
        bookInfo[0] = c.getString(0);
        bookInfo[1] = c.getString(1);
        bookInfo[2] = String.valueOf(c.getFloat(2));
        bookInfo[3] = c.getString(3);
        c.close();
        return bookInfo;
    }

    public boolean userExists(String userName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT info_user FROM userInfo WHERE info_user=\'" + userName + "\'", null);
        c.moveToFirst();
        if(c.isAfterLast()) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean txtBookExists(long isbnNumber) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT tb_isbn FROM bookInfo WHERE tb_isbn=" + isbnNumber, null);
        c.moveToFirst();
        if(c.isAfterLast()) {
            c.close();
            return false;
        }
        c.close();
        return true;
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
        c.close();
        db.close();
        return dbString;
    }
}
