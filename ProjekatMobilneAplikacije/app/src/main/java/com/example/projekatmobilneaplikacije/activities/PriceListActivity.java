package com.example.projekatmobilneaplikacije.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.BundleListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ServiceListAdapter;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PriceListActivity extends AppCompatActivity {
    public static final String ARG_PARAM = "product_list";
    private ProductListAdapter adapter;
    private ServiceListAdapter serviceAdapter;
    private BundleListAdapter bundleAdapter;

    public static ArrayList<Product> products = new ArrayList<Product>();
    public static ArrayList<Service> services = new ArrayList<Service>();
    public static ArrayList<CustomBundle> bundles = new ArrayList<CustomBundle>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference productsRef = db.collection("products");
    CollectionReference servicesRef = db.collection("services");
    CollectionReference bundlesRef = db.collection("bundles");

    ListView listView, listViewService, listViewBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_price_list);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ARG_PARAM)) {
            products = intent.getParcelableArrayListExtra(ARG_PARAM);
            adapter = new ProductListAdapter(this, products, null);
        }

        adapter = new ProductListAdapter(this, products, null);
        serviceAdapter = new ServiceListAdapter(this, services, null);
        bundleAdapter = new BundleListAdapter(this, bundles);

        productsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                products.clear();

                // Check if there are any documents in the collection
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Iterate through each document in the collection
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Convert each document to a Product object
                        Product product = documentSnapshot.toObject(Product.class);

                        products.add(product);
                    }
                    // After retrieving all products, update your adapter
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle case when there are no products in the collection
                    Log.d("PriceListActivity", "No products found");
                }
            }
        });

        servicesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                services.clear();

                // Check if there are any documents in the collection
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Iterate through each document in the collection
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Convert each document to a Product object
                        Service service = documentSnapshot.toObject(Service.class);

                        services.add(service);
                    }
                    // After retrieving all products, update your adapter
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    // Handle case when there are no products in the collection
                    Log.d("PriceListActivity", "No services found");
                }
            }
        });

        bundlesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                bundles.clear();

                // Check if there are any documents in the collection
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Iterate through each document in the collection
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Convert each document to a Product object
                        CustomBundle bundle = documentSnapshot.toObject(CustomBundle.class);

                        bundles.add(bundle);
                    }
                    // After retrieving all products, update your adapter
                    bundleAdapter.notifyDataSetChanged();
                } else {
                    // Handle case when there are no products in the collection
                    Log.d("PriceListActivity", "No bundles found");
                }
            }
        });

        listView = findViewById(R.id.productsList);
        listView.setAdapter(adapter);

        listViewService = findViewById(R.id.servicesList);
        listViewService.setAdapter(serviceAdapter);

        listViewBundle = findViewById(R.id.bundlesList);
        listViewBundle.setAdapter(bundleAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.price_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}