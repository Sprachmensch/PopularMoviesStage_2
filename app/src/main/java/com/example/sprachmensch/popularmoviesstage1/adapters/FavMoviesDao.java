package com.example.sprachmensch.popularmoviesstage1.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.sprachmensch.popularmoviesstage1.FavMovies;

import java.util.List;

@Dao
public interface FavMoviesDao {

    @Query("SELECT * FROM FavMovies")
    LiveData<List<FavMovies>> getAll();

    @Insert
    void insertFavMovie(FavMovies favMovies);

    @Query("DELETE FROM FavMovies WHERE movieId=:movieId")
    void deleteFavMovie(String movieId);

    @Query("SELECT COUNT(movieId) FROM FavMovies WHERE movieId = :movieId")
    int checkIfFavMovie(String movieId);

}
