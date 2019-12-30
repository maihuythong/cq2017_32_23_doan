package com.maihuythong.testlogin.rate_comment_review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendCommentTour {
    @SerializedName("tourId")
    @Expose
    private long tourId;

    @SerializedName("userId")
    @Expose
    private int userId;

    @SerializedName("comment")
    @Expose
    private String comment;
}
