package com.francescosalamone.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.francescosalamone.popularmoviesstage2.databinding.ActivityDetailBinding;
import com.francescosalamone.popularmoviesstage2.model.Movie;
import com.francescosalamone.popularmoviesstage2.utility.JsonUtility;
import com.francescosalamone.popularmoviesstage2.utility.NetworkUtility;
import com.francescosalamone.popularmoviesstage2.utility.PosterAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_WIDTH_URL = "w342";
    private static final int TRAILER_LOADER = 2016;
    public static final int UPDATED_OBJECT = 188;
    private static final int DEFAULT_POSITION_VALUE = -1;

    private String movieDbApiKey;
    private int requestCode = 2;
    private int idMovie = -1;
    private int position;

    ActivityDetailBinding mBinding;

    private Movie movie = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        movieDbApiKey = BuildConfig.apiV3;

        //Setup NavigationBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent == null){
            finish();
        }

        movie = intent.getParcelableExtra("Movie");
        position = intent.getIntExtra("MoviePosition", DEFAULT_POSITION_VALUE);
        if(movie ==  null || position == -1){
            finish();
        }

        //Check if already exist the trailers, if no new http request is needed
        if(movie.getTrailerKey().isEmpty()){
            idMovie = movie.getIdMovie();
            updateTrailers();
        }

        populateUI(movie);

    }

    private void updateTrailers(){
        //I check, before the HTTP request, if we have an internet connection available
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            try {
                if (getSupportLoaderManager().getLoader(TRAILER_LOADER).isStarted())
                    getSupportLoaderManager().restartLoader(TRAILER_LOADER, null, this);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getSupportLoaderManager().initLoader(TRAILER_LOADER, null, this);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                if(movieDbApiKey == null || TextUtils.isEmpty(movieDbApiKey))
                    return null;
                try{
                    return NetworkUtility.getContentFromHttp(NetworkUtility.buildUrl(movieDbApiKey, requestCode, idMovie) );
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        List<String> trailersAsList = new ArrayList<>();
        try {
            trailersAsList = JsonUtility.parseTrailerJson(data);

        } catch (JSONException e){
            e.printStackTrace();
        }

        if(trailersAsList == null || trailersAsList.isEmpty()){
            closeOnError();
            return;
        }

        movie.setTrailerKey(trailersAsList);

        Bundle bundle = new Bundle();
        bundle.putParcelable("Movie", movie);
        bundle.putInt("MoviePosition", position);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(UPDATED_OBJECT, intent);

        getSupportLoaderManager().destroyLoader(TRAILER_LOADER);
    }

    private void closeOnError(){
        finish();
        Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
