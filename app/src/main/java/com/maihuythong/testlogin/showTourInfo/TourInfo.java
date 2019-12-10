package com.maihuythong.testlogin.showTourInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.maihuythong.testlogin.rate_comment_review.Comment;

public class TourInfo {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("hostId")
    @Expose
    private  long hostId;

    @SerializedName("status")
    @Expose
    private long status;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("minCost")
    @Expose
    private String minCost;

    @SerializedName("maxCost")
    @Expose
    private String maxCost;

    @SerializedName("startDate")
    @Expose
    private long startDate;

    @SerializedName("endDate")
    @Expose
    private long endDate;

    @SerializedName("adults")
    @Expose
    private long adults;

    @SerializedName("childs")
    @Expose
    private long childs;

    @SerializedName("isPrivate")
    @Expose
    private boolean isPrivate;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("stopPoints")
    @Expose
    private StopPoint[] stopPoints;

    @SerializedName("comments")
    @Expose
    private Comment[] comments;

    @SerializedName("members")
    @Expose
    private Member[] members;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHostId() {
        return hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(String maxCost) {
        this.maxCost = maxCost;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getAdults() {
        return adults;
    }

    public void setAdults(long adults) {
        this.adults = adults;
    }

    public long getChilds() {
        return childs;
    }

    public void setChilds(long childs) {
        this.childs = childs;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public StopPoint[] getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(StopPoint[] stopPoints) {
        this.stopPoints = stopPoints;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public Member[] getMembers() {
        return members;
    }

    public void setMembers(Member[] members) {
        this.members = members;
    }
}
