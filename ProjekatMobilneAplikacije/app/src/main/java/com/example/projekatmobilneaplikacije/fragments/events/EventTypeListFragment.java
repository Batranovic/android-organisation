package com.example.projekatmobilneaplikacije.fragments.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.EventTypeListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ServiceAndProductListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentEventTypeListBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentServiceAndProductListBinding;
import com.example.projekatmobilneaplikacije.fragments.serviceAndProduct.ServiceAndProductListFragment;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.EventType;

import java.util.ArrayList;


public class EventTypeListFragment extends ListFragment {
    private FragmentEventTypeListBinding binding;
    private EventTypeListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<EventType> mEventTypes;

    public static EventTypeListFragment newInstance(ArrayList<EventType> eventTypes){
        EventTypeListFragment fragment = new EventTypeListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, eventTypes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEventTypeListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEventTypes = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new EventTypeListAdapter(getActivity(), mEventTypes);
            setListAdapter(adapter);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
