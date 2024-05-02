package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projekatmobilneaplikacije.model.enumerations.SubcategoryType;
import java.util.ArrayList;
import java.util.List;
public class EventType  implements Parcelable {
    private String name;
    private String description;
    private Boolean isActive;
    private List<Subcategory> subcategories; // Lista subkategorija

    public EventType(String name, String description, Boolean isActive, List<Subcategory> subcategories) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.subcategories = subcategories;
    }

    public EventType() {
    }
    // Konstruktor za čitanje iz Parcel objekta
    protected EventType(Parcel in) {
        // Čitanje ostalih atributa proizvoda iz Parcel objekta
        name = in.readString();
        description = in.readString();
        isActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
    @Override
    public String toString() {
        return "EventType{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' + ", isActive='" + isActive +
                '}';
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
        dest.writeString(name);
        dest.writeString(description);
        dest.writeValue(isActive);
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
    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
