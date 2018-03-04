package com.francescosalamone.popularmoviesstage2;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.francescosalamone.popularmoviesstage2.model.Movie;
import com.francescosalamone.popularmoviesstage2.utility.JsonUtility;
import com.francescosalamone.popularmoviesstage2.utility.NetworkUtility;
import com.francescosalamone.popularmoviesstage2.utility.PosterAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
        , PosterAdapter.ItemClickListener {

    private String MovieDbApiKey;
    private static final int MOVIE_LOADER = 1502;
    private int sortCode =0;

    private PosterAdapter mPosterAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.rv_posters);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPosterAdapter = new PosterAdapter( this);
        mRecyclerView.setAdapter(mPosterAdapter);

        configureBottomNav();


        MovieDbApiKey = getString(R.string.apiV3);

        //swipeRefreshLayout refresh the page, it's good solution if I haven't some connection and I want to try again
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMovieList();
            }
        });

        updateMovieList();
    }

    private void configureBottomNav(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_top_rated:
                        sortCode = 1;
                        break;
                    case R.id.action_popular:
                        sortCode = 0;
                    default:
                        break;
                }
                updateMovieList();
                return true;
            }
        });
    }

    private void updateMovieList(){
        //I check, before the HTTP request, if we have an internet connection available
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            try {
                if (getSupportLoaderManager().getLoader(MOVIE_LOADER).isStarted())
                    getSupportLoaderManager().restartLoader(MOVIE_LOADER, null, this);
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);
            }
            swipeRefreshLayout.setRefreshing(false);
        }
        else {
            Toast.makeText(this, "No internet connection, Swipe to refresh", Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            public String loadInBackground() {
                if(MovieDbApiKey == null || TextUtils.isEmpty(MovieDbApiKey))
                    return null;
                try{
                    return NetworkUtility.getContentFromHttp(NetworkUtility.buildUrl(MovieDbApiKey, sortCode) );
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
    public void onLoadFinished(Loader<String> loader, String httpResult) {

        List<Movie> moviesAsList = new ArrayList<>();
        try {
            moviesAsList = JsonUtility.parseMovieJson(httpResult);
        } catch (JSONException e){
            e.printStackTrace();
        }

        if(moviesAsList == null || moviesAsList.isEmpty()){
            closeOnError();
            return;
        }

        mPosterAdapter.setPoster(moviesAsList);
    }

    private void closeOnError(){
        finish();
        Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onItemClick(int clickItemPosition) {

    }
}
