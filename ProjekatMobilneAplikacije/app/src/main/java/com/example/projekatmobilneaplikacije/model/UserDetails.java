package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projekatmobilneaplikacije.model.enumerations.UserRole;

public class UserDetails implements Parcelable {
    private String username;
    private String name;
    private String surname;
    private String home_address;
    private String phone;
    private UserRole role;

    private Boolean isBlocked;

    public UserDetails(String username, String name, String surname, String home_address, String phone, UserRole role, Boolean isBlocked) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.home_address = home_address;
        this.phone = phone;
        this.role = role;
        this.isBlocked = false;
    }

    public UserDetails() {}

    protected UserDetails(Parcel in) {
        username = in.readString();
        name = in.readString();
        surname = in.readString();
        home_address = in.readString();
        phone = in.readString();
        role = UserRole.valueOf(in.readString());
        isBlocked = in.readBoolean();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(home_address);
        dest.writeString(phone);
        dest.writeString(role.name());
        dest.writeBoolean(isBlocked);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
