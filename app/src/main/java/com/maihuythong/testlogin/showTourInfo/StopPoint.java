package com.maihuythong.testlogin.showTourInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopPoint {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("serviceId")
    @Expose
    private int serviceId;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("provinceId")
    @Expose
    private int provinceId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("lat")
    @Expose
    private Number lat;

    @SerializedName("long")
    @Expose
    private Number Long;

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

    @SerializedName("index")
    @Expose
    private  int index;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServiceId(){return serviceId;}
    public void setServiceId(int value){serviceId =value;}

    public int getProvinceId(){return provinceId;}
    public void setProvinceId(int value){provinceId = value;}

    public String getAddress(){return address;}
    public void setAddress(String value){address=value;}

    public int getIndex(){return index;}
    public void setIndex(int value){index=value;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Number getLat() {
        return lat;
    }
    public void setLat(long lat) {
        this.lat = lat;
    }

    public Number getLong() {
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
