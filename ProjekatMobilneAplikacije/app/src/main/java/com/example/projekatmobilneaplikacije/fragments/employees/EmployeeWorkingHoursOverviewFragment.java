package com.example.projekatmobilneaplikacije.fragments.employees;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeWorkingHoursBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeWorkingHoursOverviewBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EmployeeWorkingHoursOverviewFragment extends Fragment {

    FragmentEmployeeWorkingHoursOverviewBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EmployeeWorkingHoursOverviewFragment() {
        // Required empty public constructor
    }

    public static EmployeeWorkingHoursOverviewFragment newInstance(String param1, String param2) {
        EmployeeWorkingHoursOverviewFragment fragment = new EmployeeWorkingHoursOverviewFragment();
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
        binding = FragmentEmployeeWorkingHoursOverviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton editButton = binding.floatingActionEdit;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EmployeeWorkingHoursEditFragment.newInstance("WH Edit Frag", "Employee Working Hours edit"), getActivity(), false, R.id.employee_wh_view);
            }
        });

        return root;
    }
}