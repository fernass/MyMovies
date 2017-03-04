package com.android.fernass.mymovies.GUI;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fernass.mymovies.DataModel.Movie;
import com.android.fernass.mymovies.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailMovie extends AppCompatActivity {

    TextView mTitle;
    ImageView mPoster;
    TextView mOverview;
    TextView mRating;
    TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Intent intent = getIntent();

        Movie movie = (Movie) intent.getParcelableExtra("movie");

        mTitle = (TextView) findViewById(R.id.tv_detail_movie_name);
        mPoster = (ImageView) findViewById(R.id.iv_detail_movie_image);
        mOverview = (TextView) findViewById(R.id.tv_detail_overview);
        mRating = (TextView) findViewById(R.id.tv_detail_rating);
        mReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);

        mTitle.setText(movie.getTitle());
        mOverview.setText(movie.getOverview());
        mRating.setText(new Double(movie.getVote_average()).toString());
        mReleaseDate.setText(movie.getRelease_date());

        Uri uri = movie.getPoster_uri();
        Picasso.with(this).load(uri).into(mPoster);
    }
}
