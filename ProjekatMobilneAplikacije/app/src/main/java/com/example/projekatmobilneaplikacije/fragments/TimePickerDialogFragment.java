package com.example.projekatmobilneaplikacije.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentTimePickerDialogBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TimePickerDialogFragment extends Fragment {

    private FragmentTimePickerDialogBinding binding;

    public static TimePickerDialogFragment newInstance() {
        return new TimePickerDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTimePickerDialogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pop the back stack to remove TimePickerDialogFragment
                getParentFragmentManager().popBackStack();

                // Now replace TimePickerDialogFragment with RegisterFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_registration_container, RegisterFragment.newInstance())
                        .commit();
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
