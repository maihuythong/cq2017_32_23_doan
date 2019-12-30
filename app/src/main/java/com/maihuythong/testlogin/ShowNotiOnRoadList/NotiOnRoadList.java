package com.maihuythong.testlogin.ShowNotiOnRoadList;

import com.google.gson.annotations.SerializedName;
import com.maihuythong.testlogin.TourCoordinate.noti;

import java.util.ArrayList;

public class NotiOnRoadList {

    @SerializedName("notiList")
    ArrayList<NotiOnRoad> notiList;

    public ArrayList<NotiOnRoad> getNotiList() {
        return notiList;
    }

    public void setNotiList(ArrayList<NotiOnRoad> notiList) {
        this.notiList = notiList;
    }
}
