package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;



import java.util.List;

public class CreateEvent implements Parcelable {
    private Long id;
    private String eventType;
    private String name;
    private String description;
    private int participants;
    private String location;
    private String date;
    private boolean isPrivate;
    private List<Subcategory> subcategories;

    public CreateEvent(Long id, String eventType, String name, String description, int participants, String location, String date, boolean isPrivate,List<Subcategory> subcategories) {
        this.id = id;
        this.eventType = eventType;
        this.name = name;
        this.description = description;
        this.participants = participants;
        this.location = location;
        this.date = date;
        this.isPrivate = isPrivate;
        this.subcategories = subcategories;
    }

    public CreateEvent() {}

    protected CreateEvent(Parcel in) {
        id = in.readLong();
        eventType = in.readString();
        name = in.readString();
        description = in.readString();
        participants = in.readInt();
        location = in.readString();
        date = in.readString();
        isPrivate = in.readByte() != 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
    public List<Subcategory> getSubcategories() {
        return subcategories;
    }


    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }


    @Override
    public String toString() {
        return "Event{" +
                "eventType='" + eventType + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", participants=" + participants +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", isPrivate=" + isPrivate +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(eventType);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(participants);
        dest.writeString(location);
        dest.writeString(date);
        dest.writeByte((byte) (isPrivate ? 1 : 0));
    }

    public static final Creator<CreateEvent> CREATOR = new Creator<CreateEvent>() {
        @Override
        public CreateEvent createFromParcel(Parcel in) {
            return new CreateEvent(in);
        }

        @Override
        public CreateEvent[] newArray(int size) {
            return new CreateEvent[size];
        }


    };
}
