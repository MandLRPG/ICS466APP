package com.example.uninstall.ics466app;

/**
 * Created by uninstall on 4/25/15.
 */
public class TextBooks {

    private int _isbn;
    private int _dateAdded;
    private double _price;
    private String _title;
    private String _author;
    private String _user;
    private String _type;

    public TextBooks() {
    }

    public TextBooks(String type) {
        this._type = type;
    }

    public void set_dateAdded(int dateAdded) {
        this._dateAdded = dateAdded;
    }

    public void set_price(int price) {
        this._price = price;
    }
}
