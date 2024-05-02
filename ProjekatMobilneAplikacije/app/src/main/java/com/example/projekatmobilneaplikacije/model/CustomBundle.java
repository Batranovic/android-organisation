package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CustomBundle implements Parcelable  {
    private Long id;
    private String title;
    private String category;
    private String eventType;
    private int price;
    private int image;

    public CustomBundle(Long id, String title, String category, String eventType, int price, int image) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.eventType = eventType;
        this.price = price;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Bundle{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", eventType='" + eventType + '\'' +
                ", price=" + price +
                ", image=" + image +
                '}';
    }

    protected CustomBundle(Parcel in) {
        // Čitanje ostalih atributa proizvoda iz Parcel objekta
        id = in.readLong();
        title = in.readString();
        category = in.readString();
        eventType = in.readString();
        price = in.readInt();
        image = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(eventType);
        dest.writeInt(price);
        dest.writeInt(image);
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
