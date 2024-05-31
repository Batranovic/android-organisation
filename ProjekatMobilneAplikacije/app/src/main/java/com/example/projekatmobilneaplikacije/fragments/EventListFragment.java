package com.example.projekatmobilneaplikacije.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.ListFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.agenda.AddAgendaActivity;
import com.example.projekatmobilneaplikacije.adapters.EventListAdapter;
import com.example.projekatmobilneaplikacije.databinding.ListCreatedEventsBinding;
import com.example.projekatmobilneaplikacije.model.CreateEvent;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends ListFragment {
    private ListCreatedEventsBinding binding;
    private EventListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<CreateEvent> mEvents;
    public static EventListFragment newInstance(ArrayList<CreateEvent> events){
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, events);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ListCreatedEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
     /*   Button addAgendaButton = root.findViewById(R.id.btn_add_agenda);
        if (addAgendaButton != null) {
            addAgendaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(requireActivity(), AddAgendaActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            // Handle the case where the button is null
            Log.e("EventPageFragment", "Button is null");
        } */

        return root;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEvents = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new EventListAdapter(getActivity(), mEvents);
            setListAdapter(adapter);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

