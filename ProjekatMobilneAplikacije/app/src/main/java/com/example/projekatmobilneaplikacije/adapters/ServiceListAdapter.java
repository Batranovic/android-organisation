package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.projekatmobilneaplikacije.activities.PriceListActivity;
import com.example.projekatmobilneaplikacije.activities.PriceListItemActivity;
import com.example.projekatmobilneaplikacije.activities.ProductDetailActivity;
import com.example.projekatmobilneaplikacije.activities.ServiceDetailActivity;
import com.example.projekatmobilneaplikacije.fragments.CreateBundleSecondFragment;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;

import java.util.ArrayList;

public class ServiceListAdapter extends ArrayAdapter<Service> {
    private ArrayList<Service> aServices;

    private Context mContext;
    private CreateBundleSecondFragment mCreateBundleSecondFragment;



    public ServiceListAdapter(Context context, ArrayList<Service> services, CreateBundleSecondFragment createBundleSecondFragment){
        super(context, R.layout.service_card, services);
        aServices = services;
        mContext = context;
        mCreateBundleSecondFragment = createBundleSecondFragment;
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

        if (mContext instanceof PriceListActivity) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.price_list_card, parent, false);
            }

            LinearLayout priceListCard = convertView.findViewById(R.id.price_list_card);
            TextView title = convertView.findViewById(R.id.title);
            TextView price = convertView.findViewById(R.id.price);
            TextView discount = convertView.findViewById(R.id.discount);
            TextView discountPrice = convertView.findViewById(R.id.discountPrice);

            if (service != null) {
                title.setText(service.getTitle());
                price.setText(String.valueOf(service.getPrice()));
                discount.setText(String.valueOf(service.getDiscount()));
                discountPrice.setText(String.valueOf(service.getPriceWithDiscount()));

                priceListCard.setOnClickListener(v -> {
                    // Handle click on the item at 'position'
                    Log.i("MobileApp", "Clicked: " + service.getTitle());

                    Intent intent = new Intent(getContext(), PriceListItemActivity.class);
                    intent.putExtra("itemType", "service");
                    intent.putExtra("serviceId", service.getId()); // Pass the product ID or any other identifier
                    intent.putExtra("title", service.getTitle());
                    intent.putExtra("price", service.getPrice());
                    intent.putExtra("discount", service.getDiscount());
                    getContext().startActivity(intent);

                    Toast.makeText(getContext(), "Clicked: " + service.getTitle(), Toast.LENGTH_SHORT).show();
                });
            }
        } else {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_card,
                        parent, false);
            }
            LinearLayout serviceCard = convertView.findViewById(R.id.service_card_item);
            ImageView imageView = convertView.findViewById(R.id.service_image);
            TextView serviceTitle = convertView.findViewById(R.id.service_title);
            TextView serviceDuration = convertView.findViewById(R.id.service_duration);
            TextView servicePrice = convertView.findViewById(R.id.service_price);
            TextView serviceSpecificity = convertView.findViewById(R.id.specificity);


            loadImageFromBase64String(service.getImage(), imageView);

            if(service != null) {
                serviceTitle.setText(service.getTitle());
                serviceDuration.setText(String.valueOf(service.getDuration()));
                servicePrice.setText(String.valueOf(service.getPrice()));
                serviceSpecificity.setText(service.getSpecificity());
                if (mCreateBundleSecondFragment == null) {
                    serviceCard.setOnClickListener(v -> {
                        // Handle click on the item at 'position'
                        Log.i("MobileApp", "Clicked: " + service.getTitle() + ", id: " +
                                service.getId().toString());


                        // Handle click event
                        Intent intent = new Intent(mContext, ServiceDetailActivity.class);
                        intent.putExtra("serviceId", service.getId());
                        intent.putExtra("title", service.getTitle());
                        intent.putExtra("description", service.getDescription());
                        intent.putExtra("subcategory", service.getSubcategory());
                        intent.putExtra("eventType", service.getEventType());
                        intent.putExtra("price", service.getPrice());
                        intent.putExtra("availability", service.getAvailability());
                        intent.putExtra("visibility", service.getVisibility());
                        intent.putExtra("specificity", service.getSpecificity());
                        intent.putExtra("discount", service.getDiscount());
                        intent.putExtra("duration", service.getDuration());
                        intent.putExtra("engagement", service.getEngagement());
                        intent.putExtra("reservationDeadline", service.getReservationDeadline());
                        intent.putExtra("cancellationDeadline", service.getCancellationDeadline());
                        intent.putExtra("confirmationMode", service.getConfirmationMode());
                        intent.putExtra("image", service.getImage());
                        mContext.startActivity(intent);

                        Toast.makeText(getContext(), "Clicked: " + service.getTitle() +
                                ", id: " + service.getId().toString(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Log.d("Create Bundle", "Bundle");
                }

            }
        }

        return convertView;
    }

    public void filterByCategory(String category) {
        ArrayList<Service> filteredServices = new ArrayList<>();
        for(Service service : aServices) {
            if(!service.isDeleted() && service.getCategory().equalsIgnoreCase(category)) {
                filteredServices.add(service);
            }
        }

        if(filteredServices.isEmpty()) {
            Log.d("ServiceListAdapter", "No services found for category: " + category);
        } else {
            Log.d("ServiceListAdapter", "Filtered services for category " + category + ": " + filteredServices.size());
        }

        clear();
        addAll(filteredServices);
        notifyDataSetChanged();
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
