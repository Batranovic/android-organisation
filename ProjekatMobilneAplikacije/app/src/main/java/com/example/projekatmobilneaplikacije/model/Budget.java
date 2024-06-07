package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projekatmobilneaplikacije.model.enumerations.Owner;

import java.util.List;

public class Budget implements Parcelable {
    private String id;
    private List<PlannedItem> plannedItems;
    private List<AchievedItem> achievedItems;
    private Double plannedBudget;
    private Double spentBudget;

    public Budget(String id, List<PlannedItem> plannedItems,List<AchievedItem> achievedItems, Double plannedBudget, Double spentBudget) {
        this.id = id;
        this.plannedItems = plannedItems;
        this.achievedItems = achievedItems;
        this.plannedBudget = plannedBudget;
        this.spentBudget = spentBudget;
    }
    public Budget(){

    }
    public List<PlannedItem> getPlannedItems() {
        return plannedItems;
    }

    public void setPlannedItems(List<PlannedItem> plannedItems) {
        this.plannedItems = plannedItems;
    }

    public List<AchievedItem> getAchievedItems() {
        return achievedItems;
    }

    public void setAchievedItems(List<AchievedItem> achievedItems) {
        this.achievedItems = achievedItems;
    }

    public Double getPlannedBudget() {
        return plannedBudget;
    }

    public void setPlannedBudget(Double plannedBudget) {
        this.plannedBudget = plannedBudget;
    }

    public Double getSpentBudget() {
        return spentBudget;
    }

    public void setSpentBudget(Double spentBudget) {
        this.spentBudget = spentBudget;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    protected Budget(Parcel in) {
        id = in.readString();
        plannedItems = in.createTypedArrayList(PlannedItem.CREATOR);
        achievedItems = in.createTypedArrayList(AchievedItem.CREATOR);
        plannedBudget =  in.readDouble();
        spentBudget = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeTypedList(plannedItems);
        dest.writeTypedList(achievedItems);
        dest.writeDouble(plannedBudget);
        dest.writeDouble(spentBudget);
    }
    public static final Creator<Budget> CREATOR = new Creator<Budget>() {
        @Override
        public Budget createFromParcel(Parcel in) {
            return new Budget(in);
        }

        @Override
        public Budget[] newArray(int size) {
            return new Budget[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }
}
