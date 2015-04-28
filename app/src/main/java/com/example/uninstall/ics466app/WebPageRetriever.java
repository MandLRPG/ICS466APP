package com.example.uninstall.ics466app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class WebPageRetriever {
    String isbn;

    public WebPageRetriever() {
        //Default constructor
    }

    public WebPageRetriever(long isbnNumber) {
        isbn = String.valueOf(isbnNumber);
    }

    //Returns a String array containing book title, author,
    public String[] getBookInfo() {
        String[] bookInfo = {"", "", "", ""};
        String foundInfo;
        String inputLine;
        int numHits = 0;
        BufferedReader reader = null;
        Matcher match = null;
        String webURL = "http://www.isbnsearch.org/isbn/" + isbn;
        Pattern titlePattern = Pattern.compile("<title>.*?\\|(.*?)<title>");
        Pattern authorPattern = Pattern.compile("Author.*?>(.*?)</p>");
        Pattern editionPattern = Pattern.compile("Edition.*?>(.*?)</p>");
        Pattern bookTypePattern = Pattern.compile("Binding.*?>(.*?)</p>");

        try {
            URL url = new URL(webURL);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            while((inputLine = reader.readLine()) != null && numHits < 4) {
                switch (numHits) {
                    case 0: match = titlePattern.matcher(inputLine);
                            break;

                    case 1: match = authorPattern.matcher(inputLine);
                            break;

                    case 2: match = editionPattern.matcher(inputLine);
                            break;

                    case 3: match = bookTypePattern.matcher(inputLine);
                            break;
                }

                if(match.find()) {
                    foundInfo = match.group(1).trim();

                    switch (numHits) {
                        case 0: if(foundInfo.equals("ISBN Search - Page Not Found")) {
                                    bookInfo[0] = "No such book";
                                    return bookInfo;
                                }
                                else if(foundInfo.equals("ISBN Search")) {
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

                        case 3: bookInfo[3] = foundInfo;
                                numHits++;
                                break;
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookInfo;
    }
}