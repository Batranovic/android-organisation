package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Notification implements Parcelable {
    private String id;
    private String title;
    private String message;
    private boolean isRead;
    private LocalDate date;

    public Notification() {
    }

    public Notification(String id, String title, String message, boolean isRead, LocalDate date) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
    }

    // Constructor for creating from Parcel
    protected Notification(Parcel in) {
        id = in.readString();
        title = in.readString();
        message = in.readString();
        isRead = in.readByte() != 0;
        date = LocalDate.parse(in.readString());
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
