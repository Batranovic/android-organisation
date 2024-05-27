package com.example.projekatmobilneaplikacije.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.ServiceListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateBundleSecondBinding;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateBundleSecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateBundleSecondFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentCreateBundleSecondBinding binding;

    String title, description, category, available, visible, discount;

    Uri selectedImageUri;


    private ListView listView;
    private ServiceListAdapter adapter;

    private ArrayList<Service> services = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference servicesRef = db.collection("services");

    private Service selectedService;


    public static CreateBundleSecondFragment newInstance(String title, String description, String category, String discount, String available, String visible, Uri selectedImageUri) {
        CreateBundleSecondFragment fragment = new CreateBundleSecondFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putString("category", category);
        args.putString("discount", discount);
        args.putString("available", available);
        args.putString("visible", visible);
        args.putParcelable("selectedImageUri", selectedImageUri);
        fragment.setArguments(args);
        return fragment;
    }



    public CreateBundleSecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateBundleSecondFragment.
     */
    // TODO: Rename and change types and number of parameters




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            services = getArguments().getParcelableArrayList(ARG_PARAM);
        }
        // Initialize the services ArrayList if it's null
        if (services == null) {
            services = new ArrayList<>();
        }
        // Initialize the adapter with the services ArrayList
        adapter = new ServiceListAdapter(getActivity(), services, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateBundleSecondBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        listView = root.findViewById(android.R.id.list);
        adapter = new ServiceListAdapter(getActivity(), services, this);
        listView.setAdapter(adapter);

        // Učitavanje servisa iz baze podataka
        loadServices();

        Bundle args = getArguments();
        if (args != null) {
            title = args.getString("title", "");
            description = args.getString("description", "");
            selectedImageUri = args.getParcelable("selectedImageUri");
            category = args.getString("category", "");
            discount = args.getString("discount", "");
            available = args.getString("available", "");
            visible = args.getString("visible", "");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedService = services.get(position);
                // Postavljanje odabrane pozadine za kliknuti element
                Toast.makeText(getContext(), "Clicked: " + selectedService, Toast.LENGTH_SHORT).show();
                Log.d("CreateBundleSecondFragment", "selected item: " + selectedService.getTitle());
                view.setBackgroundColor(ContextCompat.getColor(getContext(), androidx.cardview.R.color.cardview_dark_background));
            }
        });


        binding.nextFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment thirdFragment = CreateBundleThirdFragment.newInstance(selectedService, title, description, category, available, visible, discount, selectedImageUri);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.create_bundle_container, thirdFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return root;
    }

    private void loadServices() {
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
                    Log.d("CreateBundleSecondFragment", "No services found");
                }
            }
        });
    }

    /*public void setSelectedService(Service service) {
        selectedService = service;
        Toast.makeText(getContext(), "on setSelected: " + selectedService.getTitle(), Toast.LENGTH_LONG).show();
    }*/


}