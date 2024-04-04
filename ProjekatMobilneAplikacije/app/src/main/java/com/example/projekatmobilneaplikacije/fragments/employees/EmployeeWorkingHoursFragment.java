package com.example.projekatmobilneaplikacije.fragments.employees;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.HomeActivity;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeWorkingHoursBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeesPageBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;


public class EmployeeWorkingHoursFragment extends Fragment {

    private FragmentEmployeeWorkingHoursBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EmployeeWorkingHoursFragment() {
        // Required empty public constructor
    }

    public static EmployeeWorkingHoursFragment newInstance(String param1, String param2) {
        EmployeeWorkingHoursFragment fragment = new EmployeeWorkingHoursFragment();
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
        binding = FragmentEmployeeWorkingHoursBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button backButton = binding.backToRegButton;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransition.to(EmployeeRegistrationFragment.newInstance("frag transition", "ergistration frag"), getActivity(), false, R.id.employee_registration);
            }
        });

        Button finishButton = binding.finishRegButton;
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}