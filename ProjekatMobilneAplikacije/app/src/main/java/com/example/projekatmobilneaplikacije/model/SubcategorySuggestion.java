package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projekatmobilneaplikacije.model.enumerations.SubcategoryType;


public class SubcategorySuggestion  implements Parcelable{

    private String name;
    private SubcategoryType subcategory;
    public SubcategorySuggestion( String name, SubcategoryType subcategory) {
        this.name = name;
        this.subcategory = subcategory;
    }

    public SubcategorySuggestion() {
    }
    // Konstruktor za čitanje iz Parcel objekta
    protected SubcategorySuggestion(Parcel in) {
        name = in.readString();
        subcategory = SubcategoryType.valueOf(in.readString());;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubcategoryType getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubcategoryType subcategory) {
        this.subcategory = subcategory;
    }


    @Override
    public String toString() {
        return "subcategorySuggestion" +
                "name='" + name + '\'' +
                ", subcategory='" + subcategory + '\'' +
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
        dest.writeString(subcategory.name());
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
