package com.example.projekatmobilneaplikacije.fragments.SubcategorySuggestion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.ServiceAndProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.SubcategorySuggestionListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentServiceAndProductListBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentSubcategorySuggestionListBinding;
import com.example.projekatmobilneaplikacije.fragments.serviceAndProduct.ServiceAndProductListFragment;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.SubcategorySuggestion;

import java.util.ArrayList;


public class SubcategorySuggestionListFragment extends ListFragment {
    private FragmentSubcategorySuggestionListBinding binding;
    private SubcategorySuggestionListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<SubcategorySuggestion> mSubcategorySuggestions;

    public static SubcategorySuggestionListFragment newInstance(ArrayList<SubcategorySuggestion> subcategorieSuggestions){
        SubcategorySuggestionListFragment fragment = new SubcategorySuggestionListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, subcategorieSuggestions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSubcategorySuggestionListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubcategorySuggestions = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new SubcategorySuggestionListAdapter(getActivity(), mSubcategorySuggestions);
            setListAdapter(adapter);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

