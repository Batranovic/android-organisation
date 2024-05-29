package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EventOrganizer implements Parcelable {
    private String id;
    private UserDetails details;

    public EventOrganizer(String id, UserDetails details) {
        this.id = id;
        this.details = details;
    }

    public EventOrganizer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDetails getDetails() {
        return details;
    }

    public void setDetails(UserDetails details) {
        this.details = details;
    }

    protected EventOrganizer(Parcel in) {
        id = in.readString();
        details = in.readParcelable(UserDetails.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(details, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventOrganizer> CREATOR = new Creator<EventOrganizer>() {
        @Override
        public EventOrganizer createFromParcel(Parcel in) {
            return new EventOrganizer(in);
        }

        @Override
        public EventOrganizer[] newArray(int size) {
            return new EventOrganizer[size];
        }
    };
}
