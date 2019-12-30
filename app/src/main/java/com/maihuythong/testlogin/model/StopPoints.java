package com.maihuythong.testlogin.model;

import java.util.ArrayList;

public class StopPoints {
    private String tourId;
    private ArrayList<StopPointInfo> stopPoints;

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getTourId() {
        return tourId;
    }

    public void setStopPoints(ArrayList<StopPointInfo> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public ArrayList<StopPointInfo> getStopPoints() {
        return stopPoints;
    }
}

