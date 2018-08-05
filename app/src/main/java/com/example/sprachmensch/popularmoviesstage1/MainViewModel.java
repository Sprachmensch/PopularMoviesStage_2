package com.example.sprachmensch.popularmoviesstage1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.sprachmensch.popularmoviesstage1.adapters.FavMovieDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<FavMovies>> favMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d("MainViewModel", "its alive - getting movies from the db!");
        FavMovieDatabase db = FavMovieDatabase.getInstance(this.getApplication());
        favMovies = db.FavMovieDao().getAll();
    }

    public LiveData<List<FavMovies>> getFavMovies() {
        return favMovies;
    }
}
