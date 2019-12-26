package com.maihuythong.testlogin.showTourInfomation;

public class TourOverview {
    private String hostName;
    private String date;
    private String adult;
    private String child;
    private String price;
    private String name;

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
