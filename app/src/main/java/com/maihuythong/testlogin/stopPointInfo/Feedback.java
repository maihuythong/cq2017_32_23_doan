package com.maihuythong.testlogin.stopPointInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("userId")
    @Expose
    private long userId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("feedback")
    @Expose
    private String feedback;

    @SerializedName("point")
    @Expose
    private int point;

    @SerializedName("createOn")
    @Expose
    private String createOn;

    public long getId() { return id;}
    public void setId(long value){id=value;}

    public long getUserId() {return userId;}
    public void setUserId(long value){userId=value;}

    public String getName(){return name;}

    public String getAvatar(){return avatar;}

    public int getPoint(){return point;}

    public String getCreateOn(){return createOn;}

    public String getFeedback(){return feedback;}
}
