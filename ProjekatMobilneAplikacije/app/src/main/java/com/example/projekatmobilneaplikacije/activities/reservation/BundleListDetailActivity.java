package com.example.projekatmobilneaplikacije.activities.reservation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.BundleProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.BundleServiceListAdapter;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BundleListDetailActivity extends AppCompatActivity {
    private BundleServiceListAdapter adapter;
    private BundleProductListAdapter padapter;

    private String bundleId;

    public static ArrayList<Service> services = new ArrayList<>();
    public static ArrayList<Product> products = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference servicesRef = db.collection("services");
    CollectionReference productsRef = db.collection("products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle_list_detail);

        if (getIntent() != null) {
            bundleId = getIntent().getStringExtra("bundleId");
        }

        ListView servicesListView = findViewById(R.id.servicesList);
        ListView productsListView = findViewById(R.id.productsList);

        if (bundleId != null) {
            // First, retrieve the bundle based on bundleId
            db.collection("bundles")
                    .whereEqualTo("id", bundleId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                CustomBundle bundle = documentSnapshot.toObject(CustomBundle.class);
                                if (bundle != null) {
                                    List<String> serviceIds = bundle.getServiceIds();
                                    if (serviceIds != null && !serviceIds.isEmpty()) {
                                        fetchServices(serviceIds);
                                    }
                                    List<String> productIds = bundle.getProductIds();
                                    if(productIds!=null && !productIds.isEmpty()){
                                        fetchProducts(productIds);
                                    }
                                }
                            }
                        } else {
                            Log.d("BundleListDetailActivity", "Bundle not found");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("BundleListDetailActivity", "Error fetching bundle", e);
                    });
        } else {
            Log.e("BundleListDetailActivity", "Bundle ID is null");
        }

        // Set up the adapter
        adapter = new BundleServiceListAdapter(this, services);
        servicesListView.setAdapter(adapter);

        padapter = new BundleProductListAdapter(this, products);
        productsListView.setAdapter(padapter);
    }

    private void fetchProducts(List<String> productIds) {
        products.clear();

        for (String productId : productIds) {
            productsRef.whereEqualTo("id", productId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                Product product = documentSnapshot.toObject(Product.class);
                                if (product != null) {
                                    products.add(product);
                                }
                            } else {
                                Log.d("BundleListDetailActivity", "Product not found for ID: " + productId);
                            }
                        }
                        padapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("BundleListDetailActivity", "Error fetching product with ID: " + productId, e);
                    });
        }
    }



    private void fetchServices(List<String> serviceIds) {
        services.clear();

        for (String serviceId : serviceIds) {
            servicesRef.whereEqualTo("id", serviceId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                Service service = documentSnapshot.toObject(Service.class);
                                if (service != null) {
                                    services.add(service);
                                }
                            } else {
                                Log.d("BundleListDetailActivity", "Service not found for ID: " + serviceId);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("BundleListDetailActivity", "Error fetching service with ID: " + serviceId, e);
                    });
        }
    }



}
