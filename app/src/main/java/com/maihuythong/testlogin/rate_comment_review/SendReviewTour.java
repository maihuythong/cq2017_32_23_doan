package com.maihuythong.testlogin.rate_comment_review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendReviewTour {

    @SerializedName("tourId")
    @Expose
    private long tourId;

    @SerializedName("point")
    @Expose
    private int point;

    @SerializedName("review")
    @Expose
    private String review;

    public long getTourId() {
        return tourId;
    }

    public void setTourId(long tourId) {
        this.tourId = tourId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
