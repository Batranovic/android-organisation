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

public class ProductListAdapter extends ArrayAdapter<Product> implements Filterable {
    private ArrayList<Product> aProducts;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final int REQUEST_CODE_EDIT_ITEM = 1;

    private Context mContext;
    private CreateBundleThirdFragment mCreateBundleThirdFragment;

    public ProductListAdapter(Context context, ArrayList<Product> products, CreateBundleThirdFragment createBundleThirdFragment){
        super(context, R.layout.product_card, products);
        aProducts = products;
        this.aProducts = products != null ? products : new ArrayList<>(); // Initialize the list if null
        mContext = context;
        mCreateBundleThirdFragment = createBundleThirdFragment;
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

            loadImageFromBase64String(product.getImage(), imageView);


            if(product != null) {

                productTitle.setText(product.getTitle());
                productEventType.setText(product.getEventType());
                productSubcategory.setText(product.getCategory());
                productCategory.setText(product.getSubcategory());
                productPrice.setText(String.valueOf(product.getPrice()));
                if (mCreateBundleThirdFragment == null) {
                    productCard.setOnClickListener(v -> {
                        // Handle click on the item at 'position'
                        Log.i("MobileApp", "Clicked: " + product.getTitle());

                        // Handle click event
                        // Start ProductDetailActivity with detailed information about the clicked product

                        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                        intent.putExtra("productId", product.getId()); // Pass the product ID or any other identifier
                        intent.putExtra("title", product.getTitle());
                        intent.putExtra("description", product.getDescription());
                        intent.putExtra("subcategory", product.getSubcategory());
                        intent.putExtra("eventType", product.getEventType());
                        intent.putExtra("price", product.getPrice());
                        intent.putExtra("availability", product.getAvailability());
                        intent.putExtra("visibility", product.getVisibility());
                        intent.putExtra("image", product.getImage());
                        getContext().startActivity(intent);

                        Toast.makeText(getContext(), "Clicked: " + product.getTitle(), Toast.LENGTH_SHORT).show();
                    });

                } else {
                    Log.d("Create Bundle", "Bundle");
                }
            }

        }
        return convertView;  ///super.getView(position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Product> filteredProducts = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented, return all products
                    //filteredProducts.addAll(aProducts);
                    for (Product product : aProducts) {
                        if (!product.isDeleted()) {
                            filteredProducts.add(product);
                        }
                    }
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Product product : aProducts) {
                        if (!product.isDeleted() && product.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredProducts.add(product);
                        }
                    }
                }

                results.values = filteredProducts;
                results.count = filteredProducts.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((ArrayList<Product>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void filterByCategory(String category) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for(Product product : aProducts) {
            if(!product.isDeleted() && product.getCategory().equalsIgnoreCase(category)) {
                filteredProducts.add(product);
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
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for(Product product : aProducts) {
            if(!product.isDeleted() && product.getSubcategory().equalsIgnoreCase(subcategory)) {
                filteredProducts.add(product);
            }
        }

        if(filteredProducts.isEmpty()) {
            Log.d("ProductListAdapter", "No products found for subcategory: " + subcategory);
        } else {
            Log.d("ProductListAdapter", "Filtered products for category " + subcategory + ": " + filteredProducts.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredProducts); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void filterByEventType(String eventType) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for(Product product : aProducts) {
            if(!product.isDeleted() && product.getEventType().equalsIgnoreCase(eventType)) {
                filteredProducts.add(product);
            }
        }

        if(filteredProducts.isEmpty()) {
            Log.d("ProductListAdapter", "No products found for subcategory: " + eventType);
        } else {
            Log.d("ProductListAdapter", "Filtered products for category " + eventType + ": " + filteredProducts.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredProducts); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void filterByAvailability(String availability) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for(Product product : aProducts) {
            if(!product.isDeleted() && product.getAvailability().equalsIgnoreCase(availability)) {
                filteredProducts.add(product);
            }
        }

        if(filteredProducts.isEmpty()) {
            Log.d("ProductListAdapter", "No products found for subcategory: " + availability);
        } else {
            Log.d("ProductListAdapter", "Filtered products for category " + availability + ": " + filteredProducts.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredProducts); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void filterByPrice(double minPrice, double maxPrice) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for(Product product : aProducts) {
            double price = product.getPrice();
            if(!product.isDeleted() && price >= minPrice && price <= maxPrice) {
                filteredProducts.add(product);
            }
        }
        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredProducts); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void filterByDescription(String description) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (Product product : aProducts) {
            // Provera da li opis proizvoda sadrži uneti tekst (ignorišući velika i mala slova)
            if (!product.isDeleted() && product.getDescription().toLowerCase().contains(description.toLowerCase())) {
                filteredProducts.add(product);
            }
        }

        if (filteredProducts.isEmpty()) {
            Log.d("ProductListAdapter", "No products found for description: " + description);
        } else {
            Log.d("ProductListAdapter", "Filtered products for description " + description + ": " + filteredProducts.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredProducts); // Dodajte filtrirane proizvode u adapter
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
