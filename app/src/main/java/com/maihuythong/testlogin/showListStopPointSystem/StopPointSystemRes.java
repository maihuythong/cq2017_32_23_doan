package com.maihuythong.testlogin.showListStopPointSystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.maihuythong.testlogin.showTourInfo.StopPoint;

public class StopPointSystemRes {
    @SerializedName("total")
    @Expose
    private long total;

    @SerializedName("stopPoints")
    @Expose
    private StopPoint[] stopPoints;

    public StopPoint[] getStopPoints(){return stopPoints;}
    public long getTotal(){return total;}
}