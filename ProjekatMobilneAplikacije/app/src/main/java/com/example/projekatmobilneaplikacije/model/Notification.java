package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Notification implements Parcelable {
    private String id;
    private String title;
    private String message;
    private boolean isRead;
    private Date date;
    private String username;

    public Notification() {
    }

    public Notification(String id, String title, String message, boolean isRead, Date date, String username) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.isRead = false;
        this.date = date;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeByte((byte) (isRead ? 1 : 0));
        dest.writeString(date.toString());
        dest.writeString(username);
    }

    // Constructor for creating from Parcel
    protected Notification(Parcel in) {
        id = in.readString();
        title = in.readString();
        message = in.readString();
        isRead = in.readByte() != 0;
        long dateMillis = in.readLong();
        date = dateMillis != -1 ? new Date(dateMillis) : null;  //date = new Date(in.readLong());
        username = in.readString();
        /*String dateString = in.readString();
        if (dateString != null) {
            date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } else {
            date = null;
        }*/
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
