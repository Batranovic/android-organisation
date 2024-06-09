package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projekatmobilneaplikacije.model.enumerations.Owner;

import java.util.Date;

public class EventOrganization implements Parcelable {
    private String type;
    private String name;
    private String description;
    private int participants;
    private String location;
    private Date date;
    private Boolean isPrivate;
    private String budgetId;

    public EventOrganization(String type, String name, String description, int participants,
                             String location, Date date, Boolean isPrivate, String budgetId) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.participants = participants;
        this.location = location;
        this.date = date;
        this.isPrivate = isPrivate;
        this.budgetId = budgetId;
    }

    public EventOrganization() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    protected EventOrganization(Parcel in) {
        type = in.readString();
        name = in.readString();
        description = in.readString();
        participants = in.readInt();
        location = in.readString();
        long startTimeMillis = in.readLong();
        date = startTimeMillis != -1 ? new Date(startTimeMillis) : null;
        isPrivate = in.readBoolean();
        budgetId = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(participants);
        dest.writeString(location);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeBoolean(isPrivate);
        dest.writeString(budgetId);
    }

    public static final Creator<EventOrganization> CREATOR = new Creator<EventOrganization>() {
        @Override
        public EventOrganization createFromParcel(Parcel in) {
            return new EventOrganization(in);
        }

        @Override
        public EventOrganization[] newArray(int size) {
            return new EventOrganization[size];
        }
    };
}
