package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projekatmobilneaplikacije.model.enumerations.Owner;

import java.util.Date;

public class RegistrationRequest implements Parcelable {
    public Boolean isApproved;
    public Owner owner;

    public Date sentRequest;
    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Boolean getIsApproved(){ return  isApproved;}
    public void setIsApproved(Boolean isApproved) {this.isApproved = isApproved;}

    public Date getSentRequest() {
        return sentRequest;
    }

    public void setSentRequest(Date sentRequest) {
        this.sentRequest = sentRequest;
    }
    public RegistrationRequest(Boolean isApproved, Owner owner, Date sentRequest) {
        this.isApproved = isApproved;
        this.owner = owner;
        this.sentRequest = sentRequest;
    }

    public RegistrationRequest(){}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isApproved ? 1 : 0));
        dest.writeParcelable(owner, flags);
        dest.writeLong(sentRequest != null ? sentRequest.getTime() : -1); // Čuvanje vremena kao long vrijednosti
    }

    protected RegistrationRequest(Parcel in) {
        isApproved = in.readByte() != 0;
        owner = in.readParcelable(Owner.class.getClassLoader());
        long time = in.readLong();
        sentRequest = time != -1 ? new Date(time) : null; // Rekonstrukcija datuma iz long vrijednosti
    }

    // Kreator (potreban za dekodiranje objekta)
    public static final Creator<RegistrationRequest> CREATOR = new Creator<RegistrationRequest>() {
        @Override
        public RegistrationRequest createFromParcel(Parcel source) {
            return new RegistrationRequest(source);
        }

        @Override
        public RegistrationRequest[] newArray(int size) {
            return new RegistrationRequest[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }
}

