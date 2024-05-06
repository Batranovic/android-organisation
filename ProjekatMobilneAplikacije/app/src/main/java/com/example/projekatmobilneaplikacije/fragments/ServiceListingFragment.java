package com.example.projekatmobilneaplikacije.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.CreateServiceActivity;
import com.example.projekatmobilneaplikacije.adapters.ServiceListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentServiceListingBinding;
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
 * Use the {@link ServiceListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceListingFragment extends ListFragment implements SearchView.OnQueryTextListener{

    private static final String ARG_PARAM = "param";

    private String mParam1;

    private ServiceListAdapter adapter;

    public static ArrayList<Service> services = new ArrayList<Service>();

    private FragmentServiceListingBinding binding;

    ListView listView;
    SearchView searchView;
    ArrayList<String> serviceTitles = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference servicesRef = db.collection("services");

    public ServiceListingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceListingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceListingFragment newInstance(String param1, String param2) {
        ServiceListingFragment fragment = new ServiceListingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            services = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new ServiceListAdapter(getActivity(), services);
            setListAdapter(adapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_listing, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the adapter with the updated data
        adapter = new ServiceListAdapter(getActivity(), services);
        setListAdapter(adapter);

        Button btnFilters = view.findViewById(R.id.btnFilters);
        btnFilters.setOnClickListener(v -> {
            Log.i("ProjekatMobilneAplikacije", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_service_filter, null);
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

        });



        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateServiceActivity.class);
                startActivity(intent);
            }
        });

        searchView = view.findViewById(R.id.action_search);
        listView = view.findViewById(android.R.id.list);


        Button removeFiltersButton = view.findViewById(R.id.removeFiltersButton);
        removeFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshServiceList();
            }
        });

        servicesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                services.clear();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Service service = documentSnapshot.toObject(Service.class);

                        services.add(service);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("ServiceListingFragment", "No services found");
                }
            }
        });

        for (Service service : services) {
            String title = service.getTitle();
            serviceTitles.add(title);
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
        Log.d("ServiceListingFragment", "New text: " + newText);
        ArrayList<Service> filteredServices = new ArrayList<Service>();

        for(Service service: services){
            if(!service.isDeleted() && service.getTitle().toLowerCase().contains(newText.toLowerCase())){
                filteredServices.add(service);
            }
        }
        adapter = new ServiceListAdapter(getContext(), filteredServices);
        listView.setAdapter(adapter);

        return false;
    }

    public void refreshServiceList() {
        servicesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                services.clear();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Service service = documentSnapshot.toObject(Service.class);
                        services.add(service);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("ServiceListingFragment", "No services found");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshServiceList();
    }



}