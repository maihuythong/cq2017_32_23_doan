package com.maihuythong.testlogin.ShowSPInformation;

import android.os.Parcel;
import android.os.Parcelable;

public class SPOverview implements Parcelable {
    private int id;
    private int serviceId;
    private String address;
    private int provinceId;
    private String name;
    private double Lat;
    private double Long;
    private long arrivalAt;
    private long leaveAt;
    private long minCost;
    private long maxCost;
    private int serviceTypeId;
    private String avatar;

    public SPOverview(){}

    protected SPOverview(Parcel in) {
        id = in.readInt();
        serviceId = in.readInt();
        address = in.readString();
        provinceId = in.readInt();
        name = in.readString();
        Lat = in.readDouble();
        Long = in.readDouble();
        arrivalAt = in.readLong();
        leaveAt = in.readLong();
        minCost = in.readLong();
        maxCost = in.readLong();
        serviceTypeId = in.readInt();
        avatar = in.readString();
    }

    public static final Creator<SPOverview> CREATOR = new Creator<SPOverview>() {
        @Override
        public SPOverview createFromParcel(Parcel in) {
            return new SPOverview(in);
        }

        @Override
        public SPOverview[] newArray(int size) {
            return new SPOverview[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public long getArrivalAt() {
        return arrivalAt;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getLeaveAt() {
        return leaveAt;
    }

    public void setArrivalAt(long arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public long getMaxCost() {
        return maxCost;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getMinCost() {
        return minCost;
    }

    public String getAddress() {
        return address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setLeaveAt(long leaveAt) {
        this.leaveAt = leaveAt;
    }

    public void setMaxCost(long maxCost) {
        this.maxCost = maxCost;
    }

    public void setMinCost(long minCost) {
        this.minCost = minCost;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(serviceId);
        dest.writeString(address);
        dest.writeInt(provinceId);
        dest.writeString(name);
        dest.writeDouble(Lat);
        dest.writeDouble(Long);
        dest.writeLong(arrivalAt);
        dest.writeLong(leaveAt);
        dest.writeLong(minCost);
        dest.writeLong(maxCost);
        dest.writeInt(serviceTypeId);
        dest.writeString(avatar);
    }
}
