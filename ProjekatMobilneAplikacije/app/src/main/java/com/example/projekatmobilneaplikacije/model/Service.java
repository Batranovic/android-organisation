package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Service implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String specificity;
    private String discount;
    private String category;
    private String subcategory;
    private String duration;
    private String location;
    private int price;
    private String eventType;
    private String availability;
    private String visibility;
    private String engagement;
    private String reservationDeadline;
    private String cancellationDeadline;
    private String confirmationMode;
    private String image;
    private boolean isDeleted;

    public Service(){
    }

    public Service(String id, String title, String description, String specificity, String discount, String category, String subcategory, String duration, String location, int price, String eventType, String availability, String visibility, String engagement, String reservationDeadline, String cancellationDeadline, String confirmationMode, String image, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.specificity = specificity;
        this.discount = discount;
        this.category = category;
        this.subcategory = subcategory;
        this.duration = duration;
        this.location = location;
        this.price = price;
        this.eventType = eventType;
        this.availability = availability;
        this.visibility = visibility;
        this.engagement = engagement;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.confirmationMode = confirmationMode;
        this.image = image;
        this.isDeleted = isDeleted;
    }

    public Service(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        specificity = in.readString();
        discount = in.readString();
        category = in.readString();
        duration = in.readString();
        location = in.readString();
        price = in.readInt();
        eventType = in.readString();
        availability = in.readString();
        visibility = in.readString();
        engagement = in.readString();
        reservationDeadline = in.readString();
        cancellationDeadline = in.readString();
        confirmationMode = in.readString();
        image = in.readString();
        isDeleted = in.readBoolean();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecificity() {
        return specificity;
    }

    public void setSpecificity(String specificity) {
        this.specificity = specificity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getEngagement() {
        return engagement;
    }

    public void setEngagement(String engagement) {
        this.engagement = engagement;
    }

    public String getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(String reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public String getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(String cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public String getConfirmationMode() {
        return confirmationMode;
    }

    public void setConfirmationMode(String confirmationMode) {
        this.confirmationMode = confirmationMode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(specificity);
        dest.writeString(discount);
        dest.writeString(category);
        dest.writeString(duration);
        dest.writeString(location);
        dest.writeInt(price);
        dest.writeString(eventType);
        dest.writeString(availability);
        dest.writeString(visibility);
        dest.writeString(engagement);
        dest.writeString(reservationDeadline);
        dest.writeString(cancellationDeadline);
        dest.writeString(confirmationMode);
        dest.writeString(image);
        dest.writeBoolean(isDeleted);
    }



    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };
}
