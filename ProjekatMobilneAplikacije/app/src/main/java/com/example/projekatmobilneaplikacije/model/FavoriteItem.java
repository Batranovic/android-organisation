package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FavoriteItem implements Parcelable {
    private String id;
    private String title;


    public FavoriteItem() {
        // Default constructor required for calls to DataSnapshot.getValue(FavoriteItem.class)
    }

    public FavoriteItem(String id, String title) {
        this.id = id;
        this.title = title;

    }

    protected FavoriteItem(Parcel in) {
        id = in.readString();
        title = in.readString();

    }

    public static final Creator<FavoriteItem> CREATOR = new Creator<FavoriteItem>() {
        @Override
        public FavoriteItem createFromParcel(Parcel in) {
            return new FavoriteItem(in);
        }

        @Override
        public FavoriteItem[] newArray(int size) {
            return new FavoriteItem[size];
        }
    };

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



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);

    }
}
