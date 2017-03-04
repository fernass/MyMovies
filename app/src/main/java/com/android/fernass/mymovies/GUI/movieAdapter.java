package com.android.fernass.mymovies.GUI;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fernass.mymovies.DataModel.Movie;
import com.android.fernass.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import android.view.View.OnClickListener;

/**
 * Created by ferna on 26.02.2017.
 */

public class movieAdapter extends RecyclerView.Adapter<movieAdapter.movieViewHolder>{

    ArrayList<Movie> movieData;
    int numMovies;
    GridItemClickListener mGridItemClickListener;

    String TAG = movieAdapter.class.getSimpleName();

    public movieAdapter(ArrayList<Movie> data, GridItemClickListener clickListener){
        movieData = data;
        numMovies = data.size();
        mGridItemClickListener = clickListener;
    }

    public interface GridItemClickListener{
        void onGridItemClick(int clickedItemIndex);
    }

    @Override
    public movieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_holder, viewGroup, false);
        movieViewHolder holder = new movieViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(movieViewHolder holder, int position){
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount(){
        return numMovies;
    }


    class movieViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener{
        ImageView movieImage;
        TextView movieName;

        public movieViewHolder(View view){
            super(view);

            movieImage = (ImageView) view.findViewById(R.id.iv_movie_image);
            movieName = (TextView) view.findViewById(R.id.tv_movie_name);

            view.setOnClickListener(this);
        }

        void bind(int index){
            movieName.setText(movieData.get(index).getTitle());
            Context context = movieName.getContext();
            Picasso.with(context).load(movieData.get(index).getPoster_uri()).
                    into(movieImage);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mGridItemClickListener.onGridItemClick(pos);
        }
    }

    public void changeData(ArrayList<Movie> data){
        movieData = data;
        notifyDataSetChanged();
    }
}

