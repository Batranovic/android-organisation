package com.example.projekatmobilneaplikacije.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.projekatmobilneaplikacije.adapters.EventListAdapter;
import com.example.projekatmobilneaplikacije.adapters.SubcategoryForEventAdapter;
import com.example.projekatmobilneaplikacije.adapters.SubcategorySuggestionListAdapter;
import com.example.projekatmobilneaplikacije.databinding.ListCreatedEventsBinding;
import com.example.projekatmobilneaplikacije.databinding.ListSubcategorysForeventBinding;
import com.example.projekatmobilneaplikacije.model.CreateEvent;
import com.example.projekatmobilneaplikacije.model.Subcategory;
import com.example.projekatmobilneaplikacije.model.SubcategorySuggestion;

import java.util.ArrayList;

public class ListSubCatForEventFragment extends ListFragment {
 private ListSubcategorysForeventBinding binding;
 private SubcategoryForEventAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Subcategory> mSubcat;



    public static ListSubCatForEventFragment newInstance(ArrayList<Subcategory>subcategories)
    {
        ListSubCatForEventFragment fragment = new ListSubCatForEventFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, subcategories);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ListSubcategorysForeventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubcat = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new SubcategoryForEventAdapter(getActivity(), mSubcat);
            setListAdapter(adapter);

        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
