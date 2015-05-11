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

import java.util.ArrayList;

public class BookSearchFragment extends Fragment{

    Button textBook1, textBook2, textBook3, textBook4, textBook5, textBook6, textBook7, textBook8, textBook9, textBook10;;
    ArrayList<String> userBookInfo;
    int numPostings = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_search_fragment, container, false);
        Bundle args = getArguments();

        if(args != null) {
            userBookInfo = args.getStringArrayList("array");
            numPostings = userBookInfo.size()/6;
        }

        textBook1 = (Button) view.findViewById(R.id.button1);
        textBook2 = (Button) view.findViewById(R.id.button2);
        textBook3 = (Button) view.findViewById(R.id.button3);
        textBook4 = (Button) view.findViewById(R.id.button4);
        textBook5 = (Button) view.findViewById(R.id.button5);
        textBook6 = (Button) view.findViewById(R.id.button6);
        textBook7 = (Button) view.findViewById(R.id.button7);
        textBook8 = (Button) view.findViewById(R.id.button8);
        textBook9 = (Button) view.findViewById(R.id.button9);
        textBook10 = (Button) view.findViewById(R.id.button10);

        textBook1.setVisibility(View.GONE);
        textBook2.setVisibility(View.GONE);
        textBook3.setVisibility(View.GONE);
        textBook4.setVisibility(View.GONE);
        textBook5.setVisibility(View.GONE);
        textBook6.setVisibility(View.GONE);
        textBook7.setVisibility(View.GONE);
        textBook8.setVisibility(View.GONE);
        textBook9.setVisibility(View.GONE);
        textBook10.setVisibility(View.GONE);

        switch(numPostings) {
            case 10:
                textBook10.setVisibility(View.VISIBLE);
                textBook10.setText(userBookInfo.get(56) + "\n$" + userBookInfo.get(55));
                textBook10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            case 9:
                textBook9.setVisibility(View.VISIBLE);
                textBook9.setText(userBookInfo.get(50) + "\n$" + userBookInfo.get(49));
                textBook9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            case 8:
                textBook8.setVisibility(View.VISIBLE);
                textBook8.setText(userBookInfo.get(44) + "\n$" + userBookInfo.get(43));
                textBook8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            case 7:
                textBook7.setVisibility(View.VISIBLE);
                textBook7.setText(userBookInfo.get(38) + "\n$" + userBookInfo.get(37));
                textBook7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            case 6:
                textBook6.setVisibility(View.VISIBLE);
                textBook6.setText(userBookInfo.get(32) + "\n$" + userBookInfo.get(31));
                textBook6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            case 5:
                textBook5.setVisibility(View.VISIBLE);
                textBook5.setText(userBookInfo.get(26) + "\n$" + userBookInfo.get(25));
                textBook5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            case 4:
                textBook4.setVisibility(View.VISIBLE);
                textBook4.setText(userBookInfo.get(20) + "\n$" + userBookInfo.get(19));
                textBook4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            case 3:
                textBook3.setVisibility(View.VISIBLE);
                textBook3.setText(userBookInfo.get(14) + "\n$" + userBookInfo.get(13));
                textBook3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            case 2:
                textBook2.setVisibility(View.VISIBLE);
                textBook2.setText(userBookInfo.get(8) + "\n$" + userBookInfo.get(7));
                textBook2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            case 1:
                textBook1.setVisibility(View.VISIBLE);
                textBook1.setText(userBookInfo.get(2) + "\n$" + userBookInfo.get(1));
                textBook1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        }
        return view;
    }
}
