package com.example.bahaa.moviesapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertMovieToDB(MovieModel movie);

    @Query("SELECT * FROM MovieModel WHERE movieId = :movieId")
    MovieModel fetchMovieById(int movieId);

    @Query("SELECT * FROM MOVIEMODEL")
    List<MovieModel> fetchMovieList();

    @Update
    void updateMovieOnDB(MovieModel movie);

    @Delete
    void deleteMovieFromDB(MovieModel movie);
}
