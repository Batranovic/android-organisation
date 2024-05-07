package com.example.projekatmobilneaplikacije.fragments;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.projekatmobilneaplikacije.R;

import com.example.projekatmobilneaplikacije.databinding.FragmentServiceAndProductPageBinding;
import com.example.projekatmobilneaplikacije.databinding.ListCreatedEventsBinding;

import com.example.projekatmobilneaplikacije.databinding.PageEventListBinding;
import com.example.projekatmobilneaplikacije.model.CreateEvent;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("createEvents")
                        .get()
                        .addOnCompleteListener(task-> {
                            if(task.isSuccessful()){
                                events.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    CreateEvent createEvent = document.toObject(CreateEvent.class);
                                    events.add(createEvent);
                                }
                                FragmentTransition.to(EventListFragment.newInstance(events), getActivity(),
                                        false, R.id.scroll_events_list);
                            }
                            else{
                                Log.d(TAG,"Error getting documents", task.getException());
                            }
                        });


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

}