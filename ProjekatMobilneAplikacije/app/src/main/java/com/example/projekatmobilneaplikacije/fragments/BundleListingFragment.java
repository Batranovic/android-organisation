package com.example.projekatmobilneaplikacije.fragments;

import android.content.Intent;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.CreateBundleActivity;
import com.example.projekatmobilneaplikacije.activities.CreateProductActivity;
import com.example.projekatmobilneaplikacije.adapters.BundleListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ServiceListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentBundleListingBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentServiceListingBinding;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BundleListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BundleListingFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    private String mParam1;

    private BundleListAdapter adapter;

    public static ArrayList<CustomBundle> bundles = new ArrayList<CustomBundle>();


    private FragmentBundleListingBinding binding;

    ListView listView;
    SearchView searchView;
    ArrayList<String> bundleTitles = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference bundlesRef = db.collection("bundles");

    public BundleListingFragment() {
        // Required empty public constructor
    }

    public static BundleListingFragment newInstance(ArrayList<CustomBundle> bundles) {
        BundleListingFragment fragment = new BundleListingFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, bundles);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundles = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new BundleListAdapter(getActivity(), bundles);
            setListAdapter(adapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bundle_listing, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Set up the adapter with the updated data
        adapter = new BundleListAdapter(getActivity(), bundles);
        setListAdapter(adapter);


        Button btnFilters = view.findViewById(R.id.btnFilters);
        btnFilters.setOnClickListener(v -> {
            Log.i("ProjekatMobilneAplikacije", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_bundle_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();


            RadioGroup categoryRadioGroup = dialogView.findViewById(R.id.categoryRadioGroup);
            categoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton radioButton = dialogView.findViewById(checkedId);
                String selectedCategory = radioButton.getText().toString();

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

        });

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateBundleActivity.class);
                startActivity(intent);
            }
        });

        searchView = view.findViewById(R.id.action_search);
        listView = view.findViewById(android.R.id.list);


        Button removeFiltersButton = view.findViewById(R.id.removeFiltersButton);
        removeFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshBundleList();
            }
        });

        bundlesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                bundles.clear();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        CustomBundle bundle = documentSnapshot.toObject(CustomBundle.class);

                        bundles.add(bundle);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("BundleListingFragment", "No bundles found");
                }
            }
        });
    }

    public void refreshBundleList() {
        bundlesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                bundles.clear();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        CustomBundle bundle = documentSnapshot.toObject(CustomBundle.class);
                        bundles.add(bundle);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("BundleListingFragment", "No bundles found");
                }
            }
        });
    }


}