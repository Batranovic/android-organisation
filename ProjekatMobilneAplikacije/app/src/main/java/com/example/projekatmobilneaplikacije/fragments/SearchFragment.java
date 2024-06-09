package com.example.projekatmobilneaplikacije.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.ChatActivity;
import com.example.projekatmobilneaplikacije.adapters.BundleListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ServiceListAdapter;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final int REQUEST_CODE_EDIT_ITEM = 1;
    private ProductListAdapter adapter;
    private ServiceListAdapter serviceAdapter;
    private BundleListAdapter bundleAdapter;

    public static ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Service> services = new ArrayList<>();
    public static ArrayList<CustomBundle> bundles = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference productsRef = db.collection("products");
    CollectionReference servicesRef = db.collection("services");
    CollectionReference bundlesRef = db.collection("bundles");
    ListView listView, listViewService, listViewBundle;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.productsList);
        listViewService = view.findViewById(R.id.servicesList);
        listViewBundle = view.findViewById(R.id.bundlesList);

        adapter = new ProductListAdapter(getActivity(), products, null);
        serviceAdapter = new ServiceListAdapter(getActivity(), services, null);
        bundleAdapter = new BundleListAdapter(getActivity(), bundles);

        listView.setAdapter(adapter);
        listViewService.setAdapter(serviceAdapter);
        listViewBundle.setAdapter(bundleAdapter);

        fetchProducts();
        fetchServices();
        fetchBundles();

        Button createButton = view.findViewById(R.id.filter_button);
        createButton.setOnClickListener(v -> {
            FilterLFragment filter = new FilterLFragment();
            // You can add fragment transaction here if needed
        });

        Button openPopupButton = view.findViewById(R.id.filter_button);
        openPopupButton.setOnClickListener(this::showPopupWindow);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });
    }

    private void fetchProducts() {
        productsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                products.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Product product = documentSnapshot.toObject(Product.class);
                        products.add(product);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("SearchFragment", "No products found");
                }
            }
        });
    }

    private void fetchServices() {
        servicesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                services.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Service service = documentSnapshot.toObject(Service.class);
                        services.add(service);
                    }
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    Log.d("SearchFragment", "No services found");
                }
            }
        });
    }

    private void fetchBundles() {
        bundlesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                bundles.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        CustomBundle bundle = documentSnapshot.toObject(CustomBundle.class);
                        bundles.add(bundle);
                    }
                    bundleAdapter.notifyDataSetChanged();
                } else {
                    Log.d("SearchFragment", "No bundles found");
                }
            }
        });
    }

    private void showPopupWindow(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.buttom_fillter, null);
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        popupWindow.showAsDropDown(anchorView);
    }
}
