package com.example.uninstall.ics466app;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
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
    boolean isDone = false;

    /*@Override
    protected String[] doInBackground(String... params) {
        return getBookInfo(params[0]);
    }*/

    public void run() {
        getBookInfo("http://www.isbnsearch.org/isbn/" + isbn);
        isDone = true;
    }

    public boolean getDone() {
        return isDone;
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
        Matcher lastMatch = null;
        InputStream iStream = null;
        Pattern titlePattern = Pattern.compile("<title>.*?\\|(.*?)</title>");
        Pattern authorPattern = Pattern.compile("Author.*?>(.*?)</p>");
        Pattern editionPattern = Pattern.compile("Edition.*?>(.*?)</p>");
        Pattern bookTypePattern = Pattern.compile("Binding.*?>(.*?)</p>");

        try {
            URL url = new URL(webURL);
            /*HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();

            String contentAsString = readIt(is, 1000);*/
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String readLine = "";

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
                        case 0: if(foundInfo.equals("ISBN Search - Page Not Found")) {
                                    System.out.println("BREAK 3");
                                    bookInfo[0] = "No such book";
                                    return bookInfo;
                                }
                                else if(foundInfo.equals("ISBN Search")) {
                                    System.out.println("BREAK 3");
                                    bookInfo[0] = "No ISBN number";
                                    return bookInfo;
                                }
                                bookInfo[0] = foundInfo;
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
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            if(iStream != null) {
                try {
                    iStream.close();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookInfo;
    }

    /*public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }*/

    /*@Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        NewPostActivity.setBookInfo(result);
    }*/
}