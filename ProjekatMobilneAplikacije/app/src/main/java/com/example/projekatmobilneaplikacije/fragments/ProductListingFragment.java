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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

    private ArrayList<Product> originalProducts = new ArrayList<>();

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
        adapter = new ProductListAdapter(getActivity(), products);

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


            RadioGroup categoryRadioGroup = dialogView.findViewById(R.id.categoryRadioGroup);
            categoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                // Dobijanje izabrane kategorije
                RadioButton radioButton = dialogView.findViewById(checkedId);
                String selectedCategory = radioButton.getText().toString();

                // Primena izabrane kategorije na listu proizvoda
                adapter.filterByCategory(selectedCategory);
            });
            RadioGroup subcategoryRadioGroup = dialogView.findViewById(R.id.subcategoryRadioGroup);
            subcategoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

                RadioButton radioButtonSubcategory= dialogView.findViewById(checkedId);
                String selectedSubcategory = radioButtonSubcategory.getText().toString();

                adapter.filterBySubcategory(selectedSubcategory);
            });
            RadioGroup eventTypeRadioGroup = dialogView.findViewById(R.id.eventTypeRadioGroup);
            eventTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

                RadioButton radioButtonEventType= dialogView.findViewById(checkedId);
                String selectedEventType = radioButtonEventType.getText().toString();

                adapter.filterByEventType(selectedEventType);
            });
            RadioGroup availabilityRadioGroup = dialogView.findViewById(R.id.availabilityRadioGroup);
            availabilityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

                RadioButton radioButtonAvailability= dialogView.findViewById(checkedId);
                String selectedAvailability = radioButtonAvailability.getText().toString();

                adapter.filterByAvailability(selectedAvailability);
            });

            // Dodajte kod za dobijanje unetih vrednosti minimalne i maksimalne cene
            EditText minPriceEditText = dialogView.findViewById(R.id.minPrice);
            EditText maxPriceEditText = dialogView.findViewById(R.id.maxPrice);
            // Pronalaženje dugmeta za primenu filtera
            Button applyFilterButton = dialogView.findViewById(R.id.applyFilterButton);

            // Dodavanje slušača za klik na dugme za primenu filtera
            applyFilterButton.setOnClickListener(buttonView -> {
                // Dobijanje vrednosti minimalne i maksimalne cene iz EditText polja
                String minPriceString = minPriceEditText.getText().toString();
                String maxPriceString = maxPriceEditText.getText().toString();

                // Provera da li su uneti stringovi prazni pre konverzije u brojeve
                if (!minPriceString.isEmpty() && !maxPriceString.isEmpty()) {
                    try {
                        // Konvertujte stringove u double vrednosti
                        double minPrice = Double.parseDouble(minPriceString);
                        double maxPrice = Double.parseDouble(maxPriceString);

                        // Primena filtera po ceni na listu proizvoda
                        adapter.filterByPrice(minPrice, maxPrice);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // Handle exception if necessary
                        Toast.makeText(getContext(), "Invalid input. Please enter valid numbers.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle case if either or both input fields are empty
                    Toast.makeText(getContext(), "Please enter both minimum and maximum price.", Toast.LENGTH_SHORT).show();
                }
            });


            Button applyDescriptionFilterButton = dialogView.findViewById(R.id.applyDescriptionFilterButton);
            EditText descriptionEditText = dialogView.findViewById(R.id.description);

            applyDescriptionFilterButton.setOnClickListener(buttonView -> {
                // Dobijanje vrednosti unete u polje za opis
                String description = descriptionEditText.getText().toString();

                // Provera da li je unet opis
                if (!description.isEmpty()) {
                    // Primena filtera po opisu na listu proizvoda
                    adapter.filterByDescription(description);
                } else {
                    // Handle case when description is empty
                    Toast.makeText(getContext(), "Please enter a description.", Toast.LENGTH_SHORT).show();
                }
            });


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
        ArrayList<Product> filteredProducts = new ArrayList<Product>();

        for(Product product: products){
            if(product.getTitle().toLowerCase().contains(newText.toLowerCase())){
                filteredProducts.add(product);
            }
        }
        adapter = new ProductListAdapter(getContext(), filteredProducts);
        listView.setAdapter(adapter);

        return false;
    }



}