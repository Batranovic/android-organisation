package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class CompanyReview implements Parcelable {
    private String id;
    private String company;
    private String owner;
    private String text;
    private Double grade;
    private String eventOrganizer;
    private Date reviewDate;
    private boolean isDeleted; // Dodano polje isDeleted

    public CompanyReview() {
    }

    public CompanyReview(String id, String company, String owner, String text, Double grade, String eventOrganizer, Date reviewDate, boolean isDeleted) {
        this.id = id;
        this.company = company;
        this.owner = owner;
        this.text = text;
        this.grade = grade;
        this.eventOrganizer = eventOrganizer;
        this.reviewDate = reviewDate;
        this.isDeleted = isDeleted;
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

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    protected CompanyReview(Parcel in) {
        id = in.readString();
        company = in.readString();
        owner = in.readString();
        text = in.readString();
        grade = in.readDouble();
        eventOrganizer = in.readString();
        reviewDate = new Date(in.readLong());
        isDeleted = in.readByte() != 0; // Čitanje isDeleted polja
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(company);
        dest.writeString(owner);
        dest.writeString(text);
        dest.writeDouble(grade);
        dest.writeString(eventOrganizer);
        dest.writeLong(reviewDate != null ? reviewDate.getTime() : -1);
        dest.writeByte((byte) (isDeleted ? 1 : 0));
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
