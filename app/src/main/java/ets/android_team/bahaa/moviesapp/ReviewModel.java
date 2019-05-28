package ets.android_team.bahaa.moviesapp;

import com.google.gson.annotations.SerializedName;

public class ReviewModel {

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String reviewContent;

    public ReviewModel(String author, String reviewContent) {
        this.author = author;
        this.reviewContent = reviewContent;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewBody) {
        this.reviewContent = reviewBody;
    }
}
