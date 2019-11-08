package com.maihuythong.testlogin.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tpl on 12/13/16.
 */

public class LoginRequest {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
