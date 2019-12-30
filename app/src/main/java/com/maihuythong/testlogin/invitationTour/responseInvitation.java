package com.maihuythong.testlogin.invitationTour;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class responseInvitation {
    @SerializedName("tourId")
    @Expose
    private long tourId;


    @SerializedName("isAccepted")
    @Expose
    private Boolean isAccepted;

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public long getTourId() {
        return tourId;
    }

    public void setTourId(long tourId) {
        this.tourId = tourId;
    }


}
