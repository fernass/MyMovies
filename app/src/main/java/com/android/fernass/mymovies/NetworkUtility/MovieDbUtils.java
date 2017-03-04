package com.android.fernass.mymovies.NetworkUtility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.fernass.mymovies.DataModel.Movie;
import com.android.fernass.mymovies.GUI.MainActivity;
import com.android.fernass.mymovies.GUI.SettingsActivity;
import com.android.fernass.mymovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ferna on 27.02.2017.
 */

public class MovieDbUtils {
    private static final String BASE_API_URL = "https://api.themoviedb.org/3/movie";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String PARAM_SIZE = "w185";
    private static final String POPULAR_ENDPOINT = "popular";
    private static final String TOP_Rated_ENDPOINT = "top_rated";
    private static final String QUERY_API_KEY = "api_key";
    private static final String PARAM_LANG = "language";
    private static final String PARAM_PAGES = "page";


    public static ArrayList<Movie> fetchMovies(String key, String param, String language){
        String response = null;
        JSONObject  jsonMovie = null;
        JSONArray json_popularMovies = null;
        ArrayList<Movie> popularMovies = null;
        String title ="";
        String overview = "";
        String poster_path = "";
        Uri poster_uri = null;
        String release_date = "";
        int id = 0;
        double vote_average = 0.0;

        URL url = null;
        if(param == MainActivity.PARAM_POPULAR)
            url = buildURL(key, language, POPULAR_ENDPOINT);
        else if (param == MainActivity.PARAM_TOP_RATED)
            url = buildURL(key, language, TOP_Rated_ENDPOINT);

        if(url != null){
            try {
                response = getResponseFromHttpConnection(url);
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }

            try {
                JSONObject object = new JSONObject(response);
                json_popularMovies = object.getJSONArray("results");
            }catch (JSONException e){
                e.printStackTrace();
            }

            popularMovies = new ArrayList<>(json_popularMovies.length());

            for(int i=0; i<json_popularMovies.length(); i++){
                try {
                    jsonMovie = json_popularMovies.getJSONObject(i);
                    title = jsonMovie.getString("title");
                    overview = jsonMovie.getString("overview");
                    poster_path = jsonMovie.getString("poster_path");
                    release_date = jsonMovie.getString("release_date");
                    vote_average = jsonMovie.getDouble("vote_average");
                    id = jsonMovie.getInt("id");

                }catch (JSONException e){
                    e.printStackTrace();
                }
                poster_uri = Uri.parse(BASE_IMAGE_URL + PARAM_SIZE + poster_path);
                Movie movie = new Movie(title, overview, poster_uri, release_date,
                        id, vote_average);
                popularMovies.add(movie);
            }
        }
        return popularMovies;
    }

    public static URL buildURL(String key, String language, String endpoint){

        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendPath(endpoint)
                .appendQueryParameter(QUERY_API_KEY, key)
                .appendQueryParameter(PARAM_LANG, language)
                .appendQueryParameter(PARAM_PAGES, "1")
                .build();
        URL url = null;

        try {
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromHttpConnection(URL url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream is = connection.getInputStream();
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");

            if(scanner.hasNext())
                return scanner.next();
            else
                return null;
        }finally {
            connection.disconnect();
        }
    }
}
