package com.maihuythong.testlogin.showAccountTours;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.maihuythong.testlogin.showlist.Tour;

public class ShowAccountToursReq {
    @SerializedName("total")
    @Expose
    private long total;

    @SerializedName("tours")
    @Expose
    private Tour[] tours;

    public long getTotal() { return total; }
    public void setTotal(long value) { this.total = value; }

    public Tour[] getTours() { return tours; }
    public void setTours(Tour[] value) { this.tours = value; }
}
