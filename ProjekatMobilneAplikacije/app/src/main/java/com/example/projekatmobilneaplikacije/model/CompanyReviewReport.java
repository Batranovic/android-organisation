package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class CompanyReviewReport implements Parcelable {
    private String id;
    private String owner;
    private String commentId;
    private String reason;

    public CompanyReviewReport() {
    }

    public CompanyReviewReport(String id, String owner, String commentId, String reason) {
        this.id = id;
        this.owner = owner;
        this.commentId = commentId;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    protected CompanyReviewReport(Parcel in) {
        id = in.readString();
        owner = in.readString();
        commentId = in.readString();
        reason = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(owner);
        dest.writeString(commentId);
        dest.writeString(reason);
    }

    public static final Creator<CompanyReviewReport> CREATOR = new Creator<CompanyReviewReport>() {
        @Override
        public CompanyReviewReport createFromParcel(Parcel in) {
            return new CompanyReviewReport(in);
        }

        @Override
        public CompanyReviewReport[] newArray(int size) {
            return new CompanyReviewReport[size];
        }
    };
}
