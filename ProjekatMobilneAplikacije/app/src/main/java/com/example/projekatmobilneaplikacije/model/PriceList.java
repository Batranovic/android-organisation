package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PriceList implements Parcelable {
    private String id;
    private String title;
    private int price;
    private int discount;
    private int discountPrice;

    public PriceList(String id, String title, int price, int discount, int discountPrice) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.discountPrice = price - (price * discount / 100);;
    }

    public static final Creator<PriceList> CREATOR = new Creator<PriceList>() {
        @Override
        public PriceList createFromParcel(Parcel in) {
            return new PriceList(in);
        }

        @Override
        public PriceList[] newArray(int size) {
            return new PriceList[size];
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

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected PriceList(Parcel in) {
        id = in.readString();
        price = in.readInt();
        discount = in.readInt();
        discountPrice = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(price);
        dest.writeInt(discount);
        dest.writeInt(discountPrice);
    }
}
