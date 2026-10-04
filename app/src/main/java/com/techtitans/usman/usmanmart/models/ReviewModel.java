package com.techtitans.usman.usmanmart.models;
public class ReviewModel {

    private String reviewId;
    private String reviewerId;

    private String reviewerName;
    private String reviewerImageUrl;
    private String reviewText;
    private float rating;
    private long timestamp;

    public ReviewModel() {
        // Required empty constructor for Firestore deserialization
    }

    public ReviewModel(String reviewId, String reviewerName, String reviewerImageUrl,
                       String reviewText, float rating, long timestamp) {
        this.reviewId = reviewId;
        this.reviewerName = reviewerName;
        this.reviewerImageUrl = reviewerImageUrl;
        this.reviewText = reviewText;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public ReviewModel(String reviewId, String uid, String name, String reviewerImageUrl, String text, float rating, long timestamp) {
        this.reviewId=reviewId;
        this.reviewerId=uid;
        this.reviewText=text;
        this.reviewerImageUrl=reviewerImageUrl;
        this.timestamp=timestamp;
        this.rating=rating;
        this.reviewerName=name;

    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerImageUrl() {
        return reviewerImageUrl;
    }

    public void setReviewerImageUrl(String reviewerImageUrl) {
        this.reviewerImageUrl = reviewerImageUrl;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }
}
