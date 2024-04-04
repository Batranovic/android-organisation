package com.example.projekatmobilneaplikacije.fragments.employees;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeInformationBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeRegistrationBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EmployeeInformationFragment extends Fragment {


    FragmentEmployeeInformationBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EmployeeInformationFragment() {
        // Required empty public constructor
    }


    public static EmployeeInformationFragment newInstance(String param1, String param2) {
        EmployeeInformationFragment fragment = new EmployeeInformationFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentEmployeeInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton workingHoursButton = binding.floatingActionWh;
        workingHoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EmployeeWHOverview.newInstance("WH Frag", "Employee Working Hours overview"), getActivity(), false, R.id.employee_information);
            }
        });

        FloatingActionButton workCalendarButton = binding.floatingActionCal;
        workCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EmployeeWorkCalendarFragment.newInstance("Calendar Fragment ]", "Employee Calendar overview"), getActivity(), false, R.id.employee_information);
            }
        });

        FloatingActionButton deactivateButton = binding.deactivate;

        // Postavljanje OnClickListener-a na dugme
        deactivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ovde postavite akciju koja će se izvršiti kada korisnik klikne na dugme
                // Na primer, prikazivanje Toast-a
                Toast.makeText(getActivity(), "User deactivated!", Toast.LENGTH_SHORT).show();
            }
        });

        Spinner spinner = binding.servicesSpinner;
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.services));
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        return root;
    }
}