package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class Product implements Parcelable {
    private String title;
    private String description;
    private String category;
    private String subcategory;
    private int price;
    private boolean isAvailable;
    private boolean isVisible;
    private String eventType;
    private int image;

    public Product(String title, String description, String category, String subcategory, int price, boolean isAvailable, boolean isVisible, String eventType, int image) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
        this.isAvailable = isAvailable;
        this.isVisible = isVisible;
        this.eventType = eventType;
        this.image = image;
    }

    public Product() {
    }
    // Konstruktor za čitanje iz Parcel objekta
    protected Product(Parcel in) {
        title = in.readString();
        description = in.readString();
        category = in.readString();
        subcategory = in.readString();
        price = in.readInt();
        isAvailable = in.readBoolean();
        isVisible = in.readBoolean();
        eventType = in.readString();
        image = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public int getPrice() {
        return price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(subcategory);
        dest.writeString(eventType);
        dest.writeInt(price);
        dest.writeBoolean(isAvailable);
        dest.writeBoolean(isVisible);
        dest.writeInt(image);
    }

    @Override
    public String toString() {
        return "Product{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", isVisible=" + isVisible +
                ", eventType='" + eventType + '\'' +
                ", image=" + image +
                '}';
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
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
