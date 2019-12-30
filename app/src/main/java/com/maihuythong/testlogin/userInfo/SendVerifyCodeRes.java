package com.maihuythong.testlogin.userInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendVerifyCodeRes {
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage(){return message;}
    public void  setMessage(String value){this.message=value;}

    public boolean getSuccess(){return  success;}
}
