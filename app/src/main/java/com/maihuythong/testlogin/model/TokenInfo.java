package com.maihuythong.testlogin.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tpl on 12/13/16.
 */

public class TokenInfo {
    @SerializedName("id")
    private long id;
    @SerializedName("username")
    private String userName;
    @SerializedName("token")
    private String token;
    @SerializedName("loginDate")
    private long loginDate;
    @SerializedName("expiredDate")
    private long expiredDate;
    @SerializedName("isExpired")
    private boolean isExpired;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(long loginDate) {
        this.loginDate = loginDate;
    }

    public long getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(long expiredDate) {
        this.expiredDate = expiredDate;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}
