package com.example.projekatmobilneaplikacije.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.CreateProductActivity;
import com.example.projekatmobilneaplikacije.activities.CreateServiceActivity;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ServiceListAdapter;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceListingFragment extends ListFragment {

    private static final String ARG_PARAM = "param";

    private String mParam1;

    private ServiceListAdapter adapter;

    public static ArrayList<Service> services = new ArrayList<Service>();

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
        });

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateServiceActivity.class);
                startActivity(intent);
            }
        });

        /*ImageButton editServiceButton = view.findViewById(R.id.editServiceButton);
        editServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateServiceActivity.class);
                startActivity(intent);
            }
        });*/
    }

    /*private void prepareServiceList(ArrayList<Service> services){
        services.add(new Service(1L, "Snimanje dronom", "Foto i video", 2, "okolina Novog Sada", 6000,R.drawable.drones));
        services.add(new Service(1L, "Snimanje kamerom 4k", "Foto i video", 1 , "okolina Novog Sada", 5000,R.drawable.drones));
    }*/
}