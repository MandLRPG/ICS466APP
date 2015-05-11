package com.example.uninstall.ics466app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class AccountPageActivity extends ActionBarActivity {

    MyDBManager dbManager;
    ArrayList<String> postInfo;
    Fragment fragment;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog pd = ProgressDialog.show(AccountPageActivity.this, "Please Wait...", "Loading...");
        setContentView(R.layout.activity_account_page);

        dbManager = new MyDBManager(this, null, null, 3);

        //This statement should only change based off of who the user is.  Implement user switching later
        postInfo = dbManager.getRows("SELECT bookInfo.tb_isbn, userBookInfo.price, bookInfo.title, bookInfo.author, bookInfo.edition, bookInfo.binding " +
                "FROM userBookInfo " +
                "INNER JOIN bookInfo " +
                "ON userBookInfo.ur_isbn=bookInfo.tb_isbn " +
                "WHERE tb_user=\'" + MainActivity.userName + "\'");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(postInfo.size() == 0) {
            fragment = new NoResultFragment();
        }
        else {
            fragment = new BookSearchFragment();
            Bundle args = new Bundle();
            args.putStringArrayList("array", postInfo);
            fragment.setArguments(args);
        }

        fragmentTransaction.add(R.id.account_fragment, fragment);
        fragmentTransaction.commit();
        pd.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_page, menu);
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
            case R.id.post_page:
                Intent postPage = new Intent(getApplicationContext(), NewPostActivity.class);
                startActivity(postPage);
                finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
