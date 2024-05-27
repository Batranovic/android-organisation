package com.example.projekatmobilneaplikacije.model.enumerations;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.Company;
import com.example.projekatmobilneaplikacije.model.EventType;
import com.example.projekatmobilneaplikacije.model.Subcategory;
import com.example.projekatmobilneaplikacije.model.UserDetails;

import java.util.List;
import java.util.UUID;

public class Owner  implements Parcelable {
    private UUID id;
    private Company company;
    private UserDetails userDetails;

    private List<Category> categories;

    private List<EventType> eventTypes;


    public Owner(UUID id, Company company, UserDetails userDetails, List<Category> categories,  List<EventType> eventTypes){
        this.id = id;
        this.company = company;
        this.userDetails = userDetails;
        this.categories = categories;
        this.eventTypes = eventTypes;
    }

    public Owner(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }
    protected Owner(Parcel in) {
        id = UUID.fromString(in.readString());
        company = in.readParcelable(Company.class.getClassLoader());

    }
    @Override
    public String toString() {
        return "Owner{" +
                "id='" + id + '\'' +
                ", company='" + company + '\'' +
                ", userDetails='" + userDetails + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        dest.writeParcelable(company, flags);

    }

    public static final Creator<Owner> CREATOR = new Creator<Owner>() {
        @Override
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        @Override
        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };
}
