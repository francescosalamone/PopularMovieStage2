package com.francescosalamone.popularmovies.model;

import java.util.Date;

/**
 * Created by Alpha on 18/02/2018.
 */

public class Movie {
    private String originalTitle;
    private String posterPath;
    private String movieOverview;
    private double usersRating;
    private String releaseDate;

    public Movie(){

    }

    public Movie(String originalTitle, String posterPath, String movieOverview, double usersRating, String releaseDate){
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.movieOverview = movieOverview;
        this.usersRating = usersRating;
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle(){
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public double getUsersRating() {
        return usersRating;
    }

    public void setUsersRating(double usersRating) {
        this.usersRating = usersRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
