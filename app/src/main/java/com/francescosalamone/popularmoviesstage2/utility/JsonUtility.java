package com.francescosalamone.popularmoviesstage2.utility;

import com.francescosalamone.popularmoviesstage2.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpha on 18/02/2018.
 */

public class JsonUtility {

    public static List<String> parseTrailerJson (String json) throws JSONException {
        final String TRAILER_KEY = "key";
        final String TRAILER_RESULT = "results";

        JSONObject trailerJson = new JSONObject(json);
        if(trailerJson.has(TRAILER_RESULT)){
            JSONArray resultsAsArray = trailerJson.optJSONArray(TRAILER_RESULT);

            List<String> trailersAsList = new ArrayList<>();

            for(int i = 0; i < resultsAsArray.length(); i++){
                JSONObject resultObj = new JSONObject(resultsAsArray.getString(i));
                trailersAsList.add(resultObj.optString(TRAILER_KEY));
            }
            return trailersAsList;
        }
        return null;
    }

    public static List<Movie> parseMovieJson (String json) throws JSONException{
        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RATING = "vote_average";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_RESULTS = "results";

        JSONObject movieJson = new JSONObject(json);
        if(movieJson.has(MOVIE_RESULTS)){
            JSONArray resultsAsArray = movieJson.optJSONArray(MOVIE_RESULTS);

            List<Movie> movieAsList = new ArrayList<>();

            for(int i = 0; i < resultsAsArray.length(); i++){
                JSONObject resultsObj = new JSONObject(resultsAsArray.getString(i));
                Movie movie = new Movie();
                List<String> trailers = new ArrayList<>();
                movie.setIdMovie(resultsObj.optInt(MOVIE_ID));
                movie.setOriginalTitle(resultsObj.optString(MOVIE_TITLE));
                movie.setPosterPath(resultsObj.optString(MOVIE_POSTER_PATH));
                movie.setMovieOverview(resultsObj.optString(MOVIE_OVERVIEW));
                movie.setUsersRating(resultsObj.optDouble(MOVIE_RATING));
                movie.setReleaseDate(resultsObj.optString(MOVIE_RELEASE_DATE));

                movieAsList.add(movie);
            }

            return movieAsList;
        } else {
            return null;
        }
    }
}
