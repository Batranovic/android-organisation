package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projekatmobilneaplikacije.model.enumerations.Status;

import java.time.LocalDate;
import java.util.Date;


public class Report implements Parcelable {
    private String id;
    private String reportedEntityUsername; // ID of the company or user being reported
    private String reportedByUsername;
    private String reason;
    private Status status;
    private Date date;

    public Report() {
    }

    public Report(String id, String reportedEntityUsername, String reportedByUsername, String reason, Status status, Date date) {
        this.id = id;
        this.reportedEntityUsername = reportedEntityUsername;
        this.reportedByUsername = reportedByUsername;
        this.reason = reason;
        this.status = status;
        this.date = date;
    }

    // Konstruktor za čitanje iz Parcel objekta
    protected Report(Parcel in) {
        id = in.readString();
        reportedEntityUsername = in.readString();
        reportedByUsername = in.readString();
        reason = in.readString();
        status = Status.values()[in.readInt()];
        long dateMillis = in.readLong();
        date = dateMillis != -1 ? new Date(dateMillis) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(reportedEntityUsername);
        dest.writeString(reportedByUsername);
        dest.writeString(reason);
        dest.writeInt(status.ordinal());
        dest.writeLong(date != null ? date.getTime() : -1);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportedEntityUsername() {
        return reportedEntityUsername;
    }

    public void setReportedEntityUsername(String reportedEntityUsername) {
        this.reportedEntityUsername = reportedEntityUsername;
    }

    public String getReportedByUsername() {
        return reportedByUsername;
    }

    public void setReportedByUsername(String reportedByUsername) {
        this.reportedByUsername = reportedByUsername;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", reportedEntityUsername='" + reportedEntityUsername + '\'' +
                ", reportedByUsername='" + reportedByUsername + '\'' +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", date=" + date +
                '}';
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}

