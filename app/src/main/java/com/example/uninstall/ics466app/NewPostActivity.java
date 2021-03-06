package com.example.uninstall.ics466app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewPostActivity extends ActionBarActivity implements View.OnKeyListener{

    static String[] bookInfo = {"", "", "", ""};

    EditText isbnBox, priceBox, txtBookBox, authorBox;
    MyDBManager dbManager;
    Button cancelButton;
    TextView emptyISBN, emptyPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        isbnBox = (EditText) findViewById(R.id.enterISBN);
        emptyISBN = (TextView) findViewById(R.id.error1);
        emptyPrice = (TextView) findViewById(R.id.error2);
        priceBox = (EditText) findViewById(R.id.enterPrice);
        txtBookBox = (EditText) findViewById(R.id.enterTxtBook);
        authorBox = (EditText) findViewById(R.id.enterAuthor);
        dbManager = new MyDBManager(this, null, null, 2);

        isbnBox.setOnKeyListener(this);
        priceBox.setOnKeyListener(this);
        txtBookBox.setOnKeyListener(this);
        authorBox.setOnKeyListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(v);
            }
        });

        Button postButton = (Button) findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connect.getActiveNetworkInfo();

                final String isbnNumber = isbnBox.getText().toString();
                final String bookPrice = priceBox.getText().toString();

                //All missing text errors generated here
                emptyISBN.setText("");
                emptyPrice.setText("");

                if(isbnNumber.length() == 0){
                    emptyISBN.setText("*ISBN must be entered.\n");
                }
                if(bookPrice.length() == 0){
                    emptyPrice.setText("*Price must be entered.\n");
                }
                if(isbnNumber.length() != 0 && bookPrice.length() != 0) {
                    final WebPageRetriever retrieve = new WebPageRetriever(Long.parseLong(isbnNumber));

                    if (networkInfo != null && networkInfo.isConnected()) {
                        final ProgressDialog pd = ProgressDialog.show(NewPostActivity.this, "Please Wait...", "Searching...");

                        //If book doesn't already exist in database, pull it up from the web
                        //AGAIN, this should normally be done server-side
                        if(!dbManager.txtBookExists(Long.parseLong(isbnBox.getText().toString()))) {
                            final Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    bookInfo = retrieve.getInfo();
                                    if (bookInfo[0].equals("NOPAGEFOUND")) {
                                        //If no such url exists (IE: no such ISBN numbered book)
                                        showError(v, 0);
                                    } else {
                                        //Boxes properly filled, show confirmation if user wants to post
                                        TextBooks newBook = new TextBooks(Long.parseLong(isbnBox.getText().toString()), bookInfo[0], bookInfo[1], bookInfo[2], bookInfo[3]);
                                        dbManager.addTextBook(newBook);
                                        showConfirmation(v, bookInfo, bookPrice);
                                    }
                                    pd.dismiss();
                                }
                            };
                            Thread myThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    retrieve.run();
                                    try {
                                        retrieve.join();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(0);
                                }
                            });
                            myThread.start();
                        }
                        //Else just pull the book info from the database
                        else {
                            bookInfo = dbManager.getTxtBookRow(Long.parseLong(isbnBox.getText().toString()));
                            showConfirmation(v, bookInfo, bookPrice);
                            pd.dismiss();
                        }
                    } else {
                        //No network connection
                        showError(v, 2);
                    }
                }
            }
        });
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event){
        if((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)){
            //Either make pressing enter close the keyboard or do the same as pressing search button here
            return true;
        }
        return false;
    }

    //Add to database
    public void addToDatabase() {
        TextBooks textBook = new TextBooks(Long.parseLong(isbnBox.getText().toString()), bookInfo[0], bookInfo[1],
                bookInfo[2], bookInfo[3], MainActivity.userName, Float.parseFloat(priceBox.getText().toString()), "2015-05-08 15:00:00");
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
            case 0: error.setMessage("No book with such ISBN number.").create();
                    break;

            /*case 1: error.setMessage("ISBN field is blank or an invalid number was entered.").create();
                    error.setTitle("Invalid Inputs");
                    break;*/

            case 2: error.setMessage("No network connection available.");

        }
        error.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
        confirm.setMessage(Html.fromHtml("Create the following posting?" +
                getString(R.string.price) + price +
                getString(R.string.title) + bookInfo[0] +
                getString(R.string.author) + bookInfo[1] +
                getString(R.string.edition) + bookInfo[2] +
                getString(R.string.binding) + bookInfo[3])).create();

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

    //Storing the retrieved book information into the database.
    public static void setBookInfo(String[] retrievedInfo) {
        bookInfo = retrievedInfo;
    }

    //

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
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
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
}