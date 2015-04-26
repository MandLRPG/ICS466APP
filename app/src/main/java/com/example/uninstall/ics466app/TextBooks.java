package com.example.uninstall.ics466app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by uninstall on 4/25/15.
 */
public class TextBooks {

    private long _isbn;
    private int _dateAdded;
    private double _price;
    private String _title;
    private String _author;
    private String _user;
    private String _type;

    public TextBooks() {
    }

    public TextBooks(long isbn, int price, String type) {
        //Grabs the current date and time
        String dateFormat = "MM-dd HH:mm";
        Calendar current = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getDefault());

        this._isbn = isbn;
        this._price = price;
        this._type = type;
        this._dateAdded = sdf.hashCode();
    }

    //All the getters below until the setters
    public long get_isbn() {
        return this._isbn;
    }

    public int get_dateAdded() {
        return this._dateAdded;
    }

    public double get_price(){
        return this._price;
    }

    public String get_title() {
        return this._title;
    }

    public String get_author() {
        return this._author;
    }

    public String get_user() {
        return this._user;
    }

    public String get_type () {
        return this._type;
    }

    //All the setters
    public void set_dateAdded(int dateAdded) {
        this._dateAdded = dateAdded;
    }

    public void set_price(int price) {
        this._price = price;
    }

    public void set_title(String title) {
        this._title = title;
    }

    public void set_author(String author) {
        this._author = author;
    }

    public void set_user(String user) {
        this._user = user;
    }

    public void set_type(String type) {
        this._type = type;
    }
}
