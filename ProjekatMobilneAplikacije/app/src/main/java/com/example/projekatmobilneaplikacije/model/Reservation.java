package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projekatmobilneaplikacije.model.enumerations.Owner;
import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;
import com.google.firebase.firestore.auth.User;

import java.util.Date;

import javax.annotation.Nullable;

public class Reservation implements Parcelable {
    private String id;
    private Employee employee;
    private UserDetails eventOrganizer;
    private ReservationStatus status;
    private Service service;
    @Nullable
    private CustomBundle bundle;

    private Date from;
    private Date to;
    private EventOrganization event;

    public Reservation() {
    }

    public Reservation(String id, Employee employee, UserDetails eventOrganizer, ReservationStatus status, Service service, @Nullable CustomBundle bundle, Date from, Date to, EventOrganization event) {
        this.id = id;
        this.employee = employee;
        this.eventOrganizer = eventOrganizer;
        this.status = status;
        this.service = service;
        this.bundle = bundle;
        this.from = from;
        this.to = to;
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public UserDetails getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(UserDetails eventOrganizer) {
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

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public EventOrganization getEvent() {
        return event;
    }

    public void setEvent(EventOrganization event) {
        this.event = event;
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
        employee = in.readParcelable(Employee.class.getClassLoader());
        eventOrganizer =  in.readParcelable(UserDetails.class.getClassLoader());
        status = ReservationStatus.valueOf(in.readString());
        service = in.readParcelable(Service.class.getClassLoader());
        bundle = in.readParcelable(CustomBundle.class.getClassLoader());
        long startTimeMillis = in.readLong();
        long endTimeMillis = in.readLong();
        from = startTimeMillis != -1 ? new Date(startTimeMillis) : null;
        to = endTimeMillis != -1 ? new Date(endTimeMillis) : null;
        event = in.readParcelable(EventOrganization.class.getClassLoader());
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
        dest.writeParcelable(employee, flags);
        dest.writeParcelable(eventOrganizer, flags);
        dest.writeString(status.name());
        dest.writeParcelable(service, flags);
        dest.writeParcelable(bundle, flags);
        dest.writeLong(from != null ? from.getTime() : -1);
        dest.writeLong(to != null ? to.getTime() : -1);
        dest.writeParcelable(event, flags);
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