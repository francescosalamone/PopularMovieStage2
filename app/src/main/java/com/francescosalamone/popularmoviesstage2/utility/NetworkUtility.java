package com.francescosalamone.popularmoviesstage2.utility;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Alpha on 17/02/2018.
 */

public class NetworkUtility {

    private final static String MOVIE_DB_URL = "http://api.themoviedb.org/3/movie/" ;
    private final static String API_KEY = "api_key";
    private final static String MOST_POPULAR ="popular";
    private final static String TOP_RATED ="top_rated";
    private final static String TRAILERS = "videos";

    /**
     * Build the URL used for get information about movies
     * @param apiKey The Api Key get from the Resource file TheMovieDbAPI.xml
     * @param requestCode The type of sort, 0 = MOST_POPULAR, 1 = TOP_RATED, 2 = TRAILERS, otherwise -1
     * @param idMovie It contains the movie Id if the request is for trailers,
     * @return the url
     */

    public static URL buildUrl (String apiKey, int requestCode, int idMovie){
        String typeOfRequest;
        if(requestCode == 2){
            typeOfRequest = TRAILERS;
        } else if(requestCode == 1){
            typeOfRequest = TOP_RATED;
        } else{
            typeOfRequest = MOST_POPULAR;
        }

        Uri uri;

        if(idMovie == -1) {
            uri = Uri.parse(MOVIE_DB_URL).buildUpon()
                    .appendPath(typeOfRequest)
                    .appendQueryParameter(API_KEY, apiKey)
                    .build();
        } else {
            uri = Uri.parse(MOVIE_DB_URL).buildUpon()
                    .appendPath(Integer.toString(idMovie))
                    .appendPath(typeOfRequest)
                    .appendQueryParameter(API_KEY, apiKey)
                    .build();
        }

        URL url = null;
        try{
            url = new URL(uri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }


    /**
     * This method get the entire content of the MovieDb Http result
     *
     * @param url The url built for get the result
     * @return The entire content with movies data
     * @throws IOException for prevent issues with network
     */

    public static String getContentFromHttp (URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = urlConnection.getInputStream();

            //UseDelimiter \\A means that will read the entire content instead line by line
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
