package com.example.projekatmobilneaplikacije.fragments.employees;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeWHOverviewBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeWorkingHoursEditBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EmployeeWorkingHoursEditFragment extends Fragment {

    FragmentEmployeeWorkingHoursEditBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EmployeeWorkingHoursEditFragment() {
        // Required empty public constructor
    }


    public static EmployeeWorkingHoursEditFragment newInstance(String param1, String param2) {
        EmployeeWorkingHoursEditFragment fragment = new EmployeeWorkingHoursEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmployeeWorkingHoursEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button backButton = binding.backToWh;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EmployeeWHOverview.newInstance("WH Frag", "Employee Working Hours overview"), getActivity(), false, R.id.employee_information);
            }
        });

        Button finishButton = binding.finichWhEdit;
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EmployeeWHOverview.newInstance("WH Frag", "Employee Working Hours overview"), getActivity(), false, R.id.employee_information);
            }
        });

        return root;
    }
}