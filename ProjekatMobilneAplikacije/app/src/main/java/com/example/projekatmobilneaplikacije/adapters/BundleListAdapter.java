package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.Service;

import java.util.ArrayList;

public class BundleListAdapter extends ArrayAdapter<CustomBundle> {
    private ArrayList<CustomBundle> aBundles;

    public BundleListAdapter(Context context, ArrayList<CustomBundle> bundles){
        super(context, R.layout.bundle_card, bundles);
        aBundles = bundles;

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
            bundleCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + bundle.getTitle() + ", id: " +
                        bundle.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + bundle.getTitle()  +
                        ", id: " + bundle.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
