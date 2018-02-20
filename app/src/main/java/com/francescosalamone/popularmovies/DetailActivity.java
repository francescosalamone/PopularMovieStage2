package com.francescosalamone.popularmovies;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.francescosalamone.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_WIDTH_URL = "w342";

    private ImageView posterIv;
    private TextView titleTv;
    private TextView overviewTv;
    private TextView ratingTv;
    private TextView releaseDateTv;

    private Movie movie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        posterIv = findViewById(R.id.iv_poster);
        titleTv = findViewById(R.id.title_tv);
        overviewTv = findViewById(R.id.overview_tv);
        ratingTv = findViewById(R.id.rating_tv);
        releaseDateTv = findViewById(R.id.release_date_tv);

        //Setup NavigationBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateUI(Movie movie){
        String posterPath = movie.getPosterPath();

        String posterUrl = POSTER_BASE_URL + POSTER_WIDTH_URL + posterPath;

        Picasso.with(this)
                .load(posterUrl)
                .into(posterIv);

        titleTv.setText(movie.getOriginalTitle());
        overviewTv.setText(movie.getMovieOverview());
        ratingTv.setText(String.valueOf(movie.getUsersRating()));
        releaseDateTv.setText(String.valueOf(movie.getReleaseDate()));

    }
}
