package ets.android_team.bahaa.moviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bahaa on 3/23/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter {

    //Here we recieve from the calling Fragment :
    // The cards container List & The Parent Activity context
    private Context context;
    private ArrayList<ReviewModel> adapterModel;

    {
        adapterModel = new ArrayList<>();
    }

    public ReviewsAdapter(Context context, ArrayList<ReviewModel> adapterModel) {
        this.context = context;
        this.adapterModel = adapterModel;
    }

    //Here We tell the RecyclerView what to show at each element of it..it'd be a cardView!
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.review_card, parent, false);
        return new ReviewViewHolder(view);
    }

    //Here We tell the RecyclerView what to show at each CardView..
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ReviewViewHolder) holder).BindView(position);

    }

    @Override
    public int getItemCount() {
        return adapterModel.size();
    }

    //Here we bind all the children views of each cardView with their corresponding
    // actions to show & interact with them
    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView author, reviewContent;


        public ReviewViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            reviewContent = itemView.findViewById(R.id.review_content);

        }


        //Here where all the glory being made..!
        public void BindView(final int position) {

            author.setText(adapterModel.get(position).getAuthor());
            reviewContent.setText(adapterModel.get(position).getReviewContent());


        }


    }
}

