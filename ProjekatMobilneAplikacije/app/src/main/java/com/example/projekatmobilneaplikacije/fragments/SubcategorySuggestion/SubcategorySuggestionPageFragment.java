package com.example.projekatmobilneaplikacije.fragments.SubcategorySuggestion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentSubcategorySuggestionPageBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.model.SubcategorySuggestion;

import java.util.ArrayList;


public class SubcategorySuggestionPageFragment extends Fragment {
    private FragmentSubcategorySuggestionPageBinding binding;

    public static ArrayList<SubcategorySuggestion> subcategorySuggestions = new ArrayList<SubcategorySuggestion>();
    public static SubcategorySuggestionPageFragment newInstance() {
        return new SubcategorySuggestionPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSubcategorySuggestionPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prepareSubcategorySuggestionList(subcategorySuggestions);

        FragmentTransition.to(SubcategorySuggestionListFragment.newInstance(subcategorySuggestions), getActivity(),
                false, R.id.scroll_category_list);


        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void prepareSubcategorySuggestionList(ArrayList<SubcategorySuggestion> subcategorySuggestions){
        //subcategorySuggestions.add(new SubcategorySuggestion(1L, "Smestaj", Subcategory.PRODUCT ));
       // subcategorySuggestions.add(new SubcategorySuggestion(2L, "Foto i video", Subcategory.SERVICE));
       // subcategorySuggestions.add(new SubcategorySuggestion(3L, "Fotografije i Albumi", Subcategory.PRODUCT));
       // subcategorySuggestions.add(new SubcategorySuggestion(4L, "Nega i lepota",Subcategory.SERVICE ));
    }
}