package com.example.uninstall.ics466app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class NewPostActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnKeyListener{

    Spinner subjects;
    EditText isbnBox, priceBox, txtBookBox, authorBox;
    MyDBManager dbManager;
    static String[] bookInfo = {"", "", "", ""};
    long isbnNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        subjects = (Spinner) findViewById(R.id.subjectSpinner);
        ArrayAdapter myArray = ArrayAdapter.createFromResource(this, R.array.class_list, android.R.layout.simple_spinner_item);
        isbnBox = (EditText) findViewById(R.id.enterISBN);
        priceBox = (EditText) findViewById(R.id.enterPrice);
        txtBookBox = (EditText) findViewById(R.id.enterTxtBook);
        authorBox = (EditText) findViewById(R.id.enterAuthor);
        dbManager = new MyDBManager(this, null, null, 1);

        subjects.setAdapter(myArray);
        subjects.setOnItemSelectedListener(this);

        isbnBox.setOnKeyListener(this);
        priceBox.setOnKeyListener(this);
        txtBookBox.setOnKeyListener(this);
        authorBox.setOnKeyListener(this);

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(v);
            }
        });

        Button postButton = (Button) findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connect.getActiveNetworkInfo();

                if(isbnBox.getText().toString().equals("")) {
                    //edit a blank textview that shows that this box needs to be filled
                }
                else if(networkInfo != null && networkInfo.isConnected()) {
                    //String webURL = "http://www.isbnsearch.org/isbn/" + isbnBox.getText().toString();
                    //do stuff that would save this to database table.
                    WebPageRetriever retrieve = new WebPageRetriever(Long.parseLong(isbnBox.getText().toString()));
                    retrieve.start();
                    while(! retrieve.isDone) {

                    }
                    bookInfo = retrieve.getInfo();
                    //new WebPageRetriever().execute(webURL);

                    switch (bookInfo[0]) {
                        case "No such book":
                            showError(v, 0);
                            break;

                        case "No ISBN number":
                            showError(v, 1);
                            break;

                        default:
                            showConfirmation(v, bookInfo, priceBox.getText().toString());
                    }
                }
                else {
                    showError(v, 3);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
        //Does something when an item is selected from the list.
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView){
        //When nothing in the list is selected
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event){
        if((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)){
            //Either make pressing enter close the keyboard or do the same as pressing search button here
            return true;
        }
        return false;
    }

    //Unused database code
    public void addToDatabase() {
        TextBooks textBook = new TextBooks(Long.parseLong(isbnBox.getText().toString()), 50, "temp");
        dbManager.addTextBook(textBook);
        dbManager.addUserTextBook(textBook);
    }

    public void showAlert(View view) {
        AlertDialog.Builder cancelAlert = new AlertDialog.Builder(this);
        cancelAlert.setTitle("Cancel Posting?");
        cancelAlert.setMessage("Draft will be deleted.").create();
        cancelAlert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        cancelAlert.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        cancelAlert.show();
    }

    public void showError(View view, int eNum) {
        AlertDialog.Builder error = new AlertDialog.Builder(this);
        error.setTitle("Error");

        switch(eNum) {
            case 0: error.setMessage("No book with such ISBN number").create();
                    break;

            case 1: error.setMessage("ISBN field is blank or invalid input received").create();
                    break;

            case 2: error.setMessage("No network connection available.");

        }
        error.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        error.show();
    }

    public void showConfirmation(View view, String[] bookInfo, String price) {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setTitle("Create Posting?");
        confirm.setMessage("Create the following posting?\n\n\n" +
                Html.fromHtml(getString(R.string.price)) + price + "\n\n" +
                Html.fromHtml(getString(R.string.title)) + bookInfo[0] + "\n\n" +
                Html.fromHtml(getString(R.string.author)) + bookInfo[1] + "\n\n" +
                Html.fromHtml(getString(R.string.edition)) + bookInfo[2] + "\n\n" +
                Html.fromHtml(getString(R.string.cover)) + bookInfo[3]).create();

        confirm.setNegativeButton("Post!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToDatabase();
                finish();
            }
        });

        confirm.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });
        confirm.show();
    }

    public static void setBookInfo(String[] retrievedInfo) {
        bookInfo = retrievedInfo;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_post, menu);
        return true;
    }

    // button on the top-right corner
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // go to different pages once button clicked
        switch (item.getItemId()) {
            case R.id.home_page:
                finish();
                return true;
            case R.id.account_page:
                Intent accountPage = new Intent(getApplicationContext(), AccountPageActivity.class);
                startActivity(accountPage);
                finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}