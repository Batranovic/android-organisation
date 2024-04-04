package com.example.projekatmobilneaplikacije.fragments.employees;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeRegistrationActivity;
import com.example.projekatmobilneaplikacije.activities.events.NewEventActivity;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeWorkCalendarBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeWorkingHoursOverviewBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class EmployeeWorkCalendarFragment extends Fragment {

    FragmentEmployeeWorkCalendarBinding binding;
    public static ArrayList<Event> events = new ArrayList<Event>();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EmployeeWorkCalendarFragment() {
        // Required empty public constructor
    }


    public static EmployeeWorkCalendarFragment newInstance(String param1, String param2) {
        EmployeeWorkCalendarFragment fragment = new EmployeeWorkCalendarFragment();
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
        binding = FragmentEmployeeWorkCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prepareEventList(events);

        FragmentTransition.to(EmpoyeeEventsListFragment.newInstance(events), getActivity(),
                false, R.id.events);

        FloatingActionButton floatingActionButton = root.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Otvorite novu aktivnost kada se klikne na floating dugme
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void prepareEventList(ArrayList<Event> events){
        events.add(new Event(1L, "Vencanje", "21.04.2024.", "14:30","23:30", "reserved"));
        events.add(new Event(1L, "Rodjendan", "22.04.2024.", "14:30","23:30", "occupied"));
        events.add(new Event(1L, "Koncert", "23.04.2024.", "14:30","23:30", "reserved"));
        events.add(new Event(1L, "Baby shower", "23.04.2024.", "14:30","23:30", "occupied"));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}