package com.maihuythong.testlogin.rate_comment_review;

public class Comment {
    private long userId;
    private String name;
    private String comment;
    private String avatar;
    private long createOn;

    public long getCreateOn() {
        return createOn;
    }

    public void setCreateOn(long createOn) {
        this.createOn = createOn;
    }

    public Comment(long userId, String name, String comment, String avatar, long createOn) {
        this.userId = userId;
        this.name = name;
        this.comment = comment;
        this.avatar = avatar;
        this.createOn = createOn;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
