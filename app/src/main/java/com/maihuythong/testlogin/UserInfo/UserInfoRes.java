package com.maihuythong.testlogin.UserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfoRes {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("createOn")
    @Expose
    private String createOn;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("gender")
    @Expose
    private int gender;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("typeLogin")
    @Expose
    private int typeLogin;


    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getFullName() {return fullName;}
    public void setFullName(String value){this.fullName=value;}

    public String getEmail() {return email;}
    public void setEmail(String value){this.email=value;}

    public String getPhone() {return phone;}
    public void setPhone(String value){this.phone=value;}

    public String getAddress(){return  address;}
    public void setAddress(String value){this.address=value;}

    public String getDob() {return dob;}
    public void setDob(String value){this.dob=value;}

    public String getAvatar() {return avatar;}
    public void setAvatar(String value){this.avatar=value;}

    public long getGender() { return gender; }
    public void setGender(int value) { this.gender = value; }

    public String getCreateOn(){return createOn;}
    public void setCreateOn(String value){this.createOn=value;}

}
