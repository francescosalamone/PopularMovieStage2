package com.francescosalamone.popularmoviesstage2;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.francescosalamone.popularmoviesstage2.databinding.ActivityReviewBinding;

public class ReviewActivity extends AppCompatActivity {

    ActivityReviewBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //Setup NavigationBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_review);

        Intent intent = getIntent();
        if(intent == null){
            finish();
        }

        mBinding.tvFullAuthor.setText(intent.getStringExtra("author"));
        mBinding.tvFullComment.setText(intent.getStringExtra("comment"));
        getSupportActionBar().setTitle(intent.getStringExtra("title"));
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
}
