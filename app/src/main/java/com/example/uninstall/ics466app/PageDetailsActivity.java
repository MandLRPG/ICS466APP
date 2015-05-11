package com.example.uninstall.ics466app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PageDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_details);
        Bundle b = getIntent().getExtras();

        TextView titleText = (TextView) findViewById(R.id.title);
        TextView priceText = (TextView) findViewById(R.id.price);
        TextView conditionText = (TextView) findViewById(R.id.condition);
        TextView isbnText = (TextView) findViewById(R.id.isbn);
        TextView authorText = (TextView) findViewById(R.id.author);
        TextView editionText = (TextView) findViewById(R.id.edition);
        TextView bindingText = (TextView) findViewById(R.id.binding);
        TextView expText = (TextView) findViewById(R.id.exp);

        Button edit = (Button) findViewById(R.id.editButton);
        Button back = (Button) findViewById(R.id.deleteButton);

        titleText.setText(b.getString("title"));
        priceText.setText("$" + b.getString("price"));
        conditionText.setText(b.getString("condition"));
        isbnText.setText(b.getString("isbn"));
        authorText.setText(b.getString("author"));
        editionText.setText(b.getString("edition"));
        bindingText.setText(b.getString("binding"));
        expText.setText(b.getString("expire"));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountPage = new Intent(getApplicationContext(), AccountPageActivity.class);
                startActivity(accountPage);
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_page_details, menu);
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

    @Override
    public void onBackPressed() {
        Intent accountPage = new Intent(getApplicationContext(), AccountPageActivity.class);
        startActivity(accountPage);
        finish();
    }
}
