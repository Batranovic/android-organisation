package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Company implements Parcelable {
    private String email;
    private String name;
    private String address;
    private String phoneNumber;
    private String description;
    private String photo;

    private WorkingHours workingHours;

    public Company(String e, String n, String a, String p, String d, String ph, WorkingHours workingHours){
        this.email = e;
        this.name = n;
        this.address = a;
        this.phoneNumber = p;
        this.description = d;
        this.photo = ph;
        this.workingHours = workingHours;
    }

    public Company(){}
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHours workingHours) {
        this.workingHours = workingHours;
    }

    protected Company(Parcel in) {
        email = in.readString();
        name = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        description = in.readString();
        photo = in.readString();
    }
    @Override
    public String toString() {
        return "Company{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address=" + address +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeString(photo);
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };
}
