package com.android.fernass.mymovies.GUI;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.fernass.mymovies.DataModel.Movie;
import com.android.fernass.mymovies.NetworkUtility.MovieDbUtils;
import com.android.fernass.mymovies.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements movieAdapter.GridItemClickListener{
    ArrayList<Movie> moviesPopList;
    ArrayList<Movie> moviesRatedList;
    ArrayList<Movie> currentMovies;

    final int NUM_COL_SMALL = 4;
    String movieDbKey;
    String movieLanguage;
    private RecyclerView movieRecyclerView;
    private movieAdapter myAdapter;
    public static final String PARAM_POPULAR = "MOST_POPULAR";
    public static final String PARAM_TOP_RATED = "TOP_RATED";
    String currentMovieList;
    Activity currentActivity;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        currentActivity = this;

        movieRecyclerView = (RecyclerView) findViewById(R.id.rv_movie);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        movieDbKey = preferences.getString(SettingsActivity.TMDB_KEY, "");
        movieLanguage = preferences.getString(SettingsActivity.LANGUAGE, "");


        if(savedInstanceState != null && savedInstanceState.containsKey("moviesRatedList")
                && savedInstanceState.containsKey("moviesPopList")){
            moviesPopList = savedInstanceState.getParcelableArrayList("moviesPopList");
            moviesRatedList = savedInstanceState.getParcelableArrayList("moviesRatedList");
            currentMovieList = savedInstanceState.getString("currentMovieList");
            if(currentMovieList == PARAM_POPULAR)
                currentMovies = moviesPopList;
            else if(currentMovieList == PARAM_TOP_RATED)
                currentMovies = moviesRatedList;

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COL_SMALL);
            movieRecyclerView.setLayoutManager(gridLayoutManager);
            movieRecyclerView.setHasFixedSize(true);
            myAdapter = new movieAdapter(currentMovies, (movieAdapter.GridItemClickListener) currentActivity);
            movieRecyclerView.setAdapter(myAdapter);
        }
        else{
            context = getApplicationContext();
            boolean is_online = isOnline();
            boolean key_set = movieDbKey != "";

            if(is_online && key_set) {
                new FetchMoviesTask(this).execute(movieDbKey);
            }
            else if (!key_set) {
                Toast toast = Toast.makeText(context, R.string.no_key, Toast.LENGTH_LONG);
                toast.show();
            }
            else{
                Toast toast = Toast.makeText(context, R.string.no_connection, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putParcelableArrayList("moviesPopList", moviesPopList);
        bundle.putParcelableArrayList("moviesRatedList", moviesRatedList);
        bundle.putString("currentMovieList", currentMovieList);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onGridItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailMovie.class);
        if(currentMovieList == PARAM_POPULAR)
            intent.putExtra("movie", moviesPopList.get( clickedItemIndex));
        else if(currentMovieList == PARAM_TOP_RATED)
            intent.putExtra("movie", moviesRatedList.get( clickedItemIndex));
        startActivity(intent);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>>{
        Context myContext;
        FetchMoviesTask(Context context){
            myContext = context;
        }


        protected ArrayList<Movie> doInBackground(String... keys){
            moviesPopList = MovieDbUtils.fetchMovies(keys[0], PARAM_POPULAR, movieLanguage);
            moviesRatedList = MovieDbUtils.fetchMovies(keys[0], PARAM_TOP_RATED, movieLanguage);
// Default value: most popular movie list will be shown first
            currentMovieList = PARAM_POPULAR;
            return moviesPopList;
        }

        protected void onPostExecute(ArrayList<Movie> movies){
            if(movies != null){
                GridLayoutManager gridLayoutManager = new GridLayoutManager(myContext, NUM_COL_SMALL);
                movieRecyclerView.setLayoutManager(gridLayoutManager);
                movieRecyclerView.setHasFixedSize(true);
//            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(myContext, R.dimen.item_offset);
//            movieRecyclerView.addItemDecoration(itemDecoration);
                myAdapter = new movieAdapter(movies, (movieAdapter.GridItemClickListener) currentActivity);
                movieRecyclerView.setAdapter(myAdapter);
            }
            else{
                Toast toast = Toast.makeText(myContext, R.string.wrong_key, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.most_popular:
                currentMovieList = PARAM_POPULAR;
                if(moviesPopList != null)
                    myAdapter.changeData(moviesPopList);
                else{
                    Toast toast = Toast.makeText(this, R.string.wrong_key, Toast.LENGTH_LONG);
                    toast.show();
                }
                return true;
            case (R.id.highest_rated):
                currentMovieList = PARAM_TOP_RATED;
                if(moviesRatedList != null)
                    myAdapter.changeData(moviesRatedList);
                else{
                    Toast toast = Toast.makeText(this, R.string.wrong_key, Toast.LENGTH_LONG);
                    toast.show();
                }
                return true;
            case (R.id.settings):
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }

    private boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
