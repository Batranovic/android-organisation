package com.example.projekatmobilneaplikacije.fragments.registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentRegisterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton fab = root.findViewById(R.id.work_time);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment timePickerDialogFragment = TimePickerDialogFragment.newInstance();

                 getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_registration_container, timePickerDialogFragment) // Replace current fragment with LogoutFragment
                        .addToBackStack(null) // Add to back stack so user can navigate back
                        .commit();
            }

        });

        FloatingActionButton selectCategory = root.findViewById(R.id.select_category);
        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectCategory.setOnClickListener(s -> {
                    Log.i("ShopApp", "Bottom Sheet Dialog");
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
                    View dialogView = getLayoutInflater().inflate(R.layout.category_sheet_filter, null);
                    bottomSheetDialog.setContentView(dialogView);
                    bottomSheetDialog.show();
                });

            }

        });

        FloatingActionButton selectEvent = root.findViewById(R.id.select_event_type);
        selectEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectEvent.setOnClickListener(s -> {
                    Log.i("ShopApp", "Bottom Sheet Dialog");
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
                    View dialogView = getLayoutInflater().inflate(R.layout.event_type_sheet_filter, null);
                    bottomSheetDialog.setContentView(dialogView);
                    bottomSheetDialog.show();
                });

            }

        });

        FloatingActionButton selectProductPackage = root.findViewById(R.id.select_product_package);
        selectProductPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectProductPackage.setOnClickListener(s -> {
                    Log.i("ShopApp", "Bottom Sheet Dialog");
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
                    View dialogView = getLayoutInflater().inflate(R.layout.srvice_product_package_sheet, null);
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



}