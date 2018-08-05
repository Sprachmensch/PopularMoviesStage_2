package com.example.sprachmensch.popularmoviesstage1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sprachmensch.popularmoviesstage1.DetailActivity;
import com.example.sprachmensch.popularmoviesstage1.Movie;
import com.example.sprachmensch.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends BaseAdapter {
    private final Context context;
    private final List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movie = movies.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.movie_poster, null);
        }

        final TextView movieTitletv=convertView.findViewById(R.id.movie_title);
        movieTitletv.setText(movie.getmovieName());

        final ImageView imageView = convertView.findViewById(R.id.poster_iv);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("MovieNumber", movie.getMovieNumber());
                context.startActivity(intent);
            }
        });

        Picasso.get()
                .load(movie.getImageUrl())
                .resize(300, 500)
                .placeholder(R.drawable.ic_sync_black_24dp)
                .error(R.drawable.ic_sync_problem_24dp)
                .into(imageView);

        return convertView;
    }

}
