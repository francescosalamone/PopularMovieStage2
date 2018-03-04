package com.francescosalamone.popularmoviesstage2;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.francescosalamone.popularmoviesstage2.databinding.ActivityDetailBinding;
import com.francescosalamone.popularmoviesstage2.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_WIDTH_URL = "w342";

    ActivityDetailBinding mBinding;

    private Movie movie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

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
                .into(mBinding.ivPoster);

        mBinding.titleTv.setText(movie.getOriginalTitle());
        mBinding.overviewTv.setText(movie.getMovieOverview());
        mBinding.ratingTv.setText(String.valueOf(movie.getUsersRating()));
        mBinding.releaseDateTv.setText(String.valueOf(movie.getReleaseDate()));

    }
}
