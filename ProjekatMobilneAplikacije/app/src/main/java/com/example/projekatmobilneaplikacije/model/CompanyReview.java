package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class CompanyReview implements Parcelable {
    private String id;
    private String company;
    private String text;
    private Double grade;
    private EventOrganizer eventOrganizer;
    private Date reviewDate;

    public CompanyReview() {
    }

    public CompanyReview(String id, String company, String text, Double grade, EventOrganizer eventOrganizer, Date reviewDate) {
        this.id = id;
        this.company = company;
        this.text = text;
        this.grade = grade;
        this.eventOrganizer = eventOrganizer;
        this.reviewDate = reviewDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public EventOrganizer getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(EventOrganizer eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    protected CompanyReview(Parcel in) {
        id = in.readString();
        company = in.readString();
        text = in.readString();
        grade = in.readDouble();
        eventOrganizer = in.readParcelable(UserDetails.class.getClassLoader());
        reviewDate = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(company);
        dest.writeString(text);
        dest.writeDouble(grade);
        dest.writeParcelable(eventOrganizer, flags);
        dest.writeLong(reviewDate != null ? reviewDate.getTime() : -1);
    }

    public static final Creator<CompanyReview> CREATOR = new Creator<CompanyReview>() {
        @Override
        public CompanyReview createFromParcel(Parcel in) {
            return new CompanyReview(in);
        }

        @Override
        public CompanyReview[] newArray(int size) {
            return new CompanyReview[size];
        }
    };
}
