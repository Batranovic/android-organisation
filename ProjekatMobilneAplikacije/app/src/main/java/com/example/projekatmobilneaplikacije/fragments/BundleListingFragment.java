package com.example.projekatmobilneaplikacije.fragments;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.CreateBundleActivity;
import com.example.projekatmobilneaplikacije.activities.CreateProductActivity;
import com.example.projekatmobilneaplikacije.adapters.BundleListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        });

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateBundleActivity.class);
                startActivity(intent);
            }
        });

        /*ImageButton editBundleButton = view.findViewById(R.id.editBundleButton);
        editBundleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pokrenite aktivnost CreateProductActivity
                Intent intent = new Intent(getActivity(), CreateBundleActivity.class);
                startActivity(intent);
            }
        });*/
    }


}