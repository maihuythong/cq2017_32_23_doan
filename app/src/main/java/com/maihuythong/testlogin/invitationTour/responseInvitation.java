package com.maihuythong.testlogin.invitationTour;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class responseInvitation {
    @SerializedName("tourId")
    @Expose
    private long tourId;

    public long getTourId() {
        return tourId;
    }

    public void setTourId(long tourId) {
        this.tourId = tourId;
    }

    public long getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(long isAccepted) {
        this.isAccepted = isAccepted;
    }

    @SerializedName("isAccepted")
    @Expose
    private long isAccepted;
}
