package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;

import java.util.ArrayList;

public class ServiceListAdapter extends ArrayAdapter<Service> {
    private ArrayList<Service> aServices;

    public ServiceListAdapter(Context context, ArrayList<Service> services){
        super(context, R.layout.service_card, services);
        aServices = services;

    }
    /*
     * Ova metoda vraca ukupan broj elemenata u listi koje treba prikazati
     * */
    @Override
    public int getCount() {
        return aServices.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Service getItem(int position) {
        return aServices.get(position);
    }

    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra da bude identifikator.
     * Naravno mozemo iskoristiti i jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Ova metoda popunjava pojedinacan element ListView-a podacima.
     * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
     * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
     * uzece java objekat sa odredjene pozicije (model) koji cuva podatke,
     * i layout koji treba da prikaze te podatke (view) npr R.layout.product_card.
     * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
     * popuniti view podacima i poslati listview da prikaze, i nastavice
     * sledecu iteraciju.
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Service service = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_card,
                    parent, false);
        }
        LinearLayout serviceCard = convertView.findViewById(R.id.service_card_item);
        ImageView imageView = convertView.findViewById(R.id.service_image);
        TextView serviceTitle = convertView.findViewById(R.id.service_title);
        TextView serviceLocation = convertView.findViewById(R.id.service_location);
        TextView serviceCategory = convertView.findViewById(R.id.service_category);
        TextView serviceDuration = convertView.findViewById(R.id.service_duration);
        TextView servicePrice = convertView.findViewById(R.id.service_price);

        loadImageFromBase64String(service.getImage(), imageView);

        if(service != null){
            serviceTitle.setText(service.getTitle());
            serviceDuration.setText(String.valueOf(service.getDuration()));
            serviceLocation.setText(service.getLocation());
            serviceCategory.setText(service.getCategory());
            servicePrice.setText(String.valueOf(service.getPrice()));
            serviceCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + service.getTitle() + ", id: " +
                        service.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + service.getTitle()  +
                        ", id: " + service.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }

    public void filterByCategory(String category) {
        ArrayList<Service> filteredProducts = new ArrayList<>();
        for(Service service : aServices) {
            if(!service.isDeleted() && service.getCategory().equalsIgnoreCase(category)) {
                filteredProducts.add(service);
            }
        }

        if(filteredProducts.isEmpty()) {
            Log.d("ProductListAdapter", "No products found for category: " + category);
        } else {
            Log.d("ProductListAdapter", "Filtered products for category " + category + ": " + filteredProducts.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredProducts); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void filterBySubcategory(String subcategory) {
        ArrayList<Service> filteredServices = new ArrayList<>();
        for(Service service : aServices) {
            if(!service.isDeleted() && service.getSubcategory().equalsIgnoreCase(subcategory)) {
                filteredServices.add(service);
            }
        }

        if(filteredServices.isEmpty()) {
            Log.d("ProductListAdapter", "No products found for subcategory: " + subcategory);
        } else {
            Log.d("ProductListAdapter", "Filtered products for category " + subcategory + ": " + filteredServices.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredServices); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void filterByEventType(String eventType) {
        ArrayList<Service> filteredServices = new ArrayList<>();
        for(Service service : aServices) {
            if(!service.isDeleted() && service.getEventType().equalsIgnoreCase(eventType)) {
                filteredServices.add(service);
            }
        }

        if(filteredServices.isEmpty()) {
            Log.d("ServiceListAdapter", "No services found for subcategory: " + eventType);
        } else {
            Log.d("ServiceListAdapter", "Filtered services for category " + eventType + ": " + filteredServices.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredServices); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void filterByAvailability(String availability) {
        ArrayList<Service> filteredServices = new ArrayList<>();
        for(Service service : aServices) {
            if(!service.isDeleted() && service.getAvailability().equalsIgnoreCase(availability)) {
                filteredServices.add(service);
            }
        }

        if(filteredServices.isEmpty()) {
            Log.d("ProductListAdapter", "No products found for subcategory: " + availability);
        } else {
            Log.d("ProductListAdapter", "Filtered products for category " + availability + ": " + filteredServices.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredServices); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void filterByPrice(double minPrice, double maxPrice) {
        ArrayList<Service> filteredServices = new ArrayList<>();
        for(Service service : aServices) {
            double price = service.getPrice();
            if(!service.isDeleted() && price >= minPrice && price <= maxPrice) {
                filteredServices.add(service);
            }
        }
        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredServices); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }



    private void loadImageFromBase64String(String base64Image, ImageView imageView) {
        if (base64Image != null && !base64Image.isEmpty()) {
            // Dekodirajte Base64 string u byte[]
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);

            // Pretvorite byte[] u bitmapu
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Postavite bitmapu na ImageView
            imageView.setImageBitmap(decodedBitmap);
        } else {
            // Ako je base64Image null ili prazan, možete postaviti neku podrazumevanu sliku ili poruku
            // Na primer, postaviti ikonicu "slike nije dostupna"
            imageView.setImageResource(R.drawable.add_photo);
        }
    }
}
