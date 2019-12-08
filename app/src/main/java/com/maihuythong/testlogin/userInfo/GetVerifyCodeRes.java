package com.maihuythong.testlogin.userInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVerifyCodeRes {
    @SerializedName("Message")
    @Expose
    private String message;

    public String getMessage(){return message;}
    public void  setMessage(String value){this.message=value;}

}
