package ets.android_team.bahaa.moviesapp;

import com.google.gson.annotations.SerializedName;

public class TrailerModel {


    private Integer movieId;

    @SerializedName("key")
    private String movieKey;

    public TrailerModel() {
        //Required Empty Constructor
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getMovieKey() {
        return movieKey;
    }

    public void setMovieKey(String movieKey) {
        this.movieKey = movieKey;
    }
}
