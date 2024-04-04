package com.example.projekatmobilneaplikacije.fragments.events;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentEventTypeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EventTypeFragment extends Fragment {
    private FragmentEventTypeBinding binding;
    private boolean isFirstSelection = true;
    private Spinner spinner;
    private RadioGroup radioGroup;

    public static EventTypeFragment newInstance() {
        return new EventTypeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEventTypeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton selectSubcategory = root.findViewById(R.id.select_subcategory);
        selectSubcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectSubcategory.setOnClickListener(s -> {
                    Log.i("ShopApp", "Bottom Sheet Dialog");
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
                    View dialogView = getLayoutInflater().inflate(R.layout.service_and_product_sheet, null);
                    bottomSheetDialog.setContentView(dialogView);
                    bottomSheetDialog.show();
                });

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

    private void setRadioButtonColor(RadioButton radioButton, int color) {
        ColorStateList colorStateList = new ColorStateList(new int[][]{{-android.R.attr.state_enabled}, {}},
                new int[]{color, radioButton.getCurrentTextColor()});
        radioButton.setButtonTintList(colorStateList);
    }
}
