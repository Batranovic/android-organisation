package com.example.projekatmobilneaplikacije.fragments.employees;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeRegistrationBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeesListBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeRegistrationFragment extends Fragment {

    private FragmentEmployeeRegistrationBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmployeeRegistrationFragment() {
        // Required empty public constructor
    }


    public static EmployeeRegistrationFragment newInstance(String param1, String param2) {
        EmployeeRegistrationFragment fragment = new EmployeeRegistrationFragment();
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
        Log.i("App", "onCreateView Employee List Fragment");
        binding = FragmentEmployeeRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button nextButton = binding.nextWh;
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EmployeeWorkingHoursFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.employee_registration, fragment)
                        .addToBackStack(null)
                        .commit();
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