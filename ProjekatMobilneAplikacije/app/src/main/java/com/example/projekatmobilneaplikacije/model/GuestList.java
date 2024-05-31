package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GuestList implements Parcelable {
    private Long id;
    private String name_surname;
    private String ageGroup;
    private boolean isInvited;
    private boolean hasAccepted;
    private String specialRequests;


    public GuestList() {
    }

    public GuestList(String name_surname, String ageGroup, boolean isInvited, boolean hasAccepted, String specialRequests) {
        this.name_surname = name_surname;
        this.ageGroup = ageGroup;
        this.isInvited = isInvited;
        this.hasAccepted = hasAccepted;
        this.specialRequests = specialRequests;
    }

    // Getters and setters
    public String getName() {
        return name_surname;
    }

    public void setName(String name_surname) {
        this.name_surname = name_surname;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
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

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "name_surname='" + name_surname + '\'' +
                ", ageGroup='" + ageGroup + '\'' +
                ", isInvited=" + isInvited +
                ", hasAccepted=" + hasAccepted +
                ", specialRequests=" + specialRequests +
                '}';
    }


    protected GuestList(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name_surname = in.readString();
        ageGroup = in.readString();
        isInvited = in.readByte() != 0;
        hasAccepted = in.readByte() != 0;
        specialRequests = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name_surname);
        dest.writeString(ageGroup);
        dest.writeByte((byte) (isInvited ? 1 : 0));
        dest.writeByte((byte) (hasAccepted ? 1 : 0));
        dest.writeString(specialRequests);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
