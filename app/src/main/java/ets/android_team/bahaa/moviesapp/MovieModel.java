package ets.android_team.bahaa.moviesapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bahaa on 3/23/2018.
 */
@Entity
public class MovieModel {

    @SerializedName("original_title")
    private String movieTitle;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String moviePoster;

    @SerializedName("vote_average")
    private Float voteAvg;

    @SerializedName("overview")
    private String moviePlot;

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private Integer movieId;



    public MovieModel() {

        //Required Empty Constructor
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public Float getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(Float voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getMoviePlot() {
        return moviePlot;
    }

    public void setMoviePlot(String moviePlot) {
        this.moviePlot = moviePlot;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
}
