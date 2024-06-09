package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class CommentDeadline implements Parcelable {
    private String id;
    private String eventOrganizer;
    private Date date;
    private Date deadline;

    public CommentDeadline() {
    }

    public CommentDeadline(String id, String eventOrganizer, Date date, Date deadline) {
        this.id = id;
        this.eventOrganizer = eventOrganizer;
        this.date = date;
        this.deadline = deadline;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    protected CommentDeadline(Parcel in) {
        id = in.readString();
        eventOrganizer = in.readString();
        date = new Date(in.readLong());
        deadline = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(eventOrganizer);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeLong(deadline != null ? deadline.getTime() : -1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentDeadline> CREATOR = new Creator<CommentDeadline>() {
        @Override
        public CommentDeadline createFromParcel(Parcel in) {
            return new CommentDeadline(in);
        }

        @Override
        public CommentDeadline[] newArray(int size) {
            return new CommentDeadline[size];
        }
    };
}
