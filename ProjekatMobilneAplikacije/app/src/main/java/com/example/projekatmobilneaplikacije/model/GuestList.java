package com.example.projekatmobilneaplikacije.model;

import java.util.List;

public class GuestList {
    private String name;
    private String age;
    private boolean isInvited;
    private boolean hasAccepted;
    private List<String> specialRequests;

    public GuestList() {
        // Default constructor required for calls to DataSnapshot.getValue(Guest.class)
    }

    public GuestList(String name, String age, boolean isInvited, boolean hasAccepted, List<String> specialRequests) {
        this.name = name;
        this.age = age;
        this.isInvited = isInvited;
        this.hasAccepted = hasAccepted;
        this.specialRequests = specialRequests;
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
}
