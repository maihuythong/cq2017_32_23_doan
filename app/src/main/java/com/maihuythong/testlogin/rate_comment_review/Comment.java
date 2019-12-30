package com.maihuythong.testlogin.rate_comment_review;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    private long userId;
    private String name;
    private String comment;
    private String avatar;
    private long createOn;

    protected Comment(Parcel in) {
        userId = in.readLong();
        name = in.readString();
        comment = in.readString();
        avatar = in.readString();
        createOn = in.readLong();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeString(name);
        dest.writeString(comment);
        dest.writeString(avatar);
        dest.writeLong(createOn);
    }
}
