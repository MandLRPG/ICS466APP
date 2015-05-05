package com.example.uninstall.ics466app;

/**
 * Created by uninstall on 3/20/15.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BookSearchFragment extends Fragment{

    Button textbook;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_search_fragment, container, false);
         textbook = (Button) view.findViewById(R.id.button1);
        textbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return view;
    }

    /*public void printDatabase(MyDBManager myManager){
        //String dbString = myManager.textbookToString();
        //textbook.setText(dbString);
    }*/
}
