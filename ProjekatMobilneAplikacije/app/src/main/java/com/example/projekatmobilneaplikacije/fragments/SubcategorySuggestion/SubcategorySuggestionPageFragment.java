package com.example.projekatmobilneaplikacije.fragments.SubcategorySuggestion;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentSubcategorySuggestionPageBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.model.Subcategory;
import com.example.projekatmobilneaplikacije.model.SubcategorySuggestion;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("subcategorysuggestions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        subcategorySuggestions.clear(); // Očisti listu pre nego što dodamo nove podatke iz baze
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            SubcategorySuggestion subcategorySuggestion = document.toObject(SubcategorySuggestion.class);
                            subcategorySuggestions.add(subcategorySuggestion);
                        }
                        // Pozivanje metode za prikazivanje liste kategorija
                        FragmentTransition.to(SubcategorySuggestionListFragment.newInstance(subcategorySuggestions), getActivity(),
                                false, R.id.scroll_subcategorysuggestion_list);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

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

}