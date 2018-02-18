package com.francescosalamone.popularmovies.utility;

import android.support.annotation.NonNull;

import com.francescosalamone.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Alpha on 18/02/2018.
 */

public class JsonUtility {

    public static List<Movie> parseMovieJson (String json) throws JSONException{
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RATING = "vote_average";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_RESULTS = "results";

        JSONObject movieJson = new JSONObject(json);
        JSONArray resultsAsArray = movieJson.optJSONArray(MOVIE_RESULTS);

        List<Movie> movieAsList = new ArrayList<Movie>();

        for(int i = 0; i < resultsAsArray.length(); i++){
            JSONObject resultsObj = new JSONObject(resultsAsArray.getString(i));
            Movie movie = new Movie();
            movie.setOriginalTitle(resultsObj.getString(MOVIE_TITLE));
            movie.setPosterPath(resultsObj.getString(MOVIE_POSTER_PATH));
            movie.setMovieOverview(resultsObj.getString(MOVIE_OVERVIEW));
            movie.setUsersRating(resultsObj.getDouble(MOVIE_RATING));
            movie.setReleaseDate(resultsObj.getString(MOVIE_RELEASE_DATE));

            movieAsList.add(movie);
        }


        return movieAsList;
    }
}
