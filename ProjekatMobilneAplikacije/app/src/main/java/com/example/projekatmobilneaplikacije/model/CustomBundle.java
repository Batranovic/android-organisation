package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomBundle implements Parcelable  {
    private String id;
    private String title;
    private String description;
    private int price;
    private int discount;
    private List<String> images;
    private String visibility;
    private String availability;
    private String category;
    private List<String> subcategories;
    private List<String> eventTypes;
    private int reservationDeadline;
    private int cancellationDeadline;
    private String confirmationMode;

    private boolean isDeleted;
    private List<String> productIds;
    private List<String> serviceIds;

    public CustomBundle() { }

    public CustomBundle(String id, String title, String description, int price, int discount, List<String> images, String visibility, String availability, String category, List<String> subcategories, List<String> eventTypes, int reservationDeadline, int cancellationDeadline, String confirmationMode, boolean isDeleted, List<String> productIds, List<String> serviceIds) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.images = images;
        this.visibility = visibility;
        this.availability = availability;
        this.category = category;
        this.subcategories = subcategories;
        this.eventTypes = eventTypes;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.confirmationMode = confirmationMode;
        this.isDeleted = isDeleted;
        this.productIds = productIds;
        this.serviceIds = serviceIds;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public List<String> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<String> serviceIds) {
        this.serviceIds = serviceIds;
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


    public int getPriceWithDiscount() {
        return price - (price * discount / 100);
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }



    public String getConfirmationMode() {
        return confirmationMode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(int reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public void setConfirmationMode(String confirmationMode) {
        this.confirmationMode = confirmationMode;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


    @Override
    public String toString() {
        return "CustomBundle{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", images=" + images +
                ", visibility=" + visibility +
                ", availability=" + availability +
                ", category='" + category + '\'' +
                ", subcategories=" + subcategories +
                ", eventTypes=" + eventTypes +
                ", reservationDeadline=" + reservationDeadline +
                ", cancellationDeadline=" + cancellationDeadline +
                ", confirmationMode='" + confirmationMode + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }

    protected CustomBundle(Parcel in) {
        // Čitanje ostalih atributa proizvoda iz Parcel objekta
        id = in.readString();
        title = in.readString();
        description = in.readString();
        price = in.readInt();
        discount = in.readInt();
        images = in.createStringArrayList();
        visibility = in.readString();
        availability = in.readString();
        category = in.readString();
        subcategories = in.createStringArrayList();
        eventTypes = in.createStringArrayList();
        reservationDeadline = in.readInt();
        cancellationDeadline = in.readInt();
        confirmationMode = in.readString();
        isDeleted = in.readBoolean();
        productIds = in.createStringArrayList();
        serviceIds = in.createStringArrayList();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Pisanje atributa u Parcel objekat
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(price);
        dest.writeInt(discount);
        dest.writeStringList(images);
        dest.writeString(visibility);
        dest.writeString(availability);
        dest.writeString(category);
        dest.writeStringList(subcategories);
        dest.writeStringList(eventTypes);
        dest.writeInt(reservationDeadline);
        dest.writeInt(cancellationDeadline);
        dest.writeString(confirmationMode);
        dest.writeByte((byte) (isDeleted ? 1 : 0));
        dest.writeStringList(productIds);
        dest.writeStringList(serviceIds);
    }

    public static final Creator<CustomBundle> CREATOR = new Creator<CustomBundle>() {
        @Override
        public CustomBundle createFromParcel(Parcel in) {
            return new CustomBundle(in);
        }

        @Override
        public CustomBundle[] newArray(int size) {
            return new CustomBundle[size];
        }
    };


}
