package com.maihuythong.testlogin.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

//    @SerializedName("title")
//    @Expose
//    private String title;
//
//    @SerializedName("body")
//    @Expose
//    private String body;
//
//    @SerializedName("userId")
//    @Expose
//    private Integer userId;
//
//    @SerializedName("id")
//    @Expose
//    private Integer id;
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getBody() {
//        return body;
//    }
//
//    public void setBody(String body) {
//        this.body = body;
//    }
//
//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    @Override
//    public String toString() {
//        return "Post{" +
//                "title='" + title + '\'' +
//                ", body='" + body + '\'' +
//                ", userId=" + userId +
//                ", id=" + id +
//                '}';
//    }


    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("gender")
    @Expose
    private long gender;

    @SerializedName("emailVerified")
    @Expose
    private boolean emailVerified;

    @SerializedName("phoneVerified")
    @Expose
    private boolean phoneVerified;

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getUsername() { return username; }
    public void setUsername(String value) { this.username = value; }

    public String getFullName() { return fullName; }
    public void setFullName(String value) { this.fullName = value; }

    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }

    public String getPhone() { return phone; }
    public void setPhone(String value) { this.phone = value; }

    public String getAddress() { return address; }
    public void setAddress(String value) { this.address = value; }

    public String getDob() { return dob; }
    public void setDob(String value) { this.dob = value; }

    public long getGender() { return gender; }
    public void setGender(long value) { this.gender = value; }

    public boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean value) { this.emailVerified = value; }

    public boolean getPhoneVerified() { return phoneVerified; }
    public void setPhoneVerified(boolean value) { this.phoneVerified = value; }

        @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", fullname=" + fullName +
                ", email=" + email +
                ", phone=" + phone +
                ", address=" + address +
                ", dob=" + dob +
                ", gender=" + gender +
                ", emailVerified=" + emailVerified +
                ", phoneVerified=" + phoneVerified +
                '}';
    }
}



