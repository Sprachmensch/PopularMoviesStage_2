package com.example.sprachmensch.popularmoviesstage1;

public class Movie {
    private String movieName;
    private String imageUrl;
    private int movieNumber;

    public Movie(String movieName, String imageUrl, int movieNumber) {
        this.movieName = movieName;
        this.imageUrl = imageUrl;
        this.movieNumber = movieNumber;
    }

    public String getmovieName() {
        return movieName;
    }

    public int getMovieNumber(){
        return movieNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
