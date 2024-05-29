package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class WorkingHours implements Parcelable {
    private Map<String, DayWorkingHours> dayWorkingHoursMap;

    public WorkingHours() {
        dayWorkingHoursMap = new HashMap<>();
    }

    public WorkingHours(Map<String, DayWorkingHours> dayWorkingHoursMap) {
        this.dayWorkingHoursMap = dayWorkingHoursMap;
    }

    public Map<String, DayWorkingHours> getDayWorkingHoursMap() {
        return dayWorkingHoursMap;
    }

    public void setDayWorkingHoursMap(Map<String, DayWorkingHours> dayWorkingHoursMap) {
        this.dayWorkingHoursMap = dayWorkingHoursMap;
    }

    protected WorkingHours(Parcel in) {
        dayWorkingHoursMap = in.readHashMap(DayWorkingHours.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(dayWorkingHoursMap);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WorkingHours> CREATOR = new Creator<WorkingHours>() {
        @Override
        public WorkingHours createFromParcel(Parcel in) {
            return new WorkingHours(in);
        }

        @Override
        public WorkingHours[] newArray(int size) {
            return new WorkingHours[size];
        }
    };

    @Override
    public String toString() {
        return "WorkingHours{" +
                "dayWorkingHoursMap=" + dayWorkingHoursMap +
                '}';
    }
}
