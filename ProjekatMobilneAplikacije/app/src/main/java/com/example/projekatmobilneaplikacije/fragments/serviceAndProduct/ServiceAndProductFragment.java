package com.example.projekatmobilneaplikacije.fragments.serviceAndProduct;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.fragments.registration.RegistrationFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ServiceAndProductFragment extends Fragment {

    private RadioGroup radioGroupCategory;
    private RadioButton radioButtonSubcategory;
    private EditText editTextCatName2;

    public static ServiceAndProductFragment newInstance() {
        return new ServiceAndProductFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service_and_product, container, false);

        radioGroupCategory = rootView.findViewById(R.id.radioGroupCategory);
        radioButtonSubcategory = rootView.findViewById(R.id.sub_category);
        editTextCatName2 = rootView.findViewById(R.id.cat_name2);

        Button buttonCreate = rootView.findViewById(R.id.buttonCreate);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigacija ka ServiceAndProductPageFragment
                NavHostFragment.findNavController(ServiceAndProductFragment.this)
                        .navigate(R.id.action_nav_service_to_nav_service_product);
            }
        });
        radioGroupCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isCategorySelected = checkedId == R.id.category;

                if (isCategorySelected) {
                    rootView.findViewById(R.id.service).setEnabled(false);
                    rootView.findViewById(R.id.product).setEnabled(false);
                    editTextCatName2.setEnabled(false);
                    rootView.findViewById(R.id.cat_name2).setEnabled(false);
                    setRadioButtonColor(rootView.findViewById(R.id.service), Color.RED); // Postavi boju radiobuttona na crvenu
                    setRadioButtonColor(rootView.findViewById(R.id.product), Color.RED); // Postavi boju radiobuttona na crvenu
                } else {
                    rootView.findViewById(R.id.service).setEnabled(true);
                    rootView.findViewById(R.id.product).setEnabled(true);
                    editTextCatName2.setEnabled(true);
                    setRadioButtonColor(rootView.findViewById(R.id.service), Color.BLACK); // Vrati boju radiobuttona na podrazumevanu vrednost (crnu)
                    setRadioButtonColor(rootView.findViewById(R.id.product), Color.BLACK); // Vrati boju radiobuttona na podrazumevanu vrednost (crnu)
                }

            }
        });

        // Postavljanje slušača događaja na RadioButton "Subcategory"
        radioButtonSubcategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Omogući ili onemogući unos u editTextCatName2 ovisno o označenosti "Subcategory"
                editTextCatName2.setEnabled(isChecked);
            }
        });

        return rootView;
    }

    // Metoda za postavljanje boje radiobuttona
    private void setRadioButtonColor(RadioButton radioButton, int color) {
        ColorStateList colorStateList = new ColorStateList(new int[][]{{-android.R.attr.state_enabled}, {}},
                new int[]{color, radioButton.getCurrentTextColor()});
        radioButton.setButtonTintList(colorStateList);
    }





}
