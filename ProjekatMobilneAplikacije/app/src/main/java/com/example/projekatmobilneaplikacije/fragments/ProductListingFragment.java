package com.example.projekatmobilneaplikacije.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;


import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.CreateProductActivity;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.projekatmobilneaplikacije.databinding.FragmentProductListingBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListingFragment extends ListFragment implements SearchView.OnQueryTextListener{
    private static final String ARG_PARAM = "param";

    private String mParam1;

    private ProductListAdapter adapter;

    public static ArrayList<Product> products = new ArrayList<Product>();

    private FragmentProductListingBinding binding;

    ListView listView;
    SearchView searchView;
    ArrayList<String> productTitles = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference productsRef = db.collection("products");

    public ProductListingFragment() { }


    public static ProductListingFragment newInstance(ArrayList<Product> products) {
        ProductListingFragment fragment = new ProductListingFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, products);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            products = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new ProductListAdapter(getActivity(), products);
            setListAdapter(adapter);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_listing, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //prepareProductList(products);

        adapter = new ProductListAdapter(getActivity(), products);
        // Set adapter for the ListFragment
        setListAdapter(adapter);

        Button btnFilters = view.findViewById(R.id.btnFilters);
        btnFilters.setOnClickListener(v -> {
            Log.i("ProjekatMobilneAplikacije", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateProductActivity.class);
                startActivity(intent);
            }
        });

        searchView = view.findViewById(R.id.action_search);
        listView = view.findViewById(android.R.id.list);



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
                        // Add the product to your ArrayList or any other data structure
                        products.add(product);
                    }
                    // After retrieving all products, update your adapter
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle case when there are no products in the collection
                    Log.d("ProductListingFragment", "No products found");
                }
            }
        });

        for (Product product : products) {
            // Get the title of each product and add it to the productTitles ArrayList
            String title = product.getTitle();
            productTitles.add(title);
        }

        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("ProductListingFragment", "New text: " + newText);
        adapter.getFilter().filter(newText);

        return false;
    }




}