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
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.francescosalamone.popularmoviesstage2.databinding.ActivityDetailBinding;
import com.francescosalamone.popularmoviesstage2.model.Movie;
import com.francescosalamone.popularmoviesstage2.model.Review;
import com.francescosalamone.popularmoviesstage2.utility.JsonUtility;
import com.francescosalamone.popularmoviesstage2.utility.NetworkUtility;
import com.francescosalamone.popularmoviesstage2.utility.ReviewAdapter;
import com.francescosalamone.popularmoviesstage2.utility.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> ,
ReviewAdapter.ItemClickListener, TrailerAdapter.ItemClickListener{

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_WIDTH_URL = "w342";
    private static final int TRAILER_LOADER = 2016;
    private static final int REVIEWS_LOADER = 1752;
    public static final int UPDATED_OBJECT = 188;
    private static final int DEFAULT_POSITION_VALUE = -1;

    private String movieDbApiKey;
    private int requestCode = 0;
    private int idMovie = -1;
    private int position;
    private int numberOfLoaderFinished = 0;

    ActivityDetailBinding mBinding;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private Movie movie = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        LinearLayoutManager layoutManagerTrailers = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvTrailers.setLayoutManager(layoutManagerTrailers);

        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvReviews.setLayoutManager(layoutManagerReviews);

        mReviewAdapter = new ReviewAdapter(this);
        mBinding.rvReviews.setAdapter(mReviewAdapter);

        mTrailerAdapter = new TrailerAdapter(this);
        mBinding.rvTrailers.setAdapter(mTrailerAdapter);

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

        getSupportActionBar().setTitle(movie.getOriginalTitle());

        //Check if already exist the trailers, if no new http request is needed
        if(movie.getTrailerKey().isEmpty()){
            idMovie = movie.getIdMovie();
            requestCode = 2;
            updateTrailers(requestCode);
        } else {
            mTrailerAdapter.setTrailers(movie.getTrailerKey());
        }

        //Check if already exist the reviews object, if no new http request is needed
        if(movie.getReviews().isEmpty()){
            idMovie = movie.getIdMovie();
            requestCode = 3;
            updateReviews(requestCode);
        } else {
            mReviewAdapter.setReviews(movie.getReviews());
            mReviewAdapter.setTitle(movie.getOriginalTitle());
        }

        populateUI(movie);

    }

    private void updateReviews(int request){
        boolean isConnected = NetworkUtility.checkInternetConnection(this);
        Bundle bundle = new Bundle();
        bundle.putInt("requestCode", request);

        if(isConnected){
            try{
                if(getSupportLoaderManager().getLoader(REVIEWS_LOADER).isStarted())
                    getSupportLoaderManager().restartLoader(REVIEWS_LOADER, bundle, this);
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                getSupportLoaderManager().initLoader(REVIEWS_LOADER, bundle, this);
            }
        }
    }

    private void updateTrailers(int request){
        boolean isConnected = NetworkUtility.checkInternetConnection(this);
        Bundle bundle = new Bundle();
        bundle.putInt("requestCode", request);

        if(isConnected) {
            try {
                if (getSupportLoaderManager().getLoader(TRAILER_LOADER).isStarted())
                    getSupportLoaderManager().restartLoader(TRAILER_LOADER, bundle, this);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getSupportLoaderManager().initLoader(TRAILER_LOADER, bundle, this);
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
        mBinding.ratingRb.setRating((float) (movie.getUsersRating()/2));
        mBinding.releaseDateTv.setText(String.valueOf(movie.getReleaseDate()));

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                if(movieDbApiKey == null || TextUtils.isEmpty(movieDbApiKey))
                    return null;
                try{
                    int request = args.getInt("requestCode");
                    return NetworkUtility.getContentFromHttp(NetworkUtility.buildUrl(movieDbApiKey, request, idMovie) );
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

        if(loader.getId() == TRAILER_LOADER){
            numberOfLoaderFinished ++;
            List<String> trailersAsList = new ArrayList<>();
            try {
                trailersAsList = JsonUtility.parseTrailerJson(data);

            } catch (JSONException e){
                e.printStackTrace();
            }

            if(trailersAsList == null || trailersAsList.isEmpty()){
                return;
            }

            mTrailerAdapter.setTrailers(trailersAsList);

            movie.setTrailerKey(trailersAsList);

            getSupportLoaderManager().destroyLoader(TRAILER_LOADER);

        } else if(loader.getId() == REVIEWS_LOADER){
            numberOfLoaderFinished++;
            List<Review> reviewAsList = new ArrayList<>();
            try{
                reviewAsList = JsonUtility.parseReviewJson(data);
            } catch (JSONException e){
                e.printStackTrace();
            }

            if(reviewAsList == null || reviewAsList.isEmpty()){
                return;
            }

            mReviewAdapter.setReviews(reviewAsList);
            mReviewAdapter.setTitle(movie.getOriginalTitle());
            movie.setReviews(reviewAsList);
            getSupportLoaderManager().destroyLoader(REVIEWS_LOADER);
        }

        //If all loaders finished
        if(numberOfLoaderFinished == 2){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Movie", movie);
            bundle.putInt("MoviePosition", position);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(UPDATED_OBJECT, intent);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public void onTrailerClick(int clickItemPosition) {

    }

    @Override
    public void onReviewClick(int clickItemPosition) {

    }
}
