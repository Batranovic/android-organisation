package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BundleItem implements Parcelable {
    private String productName;
    private String eventName;
    private String productSubcategory;
    private String bundleId;
    private Double productPrice;
    private BundleItem(){

    }

    public BundleItem(String productName, String eventName, String productSubcategory, String bundleId, Double productPrice){
        this.productName = productName;
        this.eventName = eventName;
        this.productSubcategory = productSubcategory;
        this.bundleId = bundleId;
        this.productPrice = productPrice;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getProductSubcategory() {
        return productSubcategory;
    }

    public void setProductSubcategory(String productSubcategory) {
        this.productSubcategory = productSubcategory;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    protected BundleItem(Parcel in) {
        productName =  in.readString();
        eventName = in.readString();
        productSubcategory = in.readString();
        bundleId = in.readString();
        productPrice = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(eventName);
        dest.writeString(productSubcategory);
        dest.writeString(bundleId);
        dest.writeDouble(productPrice);

    }
    public static final Creator<BundleItem> CREATOR = new Creator<BundleItem>() {
        @Override
        public BundleItem createFromParcel(Parcel in) {
            return new BundleItem(in);
        }

        @Override
        public BundleItem[] newArray(int size) {
            return new BundleItem[size];
        }
    };
}
