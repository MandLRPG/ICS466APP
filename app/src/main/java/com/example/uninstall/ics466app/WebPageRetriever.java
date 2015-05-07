package com.example.uninstall.ics466app;

import android.app.ProgressDialog;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by uninstall on 4/27/15.
 *
 *
 *
 * NORMALLY THIS FILE WILL NORMALLY BE ON THE SERVER SIDE WHICH WOULD TAKE CARE OF ALL OF
 * THE SEARCHING AND RETURNING OF VALUES TO FILL DATABASE TABLE VALUES.
 */
public class WebPageRetriever extends Thread {
    String isbn;
    String[] bookInfo = {"N/A", "N/A", "N/A", "N/A"};

    /*@Override
    protected String[] doInBackground(String... params) {
        return getBookInfo(params[0]);
    }*/

    public void run() {
        getBookInfo("http://www.isbnsearch.org/isbn/" + isbn);
    }

    public String[] getInfo() {
        return bookInfo;
    }

    public WebPageRetriever() {
        //Default constructor
    }

    public WebPageRetriever(long isbnNumber) {
        isbn = String.valueOf(isbnNumber);
    }

    //Returns a String array containing book title, author, edition #, and binding
    public String[] getBookInfo(String webURL) {
        String foundInfo;
        int numHits = 0;
        Matcher match = null;
        Matcher lastMatch;
        Pattern titlePattern = Pattern.compile("<title>.*?\\|(.*?)</title>");
        Pattern authorPattern = Pattern.compile("Author.*?>(.*?)</p>");
        Pattern editionPattern = Pattern.compile("Edition.*?>(.*?)</p>");
        Pattern bookTypePattern = Pattern.compile("Binding.*?>(.*?)</p>");

        try {
            URL url = new URL(webURL);

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String readLine;

            while((readLine = reader.readLine()) != null && numHits < 4) {
                switch (numHits) {
                    case 0: match = titlePattern.matcher(readLine);
                            break;

                    case 1: match = authorPattern.matcher(readLine);
                            break;

                    case 2: match = editionPattern.matcher(readLine);
                            break;
                }

                lastMatch = bookTypePattern.matcher(readLine);

                if(match.find()) {
                    foundInfo = match.group(1).trim();

                    switch (numHits) {
                        case 0: bookInfo[0] = foundInfo;
                                numHits++;
                                break;

                        case 1: bookInfo[1] = foundInfo;
                                numHits++;
                                break;

                        case 2: bookInfo[2] = foundInfo;
                                numHits++;
                                break;
                    }
                }
                else if(lastMatch.find()) {
                    foundInfo = lastMatch.group(1).trim();
                    bookInfo[3] = foundInfo;
                    numHits = 4;
                }
            }
        }
        catch(FileNotFoundException e) {
            bookInfo[0] = "NOPAGEFOUND";
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
        catch(IOException e) {

        }
        return bookInfo;
    }
}