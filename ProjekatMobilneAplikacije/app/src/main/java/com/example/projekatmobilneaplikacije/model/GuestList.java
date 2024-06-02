package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class GuestList implements Parcelable {
    private String id;
    private String name;
    private String age;
    private boolean isInvited;
    private boolean hasAccepted;
    private List<String> specialRequests;

    public GuestList() {
        // Default constructor required for calls to DataSnapshot.getValue(Guest.class)
    }

    public GuestList( String id,String name, String age, boolean isInvited, boolean hasAccepted, List<String> specialRequests) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isInvited = isInvited;
        this.hasAccepted = hasAccepted;
        this.specialRequests = specialRequests;
    }

    protected GuestList(Parcel in) {
        id = in.readString();
        name = in.readString();
        age = in.readString();
        isInvited = in.readByte() != 0;
        hasAccepted = in.readByte() != 0;
        specialRequests = in.createStringArrayList();
    }

    public static final Creator<GuestList> CREATOR = new Creator<GuestList>() {
        @Override
        public GuestList createFromParcel(Parcel in) {
            return new GuestList(in);
        }

        @Override
        public GuestList[] newArray(int size) {
            return new GuestList[size];
        }
    };
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isInvited() {
        return isInvited;
    }

    public void setInvited(boolean invited) {
        isInvited = invited;
    }

    public boolean isHasAccepted() {
        return hasAccepted;
    }

    public void setHasAccepted(boolean hasAccepted) {
        this.hasAccepted = hasAccepted;
    }

    public List<String> getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(List<String> specialRequests) {
        this.specialRequests = specialRequests;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
       parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(age);
        parcel.writeByte((byte) (isInvited ? 1 : 0));
        parcel.writeByte((byte) (hasAccepted ? 1 : 0));
        parcel.writeStringList(specialRequests);
    }
}
