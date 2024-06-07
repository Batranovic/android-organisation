package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projekatmobilneaplikacije.model.enumerations.CompanyReviewReportStatus;

import java.util.Date;

public class CompanyReviewReport implements Parcelable {
    private String id;
    private String owner;
    private String commentId;
    private String reason;
    private Date reportDate;
    private CompanyReviewReportStatus status;

    public CompanyReviewReport() {
    }

    public CompanyReviewReport(String id, String owner, String commentId, String reason, Date reportDate, CompanyReviewReportStatus status) {
        this.id = id;
        this.owner = owner;
        this.commentId = commentId;
        this.reason = reason;
        this.reportDate = reportDate;
        this.status = status;
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

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public CompanyReviewReportStatus getStatus() {
        return status;
    }

    public void setStatus(CompanyReviewReportStatus status) {
        this.status = status;
    }

    protected CompanyReviewReport(Parcel in) {
        id = in.readString();
        owner = in.readString();
        commentId = in.readString();
        reason = in.readString();
        long tmpReportDate = in.readLong();
        reportDate = tmpReportDate != -1 ? new Date(tmpReportDate) : null;
        status = (CompanyReviewReportStatus) in.readValue(CompanyReviewReportStatus.class.getClassLoader());
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
        dest.writeLong(reportDate != null ? reportDate.getTime() : -1);
        dest.writeValue(status);
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
