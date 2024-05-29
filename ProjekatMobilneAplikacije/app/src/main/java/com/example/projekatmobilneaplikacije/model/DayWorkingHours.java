package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.sql.Time;

public class DayWorkingHours  implements Parcelable {
    private Date  startTime;
    private Date  endTime;

    public DayWorkingHours(Date  startTime, Date  endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayWorkingHours(){}

    public Date  getStartTime() {
        return startTime;
    }

    public void setStartTime(Date  startTime) {
        this.startTime = startTime;
    }

    public Date  getEndTime() {
        return endTime;
    }

    public void setEndTime(Date  endTime) {
        this.endTime = endTime;
    }

    protected DayWorkingHours(Parcel in) {
        long startTimeMillis = in.readLong();
        long endTimeMillis = in.readLong();
        startTime = startTimeMillis != -1 ? new Date(startTimeMillis) : null;
        endTime = endTimeMillis != -1 ? new Date(endTimeMillis) : null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startTime != null ? startTime.getTime() : -1);
        dest.writeLong(endTime != null ? endTime.getTime() : -1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DayWorkingHours> CREATOR = new Creator<DayWorkingHours>() {
        @Override
        public DayWorkingHours createFromParcel(Parcel in) {
            return new DayWorkingHours(in);
        }

        @Override
        public DayWorkingHours[] newArray(int size) {
            return new DayWorkingHours[size];
        }
    };

    @Override
    public String toString() {
        return "DayWorkingHours{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

}
