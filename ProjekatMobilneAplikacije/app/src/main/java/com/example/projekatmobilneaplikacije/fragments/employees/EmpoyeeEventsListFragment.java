package com.example.projekatmobilneaplikacije.fragments.employees;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.EmployeeEventListAdapter;
import com.example.projekatmobilneaplikacije.adapters.EmployeeListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeesListBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmpoyeeEventsListBinding;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Event;

import java.util.ArrayList;


public class EmpoyeeEventsListFragment extends ListFragment {

    FragmentEmpoyeeEventsListBinding binding;
    private static final String ARG_PARAM = "param";
    private ArrayList<Event> mEvents;

    private EmployeeEventListAdapter adapter;

    public EmpoyeeEventsListFragment() {
    }


    public static EmpoyeeEventsListFragment newInstance(ArrayList<Event> events){
        EmpoyeeEventsListFragment fragment = new EmpoyeeEventsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, events);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("App", "onCreate Employee List Fragment");
        if (getArguments() != null) {
            mEvents = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new EmployeeEventListAdapter(getActivity(), mEvents);
            setListAdapter(adapter);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEmpoyeeEventsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}