package com.example.projekatmobilneaplikacije.fragments.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentEventTypePageBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentServiceAndProductPageBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.fragments.serviceAndProduct.ServiceAndProductListFragment;
import com.example.projekatmobilneaplikacije.fragments.serviceAndProduct.ServiceAndProductPageFragment;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.EventType;

import java.util.ArrayList;

public class EventTypePageFragment extends Fragment {
    private FragmentEventTypePageBinding binding;
    public static ArrayList<EventType> eventTypes = new ArrayList<EventType>();

    public static EventTypePageFragment newInstance() {
        return new EventTypePageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEventTypePageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Button createEventTypeButton = root.findViewById(R.id.create_event_type);
        createEventTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigacija na drugi fragment
                NavHostFragment.findNavController(EventTypePageFragment.this)
                        .navigate(R.id.action_nav_event_type_page_to_nav_event_type_create);
            }
        });



        FragmentTransition.to(EventTypeListFragment.newInstance(eventTypes), getActivity(),
                false, R.id.scroll_event_type_list);



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