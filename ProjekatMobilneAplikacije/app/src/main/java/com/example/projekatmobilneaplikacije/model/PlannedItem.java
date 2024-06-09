package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projekatmobilneaplikacije.model.enumerations.SubcategoryType;

public class PlannedItem implements  Parcelable{
    private String subcategoryType;
    private Double amount;

    private PlannedItem(){

    }
    public PlannedItem(String type, Double amount){
        this.subcategoryType = type;
        this.amount = amount;
    }

    public String getSubcategoryType() {
        return subcategoryType;
    }

    public void setSubcategoryType(String subcategoryType) {
        this.subcategoryType = subcategoryType;
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

    protected PlannedItem(Parcel in) {
        subcategoryType =  in.readString();
        amount = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subcategoryType);
        dest.writeDouble(amount);
    }
    public static final Parcelable.Creator<PlannedItem> CREATOR = new Parcelable.Creator<PlannedItem>() {
        @Override
        public PlannedItem createFromParcel(Parcel in) {
            return new PlannedItem(in);
        }

        @Override
        public PlannedItem[] newArray(int size) {
            return new PlannedItem[size];
        }
    };


}
