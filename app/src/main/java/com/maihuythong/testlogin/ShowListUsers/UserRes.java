package com.maihuythong.testlogin.ShowListUsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRes {
    @SerializedName("rowPerPage")
    @Expose
    private int rowPerPage;

    @SerializedName("pageNum")
    @Expose
    private int pageNum;

    public long getRowPerPage() { return rowPerPage; }
    public void setRowPerPage(int value) { this.rowPerPage = value; }

    public long getPageNum() { return pageNum; }
    public void setPageNum(int value) { this.pageNum = value; }
}
