package com.example.projekatmobilneaplikacije.fragments.serviceAndProduct;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentServiceAndProductPageBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.Subcategory;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ServiceAndProductPageFragment extends Fragment {
    private FragmentServiceAndProductPageBinding binding;
    private RadioButton categoryRadioButton;
    private RadioButton subcategoryRadioButton;
    public static ArrayList<Category> categories = new ArrayList<>();
    public static ArrayList<Subcategory> subcategories = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentServiceAndProductPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicijalizacija radio dugmadi
        categoryRadioButton = root.findViewById(R.id.categoryFilter);
        subcategoryRadioButton = root.findViewById(R.id.subcategoryFilter);

        // Postavljanje slušalaca za promene selekcije u RadioGroup
        RadioGroup radioGroupType = root.findViewById(R.id.radioGroupType);
        radioGroupType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.categoryFilter) {
                // Ako je selektovano kategorija, prikaži kategorije
                FragmentTransition.to(ServiceAndProductListFragment.newInstance(categories), getActivity(),
                        false, R.id.scroll_category_list);
            } else if (checkedId == R.id.subcategoryFilter) {
                // Ako je selektovano podkategorija, prikaži podkategorije
                FragmentTransition.to(ServiceAndProductListFragment.newInstance(subcategories), getActivity(),
                        false, R.id.scroll_category_list);
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        categories.clear(); // Očisti listu pre nego što dodamo nove podatke iz baze
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Category category = document.toObject(Category.class);
                            categories.add(category);
                        }
                        // Pozivanje metode za prikazivanje liste kategorija
                        FragmentTransition.to(ServiceAndProductListFragment.newInstance(categories), getActivity(),
                                false, R.id.scroll_category_list);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });


        db.collection("subcategories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        subcategories.clear(); // Očisti listu pre nego što dodamo nove podatke iz baze
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Subcategory subcategory = document.toObject(Subcategory.class);
                            subcategories.add(subcategory);
                        }
                        // Pozivanje metode za prikazivanje liste kategorija
                        FragmentTransition.to(ServiceAndProductListFragment.newInstance(subcategories), getActivity(),
                                false, R.id.scroll_category_list);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        // Dugme za dodavanje nove kategorije
        Button createCategoryButton = root.findViewById(R.id.create_category);
        createCategoryButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ServiceAndProductPageFragment.this)
                        .navigate(R.id.action_nav_service_product_to_nav_service));

        // Dugme za pregled predloga za podkategorije
        Button viewSubcategorySuggestionButton = root.findViewById(R.id.view_subcategory_suggestion);
        viewSubcategorySuggestionButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ServiceAndProductPageFragment.this)
                        .navigate(R.id.action_nav_service_product_to_nav_subcategory_suggestion));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
