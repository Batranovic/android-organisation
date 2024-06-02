package com.example.projekatmobilneaplikacije.fragments;

import static android.service.controls.ControlsProviderService.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.PageEventListBinding;
import com.example.projekatmobilneaplikacije.databinding.PageGuestListBinding;
import com.example.projekatmobilneaplikacije.model.CreateEvent;
import com.example.projekatmobilneaplikacije.model.GuestList;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class GuestPageFragment extends Fragment {

    private PageGuestListBinding binding;
    public ArrayList<GuestList> guests = new ArrayList<>();
    public static GuestPageFragment newInstance(){
        return new GuestPageFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PageGuestListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("guestList")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        guests.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            GuestList guestList = document.toObject(GuestList.class);
                            guests.add(guestList);
                        }
                        FragmentTransition.to(GuestListFragment.newInstance(guests), getActivity(),
                                false, R.id.scroll_guests_list);
                    } else {
                        Log.d(TAG, "Error getting documents", task.getException());
                    }
                });


        FragmentTransition.to(GuestListFragment.newInstance(guests), getActivity(),
                false, R.id.scroll_guests_list);


        //agenda
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




