package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;

import javax.annotation.Nullable;

public class Reservation implements Parcelable {
    private String id;
    private String owner;
    private String employee;
    private String eventOrganizer;
    private ReservationStatus status;
    private Service service;
    @Nullable
    private CustomBundle bundle;

    public Reservation() {
    }

    public Reservation(String id, String owner, String employee, String eventOrganizer, ReservationStatus status, Service service, @Nullable CustomBundle bundle) {
        this.id = id;
        this.owner = owner;
        this.employee = employee;
        this.eventOrganizer = eventOrganizer;
        this.status = status;
        this.service = service;
        this.bundle = bundle;
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

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Nullable
    public CustomBundle getBundle() {
        return bundle;
    }

    public void setBundle(@Nullable CustomBundle bundle) {
        this.bundle = bundle;
    }

    // Konstruktor za čitanje iz Parcel objekta
    protected Reservation(Parcel in) {
        id = in.readString();
        owner = in.readString();
        employee = in.readString();
        eventOrganizer = in.readString();
        status = ReservationStatus.valueOf(in.readString());
        service = in.readParcelable(Service.class.getClassLoader());
        bundle = in.readParcelable(CustomBundle.class.getClassLoader());
    }

    /*
     * Ova metoda opisuje vrste posebnih objekata koje vaša Parcelable implementacija sadrži.
     * Većinom se vraća 0, osim u slučajevima kada objekat uključuje File Descriptor,
     * u kom slučaju se vraća 1.*/
    @Override
    public int describeContents() {
        return 0;
    }
    /*
     * Metoda koja uzima dva argumenta: Parcel u koji se vaš objekat serijalizuje i
     * flags koje Android koristi za označavanje načina na koji se objekat treba
     * serijalizovati. U ovoj metodi trebate upisati sve potrebne podatke iz vašeg
     * objekta u Parcel.
     * */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(owner);
        dest.writeString(employee);
        dest.writeString(eventOrganizer);
        dest.writeString(status.name());
        dest.writeParcelable(service, flags);
        dest.writeParcelable(bundle, flags);
    }

    /*
     * Da biste omogućili Androidu da regeneriše vaš objekat iz Parcel-a,
     * morate da obezbedite statički CREATOR polje koje implementira Parcelable.Creator
     * interfejs. Ovaj interfejs ima dve metode:
     * - createFromParcel(Parcel source): Stvara i vraća novu instancu vaše klase,
     * popunjavajući je podacima iz Parcel objekta koji je prosleđen kao argument.
     * - newArray(int size): Vraća niz vaše klase, što Android koristi kada se
     * regenerišu nizovi Parcelable objekata.
     * */
    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };
}
