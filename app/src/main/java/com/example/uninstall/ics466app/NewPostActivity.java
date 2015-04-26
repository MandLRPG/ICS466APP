package com.example.uninstall.ics466app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

public class NewPostActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnKeyListener{

    Spinner subjects;
    EditText txtBookBox, isbnBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        subjects = (Spinner) findViewById(R.id.subjectSpinner);
        ArrayAdapter myArray = ArrayAdapter.createFromResource(this, R.array.class_list, android.R.layout.simple_spinner_item);

        subjects.setAdapter(myArray);
        subjects.setOnItemSelectedListener(this);
        txtBookBox = (EditText) findViewById(R.id.enterTxtBook);
        isbnBox = (EditText) findViewById(R.id.enterISBN);

        txtBookBox.setOnKeyListener(this);
        isbnBox.setOnKeyListener(this);


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
                //do stuff that would save this to database table.
                finish();
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

    public void showAlert(View view) {
        AlertDialog.Builder cancelAlert = new AlertDialog.Builder(this);
        cancelAlert.setMessage("Cancel without posting? (Info will be deleted)").create();
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
        cancelAlert.setTitle("Delete New Post Without Posting?");
        cancelAlert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.home_page:
                finish();
                return true;
            case R.id.account_page:
                finish();
                Intent accountPage = new Intent(getApplicationContext(), AccountPageActivity.class);
                startActivity(accountPage);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
