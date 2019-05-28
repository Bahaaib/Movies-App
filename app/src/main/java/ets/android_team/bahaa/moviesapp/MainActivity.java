package ets.android_team.bahaa.moviesapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ArrayList<MovieModel> movieList;
    public static List<MovieModel> favoriteMovies;
    public RecyclerView recyclerView;
    MovieAdapter adapter;
    GridLayoutManager gridLayoutManager;
    public static String API_URL = "https://api.themoviedb.org/3/movie/";
    public String ORDER_PREF = "popular?";
    public RequestQueue requestQueue;
    public ConnectivityManager manager;

    ///////Database portion////////////
    private static final String DATABASE_NAME = "movies_db";
    public static MoviesDatabase moviesDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init Admob
        MobileAds.initialize(this, "ca-app-pub-6702076183097498~7970103834");


        movieList = new ArrayList<>();
        favoriteMovies = new ArrayList();
        requestQueue = Volley.newRequestQueue(getBaseContext());

        ///////Database portion////////////
        moviesDatabase = Room.databaseBuilder(this, MoviesDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();


        recyclerView = findViewById(R.id.gameRecyclerView);

        adapter = new MovieAdapter(this, movieList);
        recyclerView.setAdapter(adapter);

        if (isOnline()) {
            loadMovies();
        } else {
            Toast.makeText(this, "Connection Not Available!", Toast.LENGTH_LONG).show();
        }


        //Showing the RecyclerView Elements using the GridView Scheme, 2 Cards in each row, propagating vertically,
        //Wrapping all passed cards with no limit
        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gridLayoutManager);
        new FetchTask().execute();
        adapter.notifyDataSetChanged();



    }

    public void loadMovies() {
        movieList.clear();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                API_URL + ORDER_PREF + "api_key=" + getString(R.string.api_key),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            // Initialize Gson and start new transaction
                            Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                                @Override
                                public boolean shouldSkipField(FieldAttributes f) {
                                    return false;
                                }

                                @Override
                                public boolean shouldSkipClass(Class<?> clazz) {
                                    return false;
                                }
                            }).create();

                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MovieModel model = gson.fromJson(jsonArray.get(i).toString(), MovieModel.class);
                                //Log.i("titles",   model.getMovieTitle());
                                movieList.add(model);
                            }
                            //Notify the RecyclerView with the new data..
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()


        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        requestQueue.add(request);


    }

    public void LoadFavorite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favoriteMovies = moviesDatabase.DaoAccess().fetchMovieList();
                movieList.clear();

                movieList.addAll(favoriteMovies);

            }
        }).start();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pref_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.popular_item:
                ORDER_PREF = "popular?";
                if (isOnline()) {
                    loadMovies();
                    new FetchTask().execute();
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(this, "Connection Not Available!", Toast.LENGTH_LONG).show();
                }
                // Log.i("menu", "Popular Pressed!");
                break;
            case R.id.top_rated_item:
                ORDER_PREF = "top_rated?";
                if (isOnline()) {
                    loadMovies();
                   new FetchTask().execute();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Connection Not Available!", Toast.LENGTH_LONG).show();
                }
                // Log.i("menu", "Top Rated Pressed!");
                break;

            case R.id.favorite_item:

                LoadFavorite();

        }
        return true;
    }

    public boolean isOnline() {
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null;

    }

    class FetchTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... params) {

            favoriteMovies = moviesDatabase.DaoAccess().fetchMovieList();

            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {
            for (int i=0 ; i<favoriteMovies.size(); i++){
                Log.i("Movie:", favoriteMovies.get(i).getMovieTitle());
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {


        }
    }
}
