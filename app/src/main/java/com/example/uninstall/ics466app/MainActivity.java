package com.example.uninstall.ics466app;

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


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnKeyListener{

    public static String userName = "rinnsio";
    Spinner subjects;
    EditText searchBox;
    String searchType = "";

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
            BookSearchFragment firstFragment = new BookSearchFragment();

            // Add the fragment to the search_fragment FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.search_fragment, firstFragment).commit();
        }

        // Go Button
        Button goButton = (Button) findViewById(R.id.search_button);
        goButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // get the text entered by user
                String searchText = searchBox.getText().toString();
                if(searchText.contentEquals("whatever we have in our database")){

                }
                else{
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    // avoid overlap fragment
                    if(fragmentManager.getBackStackEntryCount() > 0)
                    {
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }

                    Fragment noResult = new NoResultFragment();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // replace the current fragment in search_fragment FrameLayout with NoResultFragment
                    fragmentTransaction.replace(R.id.search_fragment, noResult);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Does something when a button is chosen
        searchType = adapterView.getItemAtPosition(i).toString();
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
