package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Service implements Parcelable {
    private Long id;
    private String title;
    private String category;
    private int duration;
    private String location;
    private int price;
    private int image;

    public Service(Long id, String title, String category, int duration, String location, int price, int image) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.duration = duration;
        this.location = location;
        this.price = price;
        this.image = image;
    }

    public Service(Parcel in) {
        id = in.readLong();
        title = in.readString();
        category = in.readString();
        price = in.readInt();
        duration = in.readInt();
        location = in.readString();
        image = in.readInt();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getDuration() {
        return duration;
    }

    public String getLocation() {
        return location;
    }

    public int getPrice() {
        return price;
    }

    public int getImage() {
        return image;
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
        dest.writeString(location);
        dest.writeInt(duration);
        dest.writeInt(price);
        dest.writeInt(image);
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", duration='" + duration + '\'' +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", image=" + image +
                '}';
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
