package com.example.projekatmobilneaplikacije.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.example.projekatmobilneaplikacije.R;

public class ServiceAndProductFragment extends Fragment {

    private RadioGroup radioGroupCategory;
    private RadioButton radioButtonSubcategory;
    private EditText editTextCatName, editTextCatDesc, editTextCatName2;
    private Button buttonCreate;

    public ServiceAndProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service_and_product, container, false);

        // Inicijalizacija elemenata sučelja
        radioGroupCategory = rootView.findViewById(R.id.radioGroupCategory);
        radioButtonSubcategory = rootView.findViewById(R.id.sub_category);
        editTextCatName = rootView.findViewById(R.id.cat_name);
        editTextCatDesc = rootView.findViewById(R.id.cat_desc);
        editTextCatName2 = rootView.findViewById(R.id.cat_name2);
        buttonCreate = rootView.findViewById(R.id.buttonCreate);

        // Postavljanje slušača događaja na RadioGroup
        radioGroupCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isCategorySelected = checkedId == R.id.category;

                // Ako je odabrana kategorija "Category", onemogući opciju "Service", "Product" i unos u cat_name2
                if (isCategorySelected) {
                    rootView.findViewById(R.id.service).setEnabled(false);
                    rootView.findViewById(R.id.product).setEnabled(false);
                    editTextCatName2.setEnabled(false);
                    buttonCreate.setEnabled(false);
                    setRadioButtonColor(rootView.findViewById(R.id.service), Color.RED); // Postavi boju radiobuttona na crvenu
                    setRadioButtonColor(rootView.findViewById(R.id.product), Color.RED); // Postavi boju radiobuttona na crvenu
                } else {
                    rootView.findViewById(R.id.service).setEnabled(true);
                    rootView.findViewById(R.id.product).setEnabled(true);
                    editTextCatName2.setEnabled(true);
                    buttonCreate.setEnabled(true);
                    setRadioButtonColor(rootView.findViewById(R.id.service), Color.BLACK); // Vrati boju radiobuttona na podrazumevanu vrednost (crnu)
                    setRadioButtonColor(rootView.findViewById(R.id.product), Color.BLACK); // Vrati boju radiobuttona na podrazumevanu vrednost (crnu)
                }

                // Onemogući sve elemente za unos ako je odabrana kategorija "Category"
                editTextCatName.setEnabled(!isCategorySelected);
                editTextCatDesc.setEnabled(!isCategorySelected);
                editTextCatName2.setEnabled(!isCategorySelected);
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
