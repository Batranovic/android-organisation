package com.example.projekatmobilneaplikacije.fragments;

import static com.example.projekatmobilneaplikacije.activities.PriceListActivity.ARG_PARAM;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.projekatmobilneaplikacije.adapters.EventListAdapter;
import com.example.projekatmobilneaplikacije.adapters.GuestListAdapter;
import com.example.projekatmobilneaplikacije.databinding.ListCreatedEventsBinding;
import com.example.projekatmobilneaplikacije.databinding.LlstGuestsBinding;
import com.example.projekatmobilneaplikacije.model.CreateEvent;
import com.example.projekatmobilneaplikacije.model.GuestList;

import java.util.ArrayList;

public class GuestListFragment extends ListFragment {
    private  LlstGuestsBinding binding;
    private GuestListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<GuestList> mGuests;
    public static GuestListFragment newInstance(ArrayList<GuestList> guests){
        GuestListFragment fragment = new GuestListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, guests);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LlstGuestsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGuests = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new GuestListAdapter(getActivity(), mGuests);
            setListAdapter(adapter);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


