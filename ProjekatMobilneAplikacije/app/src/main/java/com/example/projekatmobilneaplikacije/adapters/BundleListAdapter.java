package com.example.projekatmobilneaplikacije.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.PriceListActivity;
import com.example.projekatmobilneaplikacije.activities.PriceListBundleActivity;
import com.example.projekatmobilneaplikacije.activities.PriceListItemActivity;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.Service;

import java.util.ArrayList;

public class BundleListAdapter extends ArrayAdapter<CustomBundle> {
    private ArrayList<CustomBundle> aBundles;
    private Context mContext;
    private static final int REQUEST_CODE_EDIT_ITEM = 1;

    public BundleListAdapter(Context context, ArrayList<CustomBundle> bundles){
        super(context, R.layout.bundle_card, bundles);
        aBundles = bundles;
        mContext = context;
    }
    /*
     * Ova metoda vraca ukupan broj elemenata u listi koje treba prikazati
     * */
    @Override
    public int getCount() {
        return aBundles.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public CustomBundle getItem(int position) {
        return aBundles.get(position);
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
        CustomBundle bundle = getItem(position);

        if (mContext instanceof PriceListActivity) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.price_list_card, parent, false);
            }

            LinearLayout priceListCard = convertView.findViewById(R.id.price_list_card);
            TextView title = convertView.findViewById(R.id.title);
            TextView price = convertView.findViewById(R.id.price);
            TextView discount = convertView.findViewById(R.id.discount);
            TextView discountPrice = convertView.findViewById(R.id.discountPrice);

            if (bundle != null) {
                title.setText(bundle.getTitle());
                price.setText(String.valueOf(bundle.getPrice()));
                discount.setText(String.valueOf(bundle.getDiscount()));
                discountPrice.setText(String.valueOf(bundle.getPriceWithDiscount()));

                priceListCard.setOnClickListener(v -> {
                    // Handle click on the item at 'position'
                    Log.i("MobileApp", "Clicked: " + bundle.getTitle());

                    Intent intent = new Intent(getContext(), PriceListBundleActivity.class);
                    intent.putExtra("bundleId", bundle.getId()); // Pass the product ID or any other identifier
                    intent.putExtra("title", bundle.getTitle());
                    intent.putExtra("discount", bundle.getDiscount());
                    //getContext().startActivity(intent);
                    ((Activity) getContext()).startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);

                    Toast.makeText(getContext(), "Clicked: " + bundle.getTitle(), Toast.LENGTH_SHORT).show();
                });

            }
        } else {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bundle_card,
                        parent, false);
            }
            LinearLayout bundleCard = convertView.findViewById(R.id.bundle_card_item);
            ImageView imageView = convertView.findViewById(R.id.bundle_image);
            TextView bundleTitle = convertView.findViewById(R.id.bundle_title);
            TextView bundleEventType = convertView.findViewById(R.id.bundle_eventType);
            TextView bundleCategory = convertView.findViewById(R.id.bundle_category);
            TextView bundlePrice = convertView.findViewById(R.id.bundle_price);

            if(bundle != null){
                bundleTitle.setText(bundle.getTitle());
                bundleCategory.setText(bundle.getCategory());
                bundlePrice.setText(String.valueOf(bundle.getPrice()));
                if (bundle.getEventTypes() != null) {
                    bundleEventType.setText(TextUtils.join(", ", bundle.getEventTypes()));
                } else {
                    bundleEventType.setText(""); // Postavite na prazan string ako nema eventTypes
                }
                bundleCard.setOnClickListener(v -> {
                    // Handle click on the item at 'position'
                    Log.i("ShopApp", "Clicked: " + bundle.getTitle() + ", id: " +
                            bundle.getId().toString());


                    Toast.makeText(getContext(), "Clicked: " + bundle.getTitle()  +
                            ", id: " + bundle.getId().toString(), Toast.LENGTH_SHORT).show();
                });
            }
        }


        return convertView;
    }

    public void filterByCategory(String category) {
        ArrayList<CustomBundle> filteredBundles = new ArrayList<>();
        for(CustomBundle bundle : aBundles) {
            if(!bundle.isDeleted() && bundle.getCategory().equalsIgnoreCase(category)) {
                filteredBundles.add(bundle);
            }
        }

        if(filteredBundles.isEmpty()) {
            Log.d("BundleListAdapter", "No bundles found for category: " + category);
        } else {
            Log.d("BundleListAdapter", "Filtered bundles for category " + category + ": " + filteredBundles.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredBundles); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void filterBySubcategory(String subcategory) {
        ArrayList<CustomBundle> filteredBundles = new ArrayList<>();
        for(CustomBundle bundle : aBundles) {
            if(!bundle.isDeleted() && bundle.getSubcategories().contains(subcategory)) {
                filteredBundles.add(bundle);
            }
        }

        if(filteredBundles.isEmpty()) {
            Log.d("ProductListAdapter", "No products found for subcategory: " + subcategory);
        } else {
            Log.d("ProductListAdapter", "Filtered products for category " + subcategory + ": " + filteredBundles.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredBundles); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void filterByEventType(String eventType) {
        ArrayList<CustomBundle> filteredBundles = new ArrayList<>();
        for(CustomBundle bundle : aBundles) {
            if(!bundle.isDeleted() && bundle.getEventTypes().contains(eventType)) {
                filteredBundles.add(bundle);
            }
        }

        if(filteredBundles.isEmpty()) {
            Log.d("ServiceListAdapter", "No services found for subcategory: " + eventType);
        } else {
            Log.d("ServiceListAdapter", "Filtered services for category " + eventType + ": " + filteredBundles.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredBundles); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }


    public void filterByPrice(double minPrice, double maxPrice) {
        ArrayList<CustomBundle> filteredBundles = new ArrayList<>();
        for(CustomBundle bundle : aBundles) {
            double price = bundle.getPrice();
            if(!bundle.isDeleted() && price >= minPrice && price <= maxPrice) {
                filteredBundles.add(bundle);
            }
        }
        clear();
        addAll(filteredBundles);
        notifyDataSetChanged();
    }

}
