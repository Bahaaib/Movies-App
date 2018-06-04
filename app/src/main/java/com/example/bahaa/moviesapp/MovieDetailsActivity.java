package com.example.bahaa.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.bahaa.moviesapp.MainActivity.API_URL;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView moviePoster;
    private TextView movieTitle, releaseDate, voteAvg, moviePlot, movieTrailer;
    private Integer movieID;
    public RequestQueue requestQueue;
    public ConnectivityManager manager;
    public TrailerModel model;
    private RecyclerView reviewRV;
    private ReviewsAdapter reviewsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList reviewList;
    private ReviewModel reviewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        requestQueue = Volley.newRequestQueue(getBaseContext());


        moviePoster = (ImageView) findViewById(R.id.movie_poster_details);

        movieTitle = (TextView) findViewById(R.id.movie_title_details);
        releaseDate = (TextView) findViewById(R.id.release_date_details);
        voteAvg = (TextView) findViewById(R.id.vote_avg_details);
        moviePlot = (TextView) findViewById(R.id.movie_plot_details);
        movieTrailer = (TextView) findViewById(R.id.movie_trailer);


        final Intent intent = getIntent();

        movieID = intent.getIntExtra("movie_id", -1);

        Log.i("id", movieID.toString());


        movieTitle.setText(intent.getStringExtra("movie_title"));
        releaseDate.setText(intent.getStringExtra("release_date"));
        voteAvg.setText(intent.getStringExtra("vote_avg"));
        moviePlot.setText(intent.getStringExtra("movie_plot"));

        reviewList = new ArrayList();


        Picasso.with(MovieDetailsActivity.this)
                .load(intent.getStringExtra("movie_poster"))
                .fit()
                .into(moviePoster);

        if (isOnline()) {
            LoadTrailer();
            LoadReviews();
        } else {
            Toast.makeText(this, "Connection Not Available!", Toast.LENGTH_LONG).show();

        }

        movieTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent yIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + model.getMovieKey()));
                    startActivity(yIntent);
                } catch (Exception e) {
                    Toast.makeText(MovieDetailsActivity.this, "No Trailers Available!", Toast.LENGTH_LONG).show();

                }
            }
        });

        reviewRV = (RecyclerView) findViewById(R.id.reviews_rv);

        reviewsAdapter = new ReviewsAdapter(this, reviewList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewRV.setLayoutManager(linearLayoutManager);
        reviewRV.setAdapter(reviewsAdapter);


    }

    public void LoadTrailer() {

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                API_URL + movieID + "/videos?" + "api_key=" + getString(R.string.api_key),
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
                                model = gson.fromJson(jsonArray.get(i).toString(), TrailerModel.class);
                                model.setMovieId(movieID);
                                Log.i("video Key", model.getMovieKey());

                            }

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

    public void LoadReviews() {
        reviewList.clear();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                API_URL + movieID + "/reviews?" + "api_key=" + getString(R.string.api_key),
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

                                    reviewModel = gson.fromJson(jsonArray.get(i).toString(), ReviewModel.class);
                                    Log.i("review", reviewModel.getReviewContent());


                                    reviewList.add(reviewModel);

                                }

                            //Notify the RecyclerView with the new data..
                            reviewsAdapter.notifyDataSetChanged();

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


    public boolean isOnline() {
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager.getActiveNetworkInfo() != null) {
            return true;
        } else {
            return false;
        }

    }

}
