package com.maihuythong.testlogin.showTourInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopPoint {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private  String name;

    @SerializedName("lat")
    @Expose
    private long lat;

    @SerializedName("Long")
    @Expose
    private long Long;

    @SerializedName("arrivalAt")
    @Expose
    private long arrivalAt;

    @SerializedName("leaveAt")
    @Expose
    private long leaveAt;

    @SerializedName("minCost")
    @Expose
    private long minCost;

    @SerializedName("maxCost")
    @Expose
    private long maxCost;

    @SerializedName("serviceTypeId")
    @Expose
    private int serviceTypeId;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLong() {
        return Long;
    }

    public void setLong(long aLong) {
        Long = aLong;
    }

    public long getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(long arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public long getLeaveAt() {
        return leaveAt;
    }

    public void setLeaveAt(long leaveAt) {
        this.leaveAt = leaveAt;
    }

    public long getMinCost() {
        return minCost;
    }

    public void setMinCost(long minCost) {
        this.minCost = minCost;
    }

    public long getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(long maxCost) {
        this.maxCost = maxCost;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
