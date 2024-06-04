package com.example.projekatmobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.UUID;


import androidx.annotation.NonNull;


public class Product implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String category;
    private String subcategory;
    private int price;
    private int discount;
    private String eventType;
    private String availability;
    private String visibility;
    private String image;
    private boolean isDeleted;

    public Product(String id, String title, String description, String category, String subcategory, int price, int discount, String eventType, String availability, String visibility, String image, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
        this.discount = discount;
        this.eventType = eventType;
        this.availability = availability;
        this.visibility = visibility;
        this.image = image;
        this.isDeleted = isDeleted;
    }

    public Product() {
    }
    // Konstruktor za čitanje iz Parcel objekta
    protected Product(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        category = in.readString();
        subcategory = in.readString();
        price = in.readInt();
        eventType = in.readString();
        availability = in.readString();
        visibility = in.readString();
        image = in.readString();
        isDeleted = in.readBoolean();
        discount = in.readInt();
    }


    public int getPriceWithDiscount() {
        return price - (price * discount / 100);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
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
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(subcategory);
        dest.writeString(eventType);
        dest.writeInt(price);
        dest.writeString(availability);
        dest.writeString(visibility);
        dest.writeString(image);
        dest.writeBoolean(isDeleted);
        dest.writeInt(discount);
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", price=" + price +
                ", eventType='" + eventType + '\'' +
                ", availability='" + availability + '\'' +
                ", visibility='" + visibility + '\'' +
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
