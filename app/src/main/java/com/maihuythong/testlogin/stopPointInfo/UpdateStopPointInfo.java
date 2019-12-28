package com.maihuythong.testlogin.stopPointInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateStopPointInfo {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("serviceId")
    @Expose
    private long serviceId;

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



    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }


    public int getProvinceId(){return provinceId;}
    public void setProvinceId(int value){provinceId = value;}

    public String getAddress(){return address;}
    public void setAddress(String value){address=value;}

    public long getServiceId(){return serviceId;}
    public void setServiceId(long value){serviceId =value;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Number getLat() {
        return lat;
    }
    public void setLat(Number lat) {
        this.lat = lat;
    }

    public Number getLong() {
        return Long;
    }
    public void setLong(Number aLong) {
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

    public int getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }


}
