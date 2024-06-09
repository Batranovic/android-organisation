package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkCalendar implements Parcelable {
    private String employee;
    private Date workStart;
    private Date workEnd;
    private List<Event> events;

    public WorkCalendar() {
    }

    public WorkCalendar(String employee, Date workStart, Date workEnd, List<Event> events) {
        this.employee = employee;
        this.workStart = workStart;
        this.workEnd = workEnd;
        this.events = events;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }


    public Date getWorkStart() {
        return workStart;
    }

    public void setWorkStart(Date workStart) {
        this.workStart = workStart;
    }

    public Date getWorkEnd() {
        return workEnd;
    }

    public void setWorkEnd(Date workEnd) {
        this.workEnd = workEnd;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }


    protected WorkCalendar(Parcel in) {
        employee = in.readString();
        workStart = new Time(in.readLong());
        workEnd = new Time(in.readLong());
        if (in.readByte() == 0x01) {
            events = new ArrayList<Event>();
            in.readList(events, Event.class.getClassLoader());
        } else {
            events = null;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(employee);
        dest.writeLong(workStart != null ? workStart.getTime() : -1L);
        dest.writeLong(workEnd != null ? workEnd.getTime() : -1L);
        if (events == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(events);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<WorkCalendar> CREATOR = new Parcelable.Creator<WorkCalendar>() {
        @Override
        public WorkCalendar createFromParcel(Parcel in) {
            return new WorkCalendar(in);
        }

        @Override
        public WorkCalendar[] newArray(int size) {
            return new WorkCalendar[size];
        }
    };

}
