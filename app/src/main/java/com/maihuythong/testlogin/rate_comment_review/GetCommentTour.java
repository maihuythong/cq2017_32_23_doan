package com.maihuythong.testlogin.rate_comment_review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCommentTour {
    @SerializedName("commentList")
    @Expose
    private Comment[] commentList;

    public Comment[] getComments() {
        return commentList;
    }

    public void setComments(Comment[] comments) {
        this.commentList = comments;
    }
}
