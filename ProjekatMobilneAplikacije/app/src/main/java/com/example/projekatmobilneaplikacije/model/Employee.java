package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Employee implements Parcelable {
    private Long id;
    private String name;
    private String surname;

    private String commonName;
    private String email;
    private String address;
    private String phoneNumber;
    private int image;

    public Employee(Long id, String name, String surname, String commonName, String email, String address, String phoneNumber,int image) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.commonName = commonName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.image = image;
    }

    public Employee() {
    }

    protected Employee(Parcel in) {
        id = in.readLong();
        name = in.readString();
        surname = in.readString();
        commonName = in.readString();
        email = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        image = in.readInt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone number='" + phoneNumber + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(commonName);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeInt(image);
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
}
