package com.example.sprachmensch.popularmoviesstage1;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class FavMovies {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String movieId;

    @ColumnInfo(name = "movie_title")
    private String movieTitle;

    @ColumnInfo(name = "poster_url")
    private String posterUrl;

    public FavMovies(String movieId, String movieTitle, String posterUrl) {
        this.movieId=movieId;
        this.movieTitle=movieTitle;
        this.posterUrl=posterUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }


}
