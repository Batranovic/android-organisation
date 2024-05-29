package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projekatmobilneaplikacije.model.enumerations.Owner;

import java.util.List;

public class Budget implements Parcelable {
    private List<BudgetItem> budgetItems;
    private Double maxBudget;

    public Budget( List<BudgetItem> budgetItems, Double maxBudget) {
        this.budgetItems = budgetItems;
        this.maxBudget = maxBudget;
    }
    public Budget(){

    }
    public List<BudgetItem> getBudgetItems() {
        return budgetItems;
    }

    // Setter za budgetItems
    public void setBudgetItems(List<BudgetItem> budgetItems) {
        this.budgetItems = budgetItems;
    }

    // Getter za maxBudget
    public Double getMaxBudget() {
        return maxBudget;
    }

    // Setter za maxBudget
    public void setMaxBudget(Double maxBudget) {
        this.maxBudget = maxBudget;
    }

    protected Budget(Parcel in) {
        budgetItems = in.createTypedArrayList(BudgetItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(budgetItems);
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
    public String toString() {
        return "Owner{" +
                "company='" + budgetItems + '\'' +
                ", userDetails='" + maxBudget + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
