package com.example.sprachmensch.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.sprachmensch.popularmoviesstage1.BuildConfig.MOVIE_DB_API_KEY;

public class DetailActivity extends AppCompatActivity {
    private String Synopsis, Title, UserRating, ReleaseDate, MovieBackdrop, MoviePoster;
    private String MovieUrl;
    private ImageView imageView, imageViewBG;
    private TextView textView, textView3, textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        textView = findViewById(R.id.description_tv);
        textView3 = findViewById(R.id.rating_tv);
        textView4 = findViewById(R.id.releasedate_tv);
        imageView = findViewById(R.id.movieposter_iv);
        imageViewBG = findViewById(R.id.imageViewBackground);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        int position = intent.getIntExtra("MovieNumber", -1);
        if (position == -1) {
            return;
        }

        // get the json for the movie
        MovieUrl = "http://api.themoviedb.org/3/movie/" + String.valueOf(position) + "?api_key="+MOVIE_DB_API_KEY;
        makeJSONRequest();
    }

    private void makeJSONRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, MovieUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Synopsis = null;
                        UserRating = null;
                        ReleaseDate = null;
                        MoviePoster = null;
                        Title = null;

                        try {
                            Title = response.getString("original_title");
                            Synopsis = response.getString("overview");
                            UserRating = response.getString("vote_average");
                            ReleaseDate = response.getString("release_date");
                            MoviePoster = response.getString("poster_path");
                            MovieBackdrop = response.getString("backdrop_path");

                            // manipulate date string
                            ReleaseDate = createDateString();

                            // pop the views
                            setTitle(Title);
                            textView.setText(Synopsis);
                            textView3.setText(UserRating);
                            textView4.setText(ReleaseDate);

                            // picasso do your thing
                            String imageURLBG = "http://image.tmdb.org/t/p/w500/" + MovieBackdrop;

                            Picasso.with(DetailActivity.this)
                                    .load(imageURLBG)
                                    .placeholder(R.drawable.ic_sync_black_24dp)
                                    .error(R.drawable.ic_sync_problem_24dp)
                                    .resize(300, 500)
                                    .into(imageViewBG);

                            String imageURL = "http://image.tmdb.org/t/p/w500/" + MoviePoster;
                            Picasso.with(DetailActivity.this)
                                    .load(imageURL)
                                    .placeholder(R.drawable.ic_sync_black_24dp)
                                    .error(R.drawable.ic_sync_problem_24dp)
                                    .resize(300, 500)
                                    .into(imageView);

                        } catch (JSONException e) {
                            Log.e("MOVIE", "JSONException: " + e.toString());

                            Snackbar.make(findViewById(android.R.id.content), "Error loading the Movie List :(", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    }

                    // turn numbers into Letters, decided against using SimpleDateFormat
                    private String createDateString() {
                        String TempYear = ReleaseDate.substring(0, 4);
                        String TempMonth = ReleaseDate.substring(5, 7);

                        if (TempMonth.equals("01"))
                            TempMonth = "Jan";
                        else if (TempMonth.equals("02"))
                            TempMonth = "Feb";
                        else if (TempMonth.equals("03"))
                            TempMonth = "Mar";
                        else if (TempMonth.equals("04"))
                            TempMonth = "Apr";
                        else if (TempMonth.equals("05"))
                            TempMonth = "May";
                        else if (TempMonth.equals("06"))
                            TempMonth = "Jun";
                        else if (TempMonth.equals("07"))
                            TempMonth = "Jul";
                        else if (TempMonth.equals("08"))
                            TempMonth = "Aug";
                        else if (TempMonth.equals("09"))
                            TempMonth = "Sep";
                        else if (TempMonth.equals("10"))
                            TempMonth = "Oct";
                        else if (TempMonth.equals("11"))
                            TempMonth = "Nov";
                        else
                            TempMonth = "Dez";

                        return TempMonth + " " + TempYear;
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
}
