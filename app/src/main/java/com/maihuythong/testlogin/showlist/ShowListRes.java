package com.maihuythong.testlogin.showlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowListRes {
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
