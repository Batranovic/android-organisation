package com.example.projekatmobilneaplikacije.fragments.registration;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.CreateProductActivity;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.RegistrationRequestsListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentRegistrationRequestsBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.fragments.serviceAndProduct.ServiceAndProductListFragment;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.Company;
import com.example.projekatmobilneaplikacije.model.DayWorkingHours;
import com.example.projekatmobilneaplikacije.model.EventType;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.RegistrationRequest;
import com.example.projekatmobilneaplikacije.model.WorkingHours;
import com.example.projekatmobilneaplikacije.model.enumerations.Owner;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationRequestsFragment extends ListFragment {

    private FragmentRegistrationRequestsBinding binding;
    public static ArrayList<RegistrationRequest> registrationRequests = new ArrayList<>();
    private RegistrationRequestsListAdapter adapter;
    private static final String ARG_PARAM = "param";
    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference regRef = db.collection("registrationRequests");

    public static RegistrationRequestsFragment newInstance(ArrayList<RegistrationRequest> registrationRequests) {
        RegistrationRequestsFragment fragment = new RegistrationRequestsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, registrationRequests);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            registrationRequests = getArguments().getParcelableArrayList(ARG_PARAM);
        }

        adapter = new RegistrationRequestsListAdapter(getActivity(), registrationRequests);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration_requests, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new RegistrationRequestsListAdapter(getActivity(), registrationRequests);
        setListAdapter(adapter);

        Button btnFilters = view.findViewById(R.id.btnFilters);
        btnFilters.setOnClickListener(v -> {
            Log.i("ProjekatMobilneAplikacije", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter_registration_request, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();

            RadioGroup categoryRadioGroup = dialogView.findViewById(R.id.categoryRadioGroup);
            categoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton radioButton = dialogView.findViewById(checkedId);
                String selectedCategory = radioButton.getText().toString();
                adapter.filterByCategory(selectedCategory);
            });

            RadioGroup eventTypeRadioGroup = dialogView.findViewById(R.id.eventTypeRadioGroup);
            eventTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton radioButtonEventType = dialogView.findViewById(checkedId);
                String selectedEventType = radioButtonEventType.getText().toString();
                adapter.filterByEventType(selectedEventType);
            });

            EditText editTextMonth = dialogView.findViewById(R.id.editTextMonth);
            EditText editTextYear = dialogView.findViewById(R.id.editTextYear);

            Button btnApplyFilter = dialogView.findViewById(R.id.applyFilterButton);
            btnApplyFilter.setOnClickListener(vi -> {
                int month = Integer.parseInt(editTextMonth.getText().toString());
                int year = Integer.parseInt(editTextYear.getText().toString());
                adapter.filterByMonthAndYear(month, year);
                bottomSheetDialog.dismiss();
            });
        });

        listView = view.findViewById(android.R.id.list);

        db.collection("registrationRequests")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        registrationRequests.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            RegistrationRequest registrationRequest = document.toObject(RegistrationRequest.class);
                            registrationRequests.add(registrationRequest);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        Button removeFiltersButton = view.findViewById(R.id.removeFiltersButton);
        removeFiltersButton.setOnClickListener(v -> refreshRequestsList());

        listView.setAdapter(adapter);
    }

    public void refreshRequestsList() {
        regRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            registrationRequests.clear();
            if (!queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    RegistrationRequest registrationRequest = documentSnapshot.toObject(RegistrationRequest.class);
                    registrationRequests.add(registrationRequest);
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.d("RegistrationRequestFragment", "No registrationRequest found");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
