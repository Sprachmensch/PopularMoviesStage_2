package com.example.sprachmensch.popularmoviesstage1;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sprachmensch.popularmoviesstage1.adapters.FavMovieDatabase;
import com.example.sprachmensch.popularmoviesstage1.adapters.ReviewAdapter;
import com.example.sprachmensch.popularmoviesstage1.adapters.TrailerAdapter;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.sprachmensch.popularmoviesstage1.BuildConfig.MOVIE_DB_API_KEY;

public class DetailActivity extends AppCompatActivity {
    private String Synopsis, Title, UserRating, ReleaseDate, MovieBackdrop, MoviePoster;
    private String MovieUrl;
    private ImageView imageView, imageViewBG;
    private TextView textView, textView3, textView4;
    private int position;
    private ImageView imageViewFavorite;
    private boolean isFavorite;
    private GridView GvTrailer, GvReview;
private FavMovieDatabase db;

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
        imageViewFavorite = findViewById(R.id.IvFav);

        GvTrailer = findViewById(R.id.GvTrailer);
        GvReview = findViewById(R.id.GvReviews);

        setSupportActionBar(toolbar);
        ViewCompat.setNestedScrollingEnabled(GvTrailer, true);
        ViewCompat.setNestedScrollingEnabled(GvReview, true);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        position = intent.getIntExtra("MovieNumber", -1);
        if (position == -1) {
            return;
        }

//        final FavMovieDatabase db = Room.databaseBuilder(getApplicationContext(),
//                FavMovieDatabase.class, "favmovies.db")
//                .allowMainThreadQueries()
//                .fallbackToDestructiveMigration()
//                .build();
        db = FavMovieDatabase.getInstance(getApplicationContext());


        new Thread(new Runnable() {
            @Override
            public void run() {
                int isFav=db.FavMovieDao().checkIfFavMovie(String.valueOf(position));
                if(isFav>0)
                {
                    isFavorite = !isFavorite;
                    imageViewFavorite.setImageResource(R.drawable.ic_grade_pink_24dp);
                }

            }
        }).start();



        //handle the onclick on fav
        imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isFavorite = !isFavorite;

                if (isFavorite) {
                    imageViewFavorite.setImageResource(R.drawable.ic_grade_pink_24dp);
                } else {
                    imageViewFavorite.setImageResource(R.drawable.ic_grade_black_24dp);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFavorite) {
                            db.FavMovieDao().insertFavMovie(new FavMovies(String.valueOf(position), Title, MoviePoster));
                        } else {
                            db.FavMovieDao().deleteFavMovie(String.valueOf(position));
                        }
                    }
                }).start();

            }
        });

        // get the json for the movie
        MovieUrl = "http://api.themoviedb.org/3/movie/" + String.valueOf(position) + "?api_key=" + MOVIE_DB_API_KEY;
        makeJSONRequest();

        makeJSonRequestTrailer();
        makeJSonRequestReviews();
    }

    private void makeJSonRequestReviews() {

        final String ReviewsUrl = "http://api.themoviedb.org/3/movie/" + String.valueOf(position) + "/reviews?api_key=" + MOVIE_DB_API_KEY;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ReviewsUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JsonParser jsonParser = new JsonParser();

                            Log.d("Json test", "response : " + response.toString());

                            JSONArray tempArray = response.getJSONArray("results");
                            Log.d("Json test", "tempArray : " + tempArray.toString());
                            Log.d("Json test", "tempArray.length : " + tempArray.length());

                            Log.d("Json test", "ReviewsUrl: " + ReviewsUrl);
                            Log.d("Json test", " .isJsonArray(:" + jsonParser.parse(String.valueOf(tempArray)).isJsonArray());
                            Log.d("Json test", " .isJsonNull(:" + jsonParser.parse(String.valueOf(tempArray)).isJsonNull());
                            Log.d("Json test", " .isJsonObject(:" + jsonParser.parse(String.valueOf(tempArray)).isJsonObject());
                            Log.d("Json test", " .isJsonPrimitive(:" + jsonParser.parse(String.valueOf(tempArray)).isJsonPrimitive());
                            JSONObject ReviewObject = tempArray.getJSONObject(0);
                            String review = ReviewObject.getString("content");
                            Log.d("Json test", "review: " + review);

                            List<String> reviews = new ArrayList<>();
                            for (int i = 0; i < tempArray.length(); i++) {

                                ReviewObject = tempArray.getJSONObject(i);
                                review = ReviewObject.getString("content");

                                reviews.add(review);
                                Log.d("Json test", "Review #" + i + " review: " + review);
                            }
                            ReviewAdapter reviewAdapter = new ReviewAdapter(DetailActivity.this, reviews);
                            GvReview.setAdapter(reviewAdapter);


                        } catch (JSONException e) {
                            Log.e("MOVIE", "JSONException: " + e.toString());

                            Snackbar.make(findViewById(android.R.id.content), "Error loading the Reviews :(", Snackbar.LENGTH_LONG)
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


    private void makeJSonRequestTrailer() {
        final String TrailerUrl = "http://api.themoviedb.org/3/movie/" + String.valueOf(position) + "/videos?api_key=" + MOVIE_DB_API_KEY;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, TrailerUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String Trailer = null;

                        try {
                            JsonParser jsonParser = new JsonParser();

                            Log.d("Json test", "response : " + response.toString());

                            JSONArray tempArray = response.getJSONArray("results");
                            Log.d("Json test", "tempArray : " + tempArray.toString());
                            Log.d("Json test", "tempArray.length : " + tempArray.length());

                            Log.d("Json test", "TrailerUrl: " + TrailerUrl);
                            Log.d("Json test", " .isJsonArray(:" + jsonParser.parse(String.valueOf(tempArray)).isJsonArray());
                            Log.d("Json test", " .isJsonNull(:" + jsonParser.parse(String.valueOf(tempArray)).isJsonNull());
                            Log.d("Json test", " .isJsonObject(:" + jsonParser.parse(String.valueOf(tempArray)).isJsonObject());
                            Log.d("Json test", " .isJsonPrimitive(:" + jsonParser.parse(String.valueOf(tempArray)).isJsonPrimitive());
                            JSONObject TrailerObject = tempArray.getJSONObject(0);
                            Trailer = TrailerObject.getString("key");
                            Log.d("Json test", "Trailer key: " + Trailer);

                            String YTThumbImage = "https://img.youtube.com/vi/" + Trailer + "/mqdefault.jpg";
                            List<String> trailers = new ArrayList<>();
                            for (int i = 0; i < tempArray.length(); i++) {

                                TrailerObject = tempArray.getJSONObject(i);
                                Trailer = TrailerObject.getString("key");

                                YTThumbImage = "https://img.youtube.com/vi/" + Trailer + "/mqdefault.jpg";
                                trailers.add(Trailer);
                                Log.d("Json test", "Trailer # " + i + " YTThumbImage: " + YTThumbImage + " key: " + Trailer);
                            }
                            TrailerAdapter trailerAdapter = new TrailerAdapter(DetailActivity.this, trailers);
                            GvTrailer.setAdapter(trailerAdapter);

                        } catch (
                                JSONException e)

                        {
                            Log.e("MOVIE", "Trailer Videos JSONException: " + e.toString());

                            Snackbar.make(findViewById(android.R.id.content), "Error loading the Trailer Videos :(", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    }

                }, new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MOVIE", "VolleyError: " + error.toString());
                        Snackbar.make(findViewById(android.R.id.content), "No Internet connection :(", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

        queue.add(jsonObjectRequest);
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

                            Picasso.get()
                                    .load(imageURLBG)
                                    .placeholder(R.drawable.ic_sync_black_24dp)
                                    .error(R.drawable.ic_sync_problem_24dp)
                                    .resize(300, 500)
                                    .into(imageViewBG);

                            String imageURL = "http://image.tmdb.org/t/p/w500/" + MoviePoster;
                            Picasso.get()
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
