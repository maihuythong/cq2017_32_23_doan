package com.maihuythong.testlogin.forgotPassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendRequestOTPRes {
    @SerializedName("userId")
    @Expose
    private long userId;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("expiredOn")
    @Expose
    private String expiredOn;

    @SerializedName("Message")
    @Expose
    private String message;

    public long getUserId(){return userId;}
    public void setUserId(long value){this.userId=value;}

    public String getType(){return type;}
    public void setType(String value){this.type= value;}

    public String getExpiredOn(){return expiredOn;}
    public void setExpiredOn(String value){this.expiredOn=value;}

    public String getMessage(){return message;}
    public void  setMessage(String value){this.message=value;}

}
