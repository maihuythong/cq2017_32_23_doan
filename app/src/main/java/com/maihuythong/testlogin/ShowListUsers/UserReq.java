package com.maihuythong.testlogin.ShowListUsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReq {
    @SerializedName("total")
    @Expose
    private long total;

    @SerializedName("users")
    @Expose
    private User[] users;

    public long getTotal() { return total; }
    public void setTotal(long value) { this.total = value; }

    public User[] getUsers() { return users; }
    public void setTours(User[] value) { this.users = value; }
}
