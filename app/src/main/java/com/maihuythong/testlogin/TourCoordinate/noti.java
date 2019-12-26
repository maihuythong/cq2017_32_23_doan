package com.maihuythong.testlogin.TourCoordinate;

public class noti {
    private String userId;
    private String name;
    private String avatar;
    private String notification;

    public noti(String userId, String notification) {
        this.userId = userId;
        this.notification = notification;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
