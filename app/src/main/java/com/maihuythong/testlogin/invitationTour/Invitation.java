package com.maihuythong.testlogin.invitationTour;

public class Invitation {
    private long id;
    private String hostName;
    private String tourName;
    private long timeInvite;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public long getTimeInvite() {
        return timeInvite;
    }

    public void setTimeInvite(long timeInvite) {
        this.timeInvite = timeInvite;
    }
}
