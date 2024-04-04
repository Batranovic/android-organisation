package com.example.projekatmobilneaplikacije.fragments.employees;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeProfileActivity;
import com.example.projekatmobilneaplikacije.adapters.EmployeeListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeesListBinding;
import com.example.projekatmobilneaplikacije.model.Employee;

import java.util.ArrayList;


public class EmployeesListFragment extends ListFragment {

    private EmployeeListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Employee> mEmployees;
    private FragmentEmployeesListBinding binding;

    public EmployeesListFragment() {
    }

    public static EmployeesListFragment newInstance(ArrayList<Employee> employees){
        EmployeesListFragment fragment = new EmployeesListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, employees);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("App", "onCreate Employee List Fragment");
        if (getArguments() != null) {
            mEmployees = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new EmployeeListAdapter(getActivity(), mEmployees);
            setListAdapter(adapter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("App", "onCreateView Employee List Fragment");
        binding = FragmentEmployeesListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = binding.list;


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}