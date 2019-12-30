package com.maihuythong.testlogin.invitationTour;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowInvitationReq {
    @SerializedName("total")
    @Expose
    private long total;

    @SerializedName("tours")
    @Expose
    private Invitation[] tours;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Invitation[] getInvitations() {
        return tours;
    }

    public void setInvitations(Invitation[] invitations) {
        this.tours = invitations;
    }
}
