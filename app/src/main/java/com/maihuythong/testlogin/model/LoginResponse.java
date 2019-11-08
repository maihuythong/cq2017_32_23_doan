package com.maihuythong.testlogin.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tpl on 12/13/16.
 */

public class LoginResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private TokenInfo data;
    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TokenInfo getData() {
        return data;
    }

    public void setData(TokenInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
