package com.example.uninstall.ics466app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnKeyListener{

    public static String userName = "rinnsio";
    Spinner subjects;
    EditText searchBox;
    String searchType = "";
    String searchText = "";
    MyDBManager dbManager;
    ArrayList<String> postInfo;
    Fragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyDBManager db = new MyDBManager(this, null, null, 3);
        if(!db.userExists(userName)) {
            db.addUserInfo(userName);
        }

        subjects = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter myArray = ArrayAdapter.createFromResource(this, R.array.search_list, android.R.layout.simple_spinner_dropdown_item);
        searchBox = (EditText) findViewById(R.id.search_box);

        subjects.setAdapter(myArray);
        subjects.setOnItemSelectedListener(this);

        searchBox.setOnKeyListener(this);

        // My Account Button
        Button myAccountButton = (Button) findViewById(R.id.myAccountButton);
        myAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountPage = new Intent(getApplicationContext(), AccountPageActivity.class);
                startActivity(accountPage);
            }
        });

        // New Post Button
        Button newPostButton = (Button) findViewById(R.id.newPostButton);
        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPostPage = new Intent(getApplicationContext(), NewPostActivity.class);
                startActivity(newPostPage);
            }
        });

        // Check that the activity is using the layout version with search_fragment
        if (findViewById(R.id.search_fragment) != null) {
            // restored from a previous state
            if (savedInstanceState != null) {
                return;
            }
            // Create a new BookSearchFragment
            StartsUpFragment firstFragment = new StartsUpFragment();

            // Add the fragment to the search_fragment FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.search_fragment, firstFragment).commit();
        }

        // Go Button
        Button goButton = (Button) findViewById(R.id.search_button);
        goButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ProgressDialog pd = ProgressDialog.show(MainActivity.this, "Please Wait...", "Loading...");
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if(searchText.contentEquals("") || postInfo.size() == 0) {
                    newFragment = new NoResultFragment();
                    // replace the current fragment in search_fragment FrameLayout with NoResultFragment
                    fragmentTransaction.replace(R.id.search_fragment, newFragment);
                }
                else{
                    newFragment = new BookSearchFragment();

                    Bundle args = new Bundle();
                    args.putStringArrayList("array", postInfo);
                    newFragment.setArguments(args);
                    // replace the current fragment in search_fragment FrameLayout with NoResultFragment
                    fragmentTransaction.replace(R.id.search_fragment, newFragment);
                }
                fragmentTransaction.commit();
                pd.dismiss();
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Does something when a button is chosen
        dbManager = new MyDBManager(MainActivity.this, null, null, 3);
        // get the text entered by user
        searchText = searchBox.getText().toString();

        searchType = adapterView.getItemAtPosition(i).toString();
        if(searchType.equals("ISBN")){
            long searchISBN = Long.parseLong(searchText);
            //This statement should only change based off of who the user is.  Implement user switching later
            postInfo = dbManager.getRows("SELECT bookInfo.tb_isbn, userBookInfo.price, bookInfo.title, bookInfo.author, bookInfo.edition, bookInfo.binding " +
                    "FROM userBookInfo " +
                    "INNER JOIN bookInfo " +
                    "ON userBookInfo.ur_isbn=bookInfo.tb_isbn " +
                    "WHERE bookInfo.tb_isbn=" + searchISBN);
        }
        else if(searchType.equals("Title")){
            //This statement should only change based off of who the user is.  Implement user switching later
            postInfo = dbManager.getRows("SELECT bookInfo.tb_isbn, userBookInfo.price, bookInfo.title, bookInfo.author, bookInfo.edition, bookInfo.binding " +
                    "FROM userBookInfo " +
                    "INNER JOIN bookInfo " +
                    "ON userBookInfo.ur_isbn=bookInfo.tb_isbn " +
                    "WHERE bookInfo.title LIKE \'%" + searchText + "%\' COLLATE NOCASE");
        }
        else if(searchType.equals("Author")){
            //This statement should only change based off of who the user is.  Implement user switching later
            postInfo = dbManager.getRows("SELECT bookInfo.tb_isbn, userBookInfo.price, bookInfo.title, bookInfo.author, bookInfo.edition, bookInfo.binding " +
                    "FROM userBookInfo " +
                    "INNER JOIN bookInfo " +
                    "ON userBookInfo.ur_isbn=bookInfo.tb_isbn " +
                    "WHERE bookInfo.author LIKE '%" + searchText + "%\' COLLATE NOCASE");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Might want to give a warning message if nothing selected.
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event){
        if((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)){
            //Either make pressing enter close the keyboard or do the same as pressing search button here
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder logout = new AlertDialog.Builder(this);
        logout.setTitle("Exit App?");
        logout.setMessage("The app will be closed.").create();
        logout.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        logout.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        logout.show();
    }

    // button on the top-right corner
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.logout:
                Intent logPage = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logPage);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
