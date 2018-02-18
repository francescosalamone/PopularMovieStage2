package com.francescosalamone.popularmovies;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.francescosalamone.popularmovies.utility.NetworkUtility;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private String MovieDbApiKey;
    static final int MOVIE_LOADER = 1502;
    private int sortCode =0;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void updateMovieList(){
        //I check, before the HTTP request, if we have an internet connection available
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this).forceLoad();
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
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        //TODO Qui bisogna fare il parsing tramite json della stringa ritornata dalla richiesta HTTP
        Toast.makeText(this, "Http request finished", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
