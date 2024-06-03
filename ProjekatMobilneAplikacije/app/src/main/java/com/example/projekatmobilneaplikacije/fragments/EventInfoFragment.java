package com.example.projekatmobilneaplikacije.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.agenda.AddAgendaActivity;
import com.example.projekatmobilneaplikacije.activities.agenda.AddGuestListActivity;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeeInformationBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentEventInfoBinding;
import com.example.projekatmobilneaplikacije.fragments.employees.EmployeeInformationFragment;
import com.example.projekatmobilneaplikacije.model.CreateEvent;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class EventInfoFragment extends Fragment {
  FragmentEventInfoBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef = db.collection("createEvents");
    private String mParam1;
    private String mParam2;
    private TextView infoTextView;

    public EventInfoFragment() {
        // Required empty public constructor
    }
    public static EventInfoFragment newInstance(String param1, String param2) {
        EventInfoFragment fragment = new EventInfoFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentEventInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        infoTextView = root.findViewById(R.id.info_name);

        retrieveEventData();


        Button addAgendaButton = root.findViewById(R.id.btn_add_agenda);
        addAgendaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add Agenda button clicked");
                Intent intent = new Intent(requireActivity(), AddAgendaActivity.class);
                if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    Log.d(TAG, "Starting AddAgendaActivity");
                    startActivity(intent);
                } else {
                    Log.e(TAG, "AddAgendaActivity not found");
                }
            }
        });

       Button addGuestButton = root.findViewById(R.id.btn_add_guests_list);
        addGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), AddGuestListActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }


        private void retrieveEventData()
        {
            eventsRef.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                // Get the employee data
                                CreateEvent event = documentSnapshot.toObject(CreateEvent.class);
                                // Now you have the employee object, you can use it as needed
                                // For example, populate UI components with employee details
                                updateUI(event);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors
                            Log.e(TAG, "Error retrieving employee data: ", e);
                        }
                    });
        }

        private void updateUI(CreateEvent event) {
            // Update TextView with common name
            infoTextView.setText(event.getName());
            // You can similarly update other UI components with different employee information
        }

    }





