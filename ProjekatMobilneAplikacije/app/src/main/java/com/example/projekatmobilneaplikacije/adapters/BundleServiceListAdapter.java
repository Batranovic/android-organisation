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
import com.example.projekatmobilneaplikacije.activities.reservation.BundleListDetailActivity;
import com.example.projekatmobilneaplikacije.activities.reservation.ReserveServiceActivity;
import com.example.projekatmobilneaplikacije.fragments.CreateBundleSecondFragment;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;

import java.util.ArrayList;

public class BundleServiceListAdapter extends ArrayAdapter<Service> {
    private ArrayList<Service> aServices;

    private Context mContext;



    public BundleServiceListAdapter(Context context, ArrayList<Service> services){
        super(context, R.layout.service_card, services);
        aServices = services;
        mContext = context;
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



            if(service != null) {
                serviceTitle.setText(service.getTitle());
                serviceDuration.setText(String.valueOf(service.getDuration()));
                servicePrice.setText(String.valueOf(service.getPrice()));
                serviceSpecificity.setText(service.getSpecificity());

                    serviceCard.setOnClickListener(v -> {
                        // Handle click on the item at 'position'
                        Log.i("MobileApp", "Clicked: " + service.getTitle() + ", id: " +
                                service.getId().toString());


                        // Handle click event
                        Intent intent = new Intent(mContext, ReserveServiceActivity.class);
                        intent.putExtra("serviceId", service.getId());
                        mContext.startActivity(intent);

                        Toast.makeText(getContext(), "Clicked: " + service.getTitle() +
                                ", id: " + service.getId().toString(), Toast.LENGTH_SHORT).show();
                    });


            }
        }

        return convertView;
    }













}
