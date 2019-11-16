package com.maihuythong.testlogin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class CreateTourRequest {
    @SerializedName("name")
    @Expose
    private String tourName;

    @SerializedName("startDate")
    @Expose
    private Number startDate;

    @SerializedName("endDate")
    @Expose
    private Number endDate;

    @SerializedName("sourceLat")
    @Expose
    private Number sourceLat;

    @SerializedName("sourceLong")
    @Expose
    private Number sourceLong;

    @SerializedName("desLat")
    @Expose
    private Number desLat;

    @SerializedName("desLong")
    @Expose
    private Number desLong;

    @SerializedName("isPrivate")
    @Expose
    private Boolean isPrivate;

    @SerializedName("adults")
    @Expose
    private Number adults;

    @SerializedName("childs")
    @Expose
    private Number childs;

    @SerializedName("minCost")
    @Expose
    private Number minCost;

    @SerializedName("maxCost")
    @Expose
    private Number maxCost;

    @SerializedName("avatar")
    @Expose
    private String avatar;



    public String getTourName(){return tourName;}
    public void  setTourName(String name){tourName=name;}

    public Number getStartDate(){return startDate;}
    public void setStartDate(Number date){startDate=date;}

    public Number getEndDate(){return endDate;}
    public void setEndDate(Number date){endDate=date;}

    public Number getSourceLat(){return sourceLat;}
    public void setSourceLat(Number lat){sourceLat=lat;}

    public Number getSourceLong(){return sourceLong;}
    public void setSourceLong(Number srclong){sourceLong=srclong;}

    public Number getDesLat(){return desLat;}
    public void setDesLat(Number lat){desLat=lat;}

    public Number getDesLong(){return desLong;}
    public void setDesLong(Number srcLong){desLong=srcLong;}

    public Number getAdults(){return adults;}
    public void setAdults(Number mount){adults=mount;}

    public Number getChilds(){return childs;}
    public void setChilds(Number mount){childs=mount;}

    public Number getMinCost(){return minCost;}
    public void setMinCost(Number mount){minCost=mount;}

    public Number getMaxCost(){return maxCost;}
    public void setMaxCost(Number mount){maxCost = mount;}


}
