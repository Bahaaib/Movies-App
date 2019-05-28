package ets.android_team.bahaa.moviesapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {MovieModel.class},
        version = 2,
        exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {
    public abstract DaoAccess DaoAccess();
}
