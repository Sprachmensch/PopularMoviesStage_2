package com.example.sprachmensch.popularmoviesstage1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sprachmensch.popularmoviesstage1.adapters.FavMovieDatabase;
import com.example.sprachmensch.popularmoviesstage1.adapters.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private MovieAdapter movieAdapter;

    private List<Movie> movies;
    private int sortedByPopularity = 1;
    private String url;
    private static final String MOVIE_DB_API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    private FavMovieDatabase db;
    private static final String GRIDPOSITION = "Adapter";
    private String SORTEDBYKEY = "savedSorting";
    private int scrollToYPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        gridView = findViewById(R.id.gridview);

        db = FavMovieDatabase.getInstance(getApplicationContext());

        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            sortedByPopularity = savedInstanceState.getInt(SORTEDBYKEY);
            scrollToYPos = savedInstanceState.getInt(GRIDPOSITION);
        }

        GetCorrectJSONFile();
        movies = new ArrayList<>();

        makeJSONRequest();


    }

    private void makeJSONRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray movieJson = response.getJSONArray("results");
                            movies.clear();
                            for (int i = 0; i < movieJson.length(); i++) {
                                JSONObject movie = movieJson.getJSONObject(i);
                                String tempMovieName = movie.getString("title");
                                int movieID = movie.getInt("id");
                                String tempMovieURL =
                                        "http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path");

                                movies.add(new Movie(tempMovieName, tempMovieURL, movieID));
                            }
                            movieAdapter = new MovieAdapter(MainActivity.this, movies);
                            gridView.setAdapter(movieAdapter);
                            gridView.smoothScrollToPosition(scrollToYPos);

                        } catch (JSONException e) {
                            Log.e("MOVIE", "BAD BAD BAD JSONException: " + e.toString());

                            Snackbar.make(findViewById(android.R.id.content), "Error loading the Movie List :(", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MOVIE", "VolleyError: " + error.toString());

                        Snackbar.make(findViewById(android.R.id.content), "No Internet connection :(", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void GetCorrectJSONFile() {
        if (sortedByPopularity == 2)
            url = "http://api.themoviedb.org/3/movie/popular?api_key=" + MOVIE_DB_API_KEY;
        else if (sortedByPopularity == 1)
            url = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + MOVIE_DB_API_KEY;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Sort by Ratings
        if (id == R.id.action_settings1) {
            if (sortedByPopularity == 1)
                Snackbar.make(this.findViewById(android.R.id.content), "Already sorted by Rating!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            else {
                sortedByPopularity = 1;
                switchSorting();
            }
        }
        //  Sort by Popularity
        else if (id == R.id.action_settings2) {
            if (sortedByPopularity == 2)
                Snackbar.make(this.findViewById(android.R.id.content), "Already sorted by Popularity!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            else {
                sortedByPopularity = 2;
                switchSorting();
            }
        }
        //  Show Favorites
        else if (id == R.id.action_settings3) {
            if (sortedByPopularity == 3)
                Snackbar.make(this.findViewById(android.R.id.content), "The Favorites are already shown!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            else {
                sortedByPopularity = 3;
                switchSorting();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchSorting() {

        if (sortedByPopularity == 2)
            Snackbar.make(this.findViewById(android.R.id.content), "Sorted by Popularity", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        else if (sortedByPopularity == 1)
            Snackbar.make(this.findViewById(android.R.id.content), "Sorted by Rating", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        else if (sortedByPopularity == 3)
            Snackbar.make(this.findViewById(android.R.id.content), "Show the Favorites", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        if (sortedByPopularity <= 2) {
            GetCorrectJSONFile();
            makeJSONRequest();
        } else {

            /*
               gets his own method someday...
             */

            final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.getFavMovies().observe(this, new Observer<List<FavMovies>>() {
                @Override
                public void onChanged(@Nullable List<FavMovies> favMovies) {

                    Log.d("LiveData", "db updated!");
                    movies.clear();

                    for (FavMovies fav : favMovies) {
                        movies.add(new Movie(
                                fav.getMovieTitle(),
                                "http://image.tmdb.org/t/p/w185/" + fav.getPosterUrl(),
                                Integer.parseInt(fav.getMovieId())));
                    }

                    movieAdapter = new MovieAdapter(MainActivity.this, movies);
                    gridView.setAdapter(movieAdapter);
                }
            });

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        state.putInt(SORTEDBYKEY, sortedByPopularity);
        state.putInt(GRIDPOSITION,
                gridView.getFirstVisiblePosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


}
