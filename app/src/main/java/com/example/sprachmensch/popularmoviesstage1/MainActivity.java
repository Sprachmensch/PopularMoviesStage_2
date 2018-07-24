package com.example.sprachmensch.popularmoviesstage1;

import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.sprachmensch.popularmoviesstage1.BuildConfig.*;



public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private MovieAdapter movieAdapter;

    private List<Movie> movies;
    private boolean sortedByPopularity;
    private String url;
    private static final String MOVIE_DB_API_KEY=BuildConfig.MOVIE_DB_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        gridView = findViewById(R.id.gridview);

        setSupportActionBar(toolbar);

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
        if (sortedByPopularity)
            url = "http://api.themoviedb.org/3/movie/popular?api_key="+MOVIE_DB_API_KEY;
        else
            url = "http://api.themoviedb.org/3/movie/top_rated?api_key="+MOVIE_DB_API_KEY;
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
            if (!sortedByPopularity)
                Snackbar.make(this.findViewById(android.R.id.content), "Already sorted by Rating!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            else
                switchSorting();
        }
        //  Sort by Popularity
        else if (id == R.id.action_settings2) {
            if (sortedByPopularity)
                Snackbar.make(this.findViewById(android.R.id.content), "Already sorted by Popularity!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            else
                switchSorting();
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchSorting() {
        sortedByPopularity = !sortedByPopularity;

        if (sortedByPopularity)
            Snackbar.make(this.findViewById(android.R.id.content), "Sort by Popularity", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        else
            Snackbar.make(this.findViewById(android.R.id.content), "Sort by Rating", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        GetCorrectJSONFile();
        makeJSONRequest();
    }
}
