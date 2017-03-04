package com.android.fernass.mymovies.DataModel;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ferna on 27.02.2017.
 *
 * The Movie class contains all the movie data obtained from the internet
 * query including the path to the resources after download (image, poster, trailer)
 */


public class Movie implements Parcelable{
    boolean adult;
    String backdrop_path;
    int budget;
    ArrayList<Genre> genres;
    String title;
    String description;
    Uri poster_uri;
    String overview;
    String release_date;
    double vote_average;
    int id;

    public Movie(String _title, String _overview, Uri _poster_uri,
                 String _release_date, int _id, double _vote_average){
        this.title = _title;
        this.overview = _overview;
        this.poster_uri = _poster_uri;
        this.release_date = _release_date;
        this.id = _id;
        this.vote_average = _vote_average;
    }

    private Movie(Parcel in){
        title = in.readString();
        poster_uri = Uri.parse(in.readString());
        overview = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
        id = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public Uri getPoster_uri() {
        return poster_uri;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getId() {
        return id;
    }

    class Genre{
        int id;
        String name;
        public Genre(int _id, String _name){
            id = _id;
            name = _name;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag){
        out.writeString(title);
        out.writeString(poster_uri.toString());
        out.writeString(overview);
        out.writeString(release_date);
        out.writeDouble(vote_average);
        out.writeInt(id);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        public Movie createFromParcel(Parcel in){
            return new Movie(in);
        }
        public Movie[] newArray(int size){
            return new Movie[size];
        }
    };
}
