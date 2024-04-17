package com.example.projekatmobilneaplikacije.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.projekatmobilneaplikacije.R;

import com.example.projekatmobilneaplikacije.databinding.FragmentServiceAndProductPageBinding;
import com.example.projekatmobilneaplikacije.databinding.ListCreatedEventsBinding;

import com.example.projekatmobilneaplikacije.databinding.PageEventListBinding;
import com.example.projekatmobilneaplikacije.model.CreateEvent;

import java.util.ArrayList;
import java.util.Calendar;

public class EventPageFragment extends Fragment {
    private PageEventListBinding binding;

    public static ArrayList<CreateEvent> events = new ArrayList<CreateEvent>();
    public static EventPageFragment newInstance() {
        return new EventPageFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PageEventListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prepareEventsList(events);

        //NavHostFragment.findNavController(EventPageFragment.this)
          //              .navigate(R.id.nav_action_events_list);

        FragmentTransition.to(EventListFragment.newInstance(events), getActivity(),
                false, R.id.scroll_events_list);


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
    private void prepareEventsList(ArrayList<CreateEvent> events){
        events.add(new CreateEvent(1L,"Wedding", "Wedding T and M", "Romantic wedding located in Italy, Como Lake with a lot of flower decorations...", 200, "Novi Sad, up to 50 km", "20.03.2024", true));
        events.add(new CreateEvent(2L, "Other", "Birthday Party", "///", 100, "Belgrade, up to 30 km", "15.05.2024", false));
    }
}