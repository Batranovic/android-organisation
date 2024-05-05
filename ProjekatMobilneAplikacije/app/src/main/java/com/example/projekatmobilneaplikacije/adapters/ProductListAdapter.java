package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.content.Intent;
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

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.ProductDetailActivity;
import com.example.projekatmobilneaplikacije.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product> implements Filterable {
    private ArrayList<Product> aProducts;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProductListAdapter(Context context, ArrayList<Product> products){
        super(context, R.layout.product_card, products);
        aProducts = products;

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


        if(product != null){
            imageView.setImageResource(product.getImageId());
            productTitle.setText(product.getTitle());
            productEventType.setText(product.getEventType());
            productSubcategory.setText(product.getCategory());
            productCategory.setText(product.getSubcategory());
            productPrice.setText(String.valueOf(product.getPrice()));
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
                getContext().startActivity(intent);
                Toast.makeText(getContext(), "Clicked: " + product.getTitle(), Toast.LENGTH_SHORT).show();
            });
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
                    filteredProducts.addAll(aProducts);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Product product : aProducts) {
                        if (product.getTitle().toLowerCase().contains(filterPattern)) {
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
            if(product.getCategory().equalsIgnoreCase(category)) {
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
            if(product.getSubcategory().equalsIgnoreCase(subcategory)) {
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
            if(product.getEventType().equalsIgnoreCase(eventType)) {
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
            if(product.getAvailability().equalsIgnoreCase(availability)) {
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
            if(price >= minPrice && price <= maxPrice) {
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
            if (product.getDescription().toLowerCase().contains(description.toLowerCase())) {
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




}
