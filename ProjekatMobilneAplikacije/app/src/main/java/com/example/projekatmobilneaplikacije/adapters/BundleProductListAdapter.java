package com.example.projekatmobilneaplikacije.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.PriceListActivity;
import com.example.projekatmobilneaplikacije.activities.PriceListItemActivity;
import com.example.projekatmobilneaplikacije.activities.ProductDetailActivity;
import com.example.projekatmobilneaplikacije.activities.reservation.ReserveProductActivity;
import com.example.projekatmobilneaplikacije.fragments.CreateBundleSecondFragment;
import com.example.projekatmobilneaplikacije.fragments.CreateBundleThirdFragment;
import com.example.projekatmobilneaplikacije.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BundleProductListAdapter extends ArrayAdapter<Product> implements Filterable {
    private ArrayList<Product> aProducts;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final int REQUEST_CODE_EDIT_ITEM = 1;

    private Context mContext;

    public BundleProductListAdapter(Context context, ArrayList<Product> products){
        super(context, R.layout.product_card, products);
        aProducts = products;
        mContext = context;
    }
    /*
     * Ova metoda vraca ukupan broj elemenata u listi koje treba prikazati
     * */
    @Override
    public int getCount() {
        return aProducts.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Product getItem(int position) {
        return aProducts.get(position);
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
        Product product = getItem(position);

        if (mContext instanceof PriceListActivity) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.price_list_card, parent, false);
            }

            LinearLayout priceListCard = convertView.findViewById(R.id.price_list_card);
            TextView title = convertView.findViewById(R.id.title);
            TextView price = convertView.findViewById(R.id.price);
            TextView discount = convertView.findViewById(R.id.discount);
            TextView discountPrice = convertView.findViewById(R.id.discountPrice);

            if (product != null) {
                title.setText(product.getTitle());
                price.setText(String.valueOf(product.getPrice()));
                discount.setText(String.valueOf(product.getDiscount()));
                discountPrice.setText(String.valueOf(product.getPriceWithDiscount()));


                priceListCard.setOnClickListener(v -> {
                    // Handle click on the item at 'position'
                    Log.i("MobileApp", "Clicked: " + product.getTitle());

                    Intent intent = new Intent(getContext(), PriceListItemActivity.class);
                    intent.putExtra("itemType", "product");
                    intent.putExtra("productId", product.getId()); // Pass the product ID or any other identifier
                    intent.putExtra("title", product.getTitle());
                    intent.putExtra("price", product.getPrice());
                    intent.putExtra("discount", product.getDiscount());
                    //getContext().startActivity(intent);
                    ((Activity) getContext()).startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);

                    Toast.makeText(getContext(), "Clicked: " + product.getTitle(), Toast.LENGTH_SHORT).show();
                });
            }
        } else {

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_card,
                        parent, false);
            }

            LinearLayout productCard = convertView.findViewById(R.id.product_card_item);
            ImageView imageView = convertView.findViewById(R.id.product_image);
            TextView productTitle = convertView.findViewById(R.id.product_title);
            TextView productEventType = convertView.findViewById(R.id.product_event_type);
            TextView productCategory = convertView.findViewById(R.id.product_category);
            TextView productSubcategory = convertView.findViewById(R.id.product_subcategory);
            TextView productPrice = convertView.findViewById(R.id.product_price);



            if(product != null) {

                productTitle.setText(product.getTitle());
                productEventType.setText(product.getEventType());
                productSubcategory.setText(product.getCategory());
                productCategory.setText(product.getSubcategory());
                productPrice.setText(String.valueOf(product.getPrice()));




            }

        }
        return convertView;  ///super.getView(position, convertView, parent);
    }




}
