package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projekatmobilneaplikacije.model.enumerations.Status;

import java.time.LocalDate;
import java.util.Date;


public class Report implements Parcelable {
    private String id;
    private String reportedEntityId; // ID of the company or user being reported
    private String reportedById;
    private String reason;
    private Status status;
    private LocalDate date;

    public Report() {
    }

    public Report(String id, String reportedEntityId, String reportedById, String reason, Status status, LocalDate date) {
        this.id = id;
        this.reportedEntityId = reportedEntityId;
        this.reportedById = reportedById;
        this.reason = reason;
        this.status = status;
        this.date = date;
    }

    // Konstruktor za čitanje iz Parcel objekta
    protected Report(Parcel in) {
        id = in.readString();
        reportedEntityId = in.readString();
        reportedById = in.readString();
        reason = in.readString();
        status = Status.values()[in.readInt()];
        date = LocalDate.parse(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(reportedEntityId);
        dest.writeString(reportedById);
        dest.writeString(reason);
        dest.writeInt(status.ordinal());
        dest.writeString(date.toString());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportedEntityId() {
        return reportedEntityId;
    }

    public void setReportedEntityId(String reportedEntityId) {
        this.reportedEntityId = reportedEntityId;
    }

    public String getReportedById() {
        return reportedById;
    }

    public void setReportedById(String reportedById) {
        this.reportedById = reportedById;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", reportedEntityId='" + reportedEntityId + '\'' +
                ", reportedById='" + reportedById + '\'' +
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

