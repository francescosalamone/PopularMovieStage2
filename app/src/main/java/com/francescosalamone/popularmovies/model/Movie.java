package com.francescosalamone.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Alpha on 18/02/2018.
 */

public class Movie implements Parcelable{
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

    protected Movie(Parcel in) {
        originalTitle = in.readString();
        posterPath = in.readString();
        movieOverview = in.readString();
        usersRating = in.readDouble();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(movieOverview);
        parcel.writeDouble(usersRating);
        parcel.writeString(releaseDate);
    }
}
