package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;

import com.example.projekatmobilneaplikacije.model.enumerations.UserRole;

public class EventOrganizer {
    private String id;

    private UserDetails details;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public UserDetails getUserDetails() {
        return details;
    }

    public void setUserDetails(UserDetails id) {
        this.details = details;
    }

    public EventOrganizer(String id,UserDetails details) {
        this.id = id;
        this.details = details;
    }

    public EventOrganizer() {
    }


}
