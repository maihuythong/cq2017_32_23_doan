package com.maihuythong.testlogin.TourCoordinate;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetNotificationList {
    @SerializedName("notiList")
    ArrayList<noti> notiList;

    public ArrayList<noti> getNotiList() {
        return notiList;
    }

    public void setNotiList(ArrayList<noti> notiList) {
        this.notiList = notiList;
    }
}
