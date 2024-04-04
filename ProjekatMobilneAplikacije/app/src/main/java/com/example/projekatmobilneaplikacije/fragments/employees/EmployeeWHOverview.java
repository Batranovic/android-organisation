package com.example.projekatmobilneaplikacije.fragments.employees;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeWHOverviewBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeesPageBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;


public class EmployeeWHOverview extends Fragment {

    FragmentEmployeeWHOverviewBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EmployeeWHOverview() {
        // Required empty public constructor
    }


    public static EmployeeWHOverview newInstance(String param1, String param2) {
        EmployeeWHOverview fragment = new EmployeeWHOverview();
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
        binding = FragmentEmployeeWHOverviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(EmployeeWorkingHoursOverviewFragment.newInstance("RADNO VREME", "RADNO VREME PRIKAZ"), getActivity(),
                false, R.id.employee_wh_view);

        return  root;
    }
}