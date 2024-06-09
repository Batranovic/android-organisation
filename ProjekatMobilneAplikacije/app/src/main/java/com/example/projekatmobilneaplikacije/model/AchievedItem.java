package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projekatmobilneaplikacije.model.enumerations.SubcategoryType;

public class AchievedItem implements Parcelable{

    private String serviceOrProductName;
    private Double amount;

    private AchievedItem(){

    }

    public AchievedItem(String name, Double amount){
        this.serviceOrProductName = name;
        this.amount = amount;
    }
    public String getServiceOrProductName() {
        return serviceOrProductName;
    }

    public void setServiceOrProductName(String serviceOrProductName) {
        this.serviceOrProductName = serviceOrProductName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    protected AchievedItem(Parcel in) {
        serviceOrProductName =  in.readString();
        amount = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceOrProductName);
        dest.writeDouble(amount);
    }
    public static final Creator<AchievedItem> CREATOR = new Creator<AchievedItem>() {
        @Override
        public AchievedItem createFromParcel(Parcel in) {
            return new AchievedItem(in);
        }

        @Override
        public AchievedItem[] newArray(int size) {
            return new AchievedItem[size];
        }
    };


}
