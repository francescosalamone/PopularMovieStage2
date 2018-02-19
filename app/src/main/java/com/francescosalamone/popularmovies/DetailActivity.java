package com.francescosalamone.popularmovies;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.francescosalamone.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.sql.Date;

public class DetailActivity extends AppCompatActivity {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_WIDTH_URL = "w342";

    ImageView poster_iv;
    TextView title_tv;
    TextView overview_tv;
    TextView rating_tv;
    TextView releaseDate_tv;

    Movie movie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        poster_iv = findViewById(R.id.iv_poster);
        title_tv = findViewById(R.id.title_tv);
        overview_tv = findViewById(R.id.overview_tv);
        rating_tv = findViewById(R.id.rating_tv);
        releaseDate_tv = findViewById(R.id.release_date_tv);

        Intent intent = getIntent();
        if(intent == null){
            finish();
        }

        movie = intent.getParcelableExtra("Movie");
        if(movie ==  null){
            finish();
        }

        populateUI(movie);

    }

    private void populateUI(Movie movie){
        String posterPath = movie.getPosterPath();

        String posterUrl = POSTER_BASE_URL + POSTER_WIDTH_URL + posterPath;

        Picasso.with(this)
                .load(posterUrl)
                .into(poster_iv);

        title_tv.setText(movie.getOriginalTitle());
        overview_tv.setText(movie.getMovieOverview());
        rating_tv.setText(String.valueOf(movie.getUsersRating()));
        releaseDate_tv.setText(String.valueOf(movie.getReleaseDate()));

    }
}
