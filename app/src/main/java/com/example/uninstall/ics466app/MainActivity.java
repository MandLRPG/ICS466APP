package com.example.uninstall.ics466app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnKeyListener{

    Spinner subjects;
    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subjects = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter myArray = ArrayAdapter.createFromResource(this, R.array.class_list, android.R.layout.simple_spinner_item);
        searchBox = (EditText) findViewById(R.id.search_box);

        subjects.setAdapter(myArray);
        subjects.setOnItemSelectedListener(this);

        searchBox.setOnKeyListener(this);

        Button myAccountButton = (Button) findViewById(R.id.myAccountButton);
        myAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountPage = new Intent(getApplicationContext(), AccountPageActivity.class);
                startActivity(accountPage);
            }
        });

        Button newPostButton = (Button) findViewById(R.id.newPostButton);
        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPostPage = new Intent(getApplicationContext(), NewPostActivity.class);
                startActivity(newPostPage);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Does something when a button is chosen
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Might want to give a warning message if nothing selected.
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event){
        if((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)){
            //do a search like clicking the search button when enter is pressed on keyboard.
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
