package com.example.projekatmobilneaplikacije.model;

import com.example.projekatmobilneaplikacije.model.enumerations.UserRole;

public class UserDetails {
    private String id;
    private String username;
    private String name;
    private String surname;
    private String home_address;
    private String phone;

    private UserRole role;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole id) {
        this.role = role;
    }

    public UserDetails(String id,String username,  String name, String surname, String home_address, String phone, UserRole role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.home_address = home_address;
        this.phone = phone;
        this.role = role;
    }

    public UserDetails() {
    }
}
