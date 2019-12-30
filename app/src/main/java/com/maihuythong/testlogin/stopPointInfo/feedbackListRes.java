package com.maihuythong.testlogin.stopPointInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class feedbackListRes {

    @SerializedName("total")
    @Expose
    private long total;

    @SerializedName("feedbackList")
    @Expose
    private Feedback[] feedbacks;

    public Feedback[] getFeedbacks() { return feedbacks;}
    public long getTotal(){return total;}
}
