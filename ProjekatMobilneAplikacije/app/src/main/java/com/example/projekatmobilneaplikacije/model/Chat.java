package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Chat implements Parcelable {
    private String sender;
    private String receiver;
    private String content;
    private Timestamp timestamp;
    private String status;

    public Chat() {}

    public Chat(String sender, String receiver, String content, Timestamp timestamp, String status) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
    }

    protected Chat(Parcel in) {
        sender = in.readString();
        receiver = in.readString();
        content = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        status = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(sender);
        parcel.writeString(receiver);
        parcel.writeString(content);
        parcel.writeParcelable(timestamp, i);
        parcel.writeString(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
// Getters and Setters
}
