package com.maihuythong.testlogin.showTourInfomation;

public class TourOverview {
    private String hostName;
    private String date;
    private String adult;
    private String child;
    private String price;
    private String name;
    private long id;
    private long status;
    private String minCost;
    private String maxCost;

    public String getMaxCost() {
        return maxCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getMinCost() {
        return minCost;
    }

    public void setMaxCost(String maxCost) {
        this.maxCost = maxCost;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAdult() {
        return adult;
    }

    public String getChild() {
        return child;
    }

    public String getDate() {
        return date;
    }

    public String getHostName() {
        return hostName;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
