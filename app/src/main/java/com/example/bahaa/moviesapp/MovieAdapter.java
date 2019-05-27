package com.example.bahaa.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.bahaa.moviesapp.MainActivity.favoriteMovies;
import static com.example.bahaa.moviesapp.MainActivity.moviesDatabase;

/**
 * Created by Bahaa on 3/23/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter {

    //Here we recieve from the calling Activity :
    // The cards container List & The Parent Activity context
    private Context context;
    private ArrayList<MovieModel> adapterModel;
    private InterstitialAd interstitialAd;
    private String testAd = "ca-app-pub-3940256099942544/1033173712";

    {
        adapterModel = new ArrayList<>();
    }

    public MovieAdapter(Context context, ArrayList<MovieModel> adapterModel) {
        this.context = context;
        this.adapterModel = adapterModel;
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId("ca-app-pub-6702076183097498/7507069401");
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    //Here We tell the RecyclerView what to show at each element of it..it'd be a cardView!
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.movie_card, parent, false);
        return new MovieViewHolder(view);
    }

    //Here We tell the RecyclerView what to show at each CardView..
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MovieViewHolder) holder).BindView(position);

    }

    @Override
    public int getItemCount() {
        return adapterModel.size();
    }

    //Here we bind all the children views of each cardView with their corresponding
    // actions to show & interact with them
    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView moviePoster;
        private CardView movieCard;
        private String BASE_URL = "https://image.tmdb.org/t/p/";
        private String IMAGE_SIZE = "w500/";
        private LikeButton favoriteButton;
        private boolean isFavorite;
        private ArrayList<Integer> favoriteMoviesIdList = new ArrayList<>();


        public MovieViewHolder(View itemView) {
            super(itemView);

            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieCard = itemView.findViewById(R.id.movie_card_view);
            favoriteButton = (LikeButton) itemView.findViewById(R.id.fav_heart);


        }


        //Here where all the glory being made..!
        public void BindView(final int position) {

            Picasso.with(context)
                    .load(BASE_URL + IMAGE_SIZE + adapterModel.get(position).getMoviePoster())
                    .fit()
                    .into(moviePoster);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent movieIntent = new Intent(context, MovieDetailsActivity.class);
                    movieIntent.putExtra("movie_poster", BASE_URL + IMAGE_SIZE + adapterModel.get(position).getMoviePoster());
                    movieIntent.putExtra("movie_title", adapterModel.get(position).getMovieTitle());
                    movieIntent.putExtra("release_date", adapterModel.get(position).getReleaseDate());
                    movieIntent.putExtra("vote_avg", String.valueOf(adapterModel.get(position).getVoteAvg()));
                    movieIntent.putExtra("movie_plot", adapterModel.get(position).getMoviePlot());
                    movieIntent.putExtra("movie_id", adapterModel.get(position).getMovieId());
                    context.startActivity(movieIntent);

                }
            });

            for (int i = 0; i < favoriteMovies.size(); i++) {
                favoriteMoviesIdList.add(favoriteMovies.get(i).getMovieId());
                Log.i("Status", "Loaded lists..");
            }

            for (int j = 0; j < favoriteMoviesIdList.size(); j++) {
                if (adapterModel.get(position).getMovieId().equals(favoriteMoviesIdList.get(j))) {
                    isFavorite = true;
                    Log.i("Status", "Found Favorite!");
                    break;

                } else {
                    Log.i("Status", adapterModel.get(position).getMovieTitle() + " is NOT Favorite!");
                    isFavorite = false;

                }
            }
            favoriteButton.setLiked(isFavorite);


            favoriteButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            moviesDatabase.DaoAccess().insertMovieToDB(adapterModel.get(position));
                            if (!isFavorite) {
                                favoriteMovies.add(adapterModel.get(position));
                            }
                        }
                    }).start();
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            moviesDatabase.DaoAccess().deleteMovieFromDB(adapterModel.get(position));
                            favoriteMovies.remove(adapterModel.get(position));
                        }
                    }).start();
                }
            });

            movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    } else {
                        Log.i("Statuss", "Failed to load");
                    }
                }
            });


        }


    }
}

