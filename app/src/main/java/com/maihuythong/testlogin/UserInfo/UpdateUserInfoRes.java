package com.maihuythong.testlogin.UserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUserInfoRes {
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage(){return message;}
    public void  setMessage(String value){this.message=value;}

}
