package com.example.uninstall.ics466app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TextBooks {

    private long _isbn;
    private String _title;
    private String _author;
    private String _edition;
    private String _binding;

    private String _user;
    private float _price;
    private Date _dateAdded;

    public TextBooks() {
    }

    public TextBooks(long isbn, float price) {
        //Grabs the current date and time
        String dateFormat = "MM-dd HH:mm";
        Calendar current = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getDefault());

        this._isbn = isbn;
        this._price = price;
        //this._dateAdded = sdf.hashCode();
    }

    //All the getters below until the setters
    public long get_isbn() {
        return this._isbn;
    }

    public String get_title() {
        return this._title;
    }

    public String get_author() {
        return this._author;
    }

    public String get_edition () {
        return this._edition;
    }

    public String get_binding () {
        return this._binding;
    }

    public String get_user() {
        return this._user;
    }

    public float get_price(){
        return this._price;
    }

    public Date get_dateAdded() {
        return this._dateAdded;
    }


    //All the setters
    public void set_isbn(long isbn) {
        this._isbn = isbn;
    }

    public void set_title(String title) {
        this._title = title;
    }

    public void set_author(String author) {
        this._author = author;
    }

    public void set_edition(String edition) {
        this._edition = edition;
    }

    public void set_binding(String binding) {
        this._binding = binding;
    }

    public void set_user(String user) {
        this._user = user;
    }

    public void set_price(float price) {
        this._price = price;
    }

    public void set_dateAdded(Date dateAdded) {
        this._dateAdded = dateAdded;
    }
}
