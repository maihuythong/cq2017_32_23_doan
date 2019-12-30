package com.maihuythong.testlogin.ShowNotiOnRoadList;

public class NotiOnRoad {

    private double lat;
    private double mLong;
    private String note;
    private long notificationType;
    private long speed;
    private long createdByTourId;

    public NotiOnRoad(double lat, double mLong, String note, long notificationType,
                      long speed, long createdByTourId) {
        this.lat = lat;
        this.mLong = mLong;
        this.note = note;
        this.notificationType = notificationType;
        this.speed = speed;
        this.createdByTourId = createdByTourId;
    }

    public double getLat() {
        return lat;
    }

    public double getmLong() {
        return mLong;
    }

    public long getCreatedByTourId() {
        return createdByTourId;
    }

    public long getNotificationType() {
        return notificationType;
    }

    public long getSpeed() {
        return speed;
    }

    public String getNote() {
        return note;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setCreatedByTourId(long createdByTourId) {
        this.createdByTourId = createdByTourId;
    }

    public void setmLong(double mLong) {
        this.mLong = mLong;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setNotificationType(long notificationType) {
        this.notificationType = notificationType;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }
}
