package com.example.sprachmensch.popularmoviesstage1.adapters;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.sprachmensch.popularmoviesstage1.FavMovies;


@Database(entities = {FavMovies.class}, version = 2)
public abstract class FavMovieDatabase extends RoomDatabase {

    private static FavMovieDatabase instance;
    private static final Object LOCK = new Object();
    private static final String DB_NAME="favmovies.db";

    public abstract FavMoviesDao FavMovieDao();

    public static FavMovieDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        FavMovieDatabase.class, FavMovieDatabase.DB_NAME)
                        .build();
            }
        }

        return instance;
    }
}


