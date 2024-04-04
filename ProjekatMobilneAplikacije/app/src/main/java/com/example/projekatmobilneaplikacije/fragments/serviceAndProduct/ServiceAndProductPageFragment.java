package com.example.projekatmobilneaplikacije.fragments.serviceAndProduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentServiceAndProductPageBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.model.Category;

import java.util.ArrayList;

public class ServiceAndProductPageFragment extends Fragment {
    private FragmentServiceAndProductPageBinding binding;

    public static ArrayList<Category> categories = new ArrayList<Category>();
    public static ServiceAndProductPageFragment newInstance() {
        return new ServiceAndProductPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentServiceAndProductPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prepareCategoryList(categories);
        //dugme iz fragment_login.xml
        Button createCategoryButton = root.findViewById(R.id.create_category);
        createCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigacija na drugi fragment
                NavHostFragment.findNavController(ServiceAndProductPageFragment.this)
                        .navigate(R.id.action_nav_service_product_to_nav_service);
            }
        });
        Button viewsubategorySuggestionButton = root.findViewById(R.id.view_subcategory_suggestion);
        viewsubategorySuggestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigacija na drugi fragment
                NavHostFragment.findNavController(ServiceAndProductPageFragment.this)
                        .navigate(R.id.action_nav_service_product_to_nav_subcategory_suggestion);
            }
        });
        FragmentTransition.to(ServiceAndProductListFragment.newInstance(categories), getActivity(),
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
    private void prepareCategoryList(ArrayList<Category> products){
        products.add(new Category(1L, "Smestaj", "Hoteli, vile, apartmani i druge opcije smeštaja za goste događaja."));
        products.add(new Category(2L, "Foto i video", "Profesionalno fotografisanje i snimanje događaja."));
        products.add(new Category(3L, "Garderoba i stilizovanje", "Iznajmljivanje ili kupovina formalne odeće, usluge stilista."));
        products.add(new Category(4L, "Nega i lepota", "Frizerske usluge, makeup artisti, manikir/pedikir."));
    }
}