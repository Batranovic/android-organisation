package com.example.projekatmobilneaplikacije.fragments.reservations;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.CreateProductActivity;
import com.example.projekatmobilneaplikacije.adapters.EmployeeListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ReservationListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentEmployeesListBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentProductListingBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentReservationListBinding;
import com.example.projekatmobilneaplikacije.fragments.ProductListingFragment;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.example.projekatmobilneaplikacije.model.Service;
import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationListFragment extends ListFragment implements SearchView.OnQueryTextListener {

    private static final String ARG_PARAM = "param";
    private ReservationListAdapter adapter;
    public static ArrayList<Reservation> reservations;
    private FragmentReservationListBinding binding;

    private ArrayList<Reservation> originalReservations = new ArrayList<>();



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reservationsRef = db.collection("reservations");

    private String userRole;
    private String username;

    FirebaseAuth auth;
    FirebaseUser user;

    SearchView searchView;

    ListView listView;

    Button acceptButton;
    public ReservationListFragment() {
    }

    public static ReservationListFragment newInstance(ArrayList<Reservation> reservations) {
        ReservationListFragment fragment = new ReservationListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, reservations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("App", "onCreate Reservations List Fragment");
        if (reservations == null) {
            reservations = new ArrayList<>();
        }
        if (getArguments() != null) {
            ArrayList<Reservation> passedReservations = getArguments().getParcelableArrayList(ARG_PARAM);
            if (passedReservations != null) {
                reservations.addAll(passedReservations);
            }
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            username = user.getEmail();
            Log.d("ReservationListFragment", "Retrieved username: " + username);
            fetchUserRole(); // Fetch the user role
        } else {
            Log.e("ReservationListFragment", "User not logged in");
        }


        adapter = new ReservationListAdapter(getActivity(), reservations);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("App", "onCreateView  Reservations List Fragment");
        return inflater.inflate(R.layout.fragment_reservation_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ReservationListAdapter(getActivity(), reservations);
        setListAdapter(adapter);

        listView = view.findViewById(android.R.id.list);
        searchView = view.findViewById(R.id.reservation_search);
        acceptButton = view.findViewById(R.id.accept_reservation_button);



        Button btnFilters = view.findViewById(R.id.btnFilters);
        btnFilters.setOnClickListener(v -> {
            Log.i("ProjekatMobilneAplikacije", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_reservations_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();


            RadioGroup statusRadioGroup = dialogView.findViewById(R.id.reservations_radio_group);
            statusRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton radioButton = dialogView.findViewById(checkedId);
                String selectedStatus = radioButton.getText().toString();

                // Primena izabrane kategorije na listu proizvoda
                adapter.filterByStatus(selectedStatus);
            });

        });

//        reservationsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                reservations.clear();
//
//                // Check if there are any documents in the collection
//                if (!queryDocumentSnapshots.isEmpty()) {
//                    // Iterate through each document in the collection
//                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        // Convert each document to a Product object
//                        Reservation reservation = documentSnapshot.toObject(Reservation.class);
//
//                        reservations.add(reservation);
//                    }
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Log.d("ReservationListFragment", "No reservations found");
//                }
//            }
//        });

        Button removeFiltersButton = view.findViewById(R.id.removeFiltersButton);
        removeFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshReservationList();
            }
        });

        listView.setAdapter(adapter);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("ReservationsListFragment", "New text: " + newText);
        ArrayList<Reservation> filteredReservations = new ArrayList<>();

        if (newText.isEmpty()) {
            filteredReservations.addAll(originalReservations); // Return original list if search text is empty
        } else {
            String query = newText.toLowerCase();

            for (Reservation reservation : reservations) {
                boolean match = false;

                //  "Owner" i "Employee"
                if (userRole.equals("Owner") || userRole.equals("Employee")) {
                    if ((reservation.getEventOrganizer().getName().toLowerCase() + " " + reservation.getEventOrganizer().getSurname().toLowerCase()).contains(query) ||
                            reservation.getService().getTitle().toLowerCase().contains(query)) {
                        match = true;
                    }
                }

                // "Owner"
                if (userRole.equals("Owner")) {
                    if (reservation.getEmployee().getCommonName().contains(query)){
                        match = true;
                    }
                }

                // Ako je pronađen meč, dodajte rezervaciju u filtriranu listu
                if (match) {
                    filteredReservations.add(reservation);
                }
            }
        }

        adapter.updateReservations(filteredReservations);
        return false;
    }



    public void refreshReservationList() {
            loadReservations();
        }

    private void fetchUserRole() {
        if (username == null) {
            Log.e("ReservationListFragment", "Username is null");
            return;
        }

        // Use a query to find the document with the specified username field
        db.collection("userDetails")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    userRole = documentSnapshot.getString("role");
                                    Log.d("ReservationListFragment", "User role: " + userRole);
                                    if ("EventOrganizer".equals(userRole)) {
                                        if (searchView != null) {
                                            searchView.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                    loadReservations(); // Load reservations after fetching the role
                                    return;
                                }
                            }
                        } else {
                            Log.d("ReservationListFragment", "User document not found for username: " + username);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ReservationListFragment", "Error fetching user role", e);
                    }
                });
    }

    private boolean shouldIncludeReservation(Reservation reservation) {
        switch (userRole) {
            case "Owner":
                return true;
            case "Employee":
                return reservation.getEmployee() != null && reservation.getEmployee().getEmail().equals(username);
            case "EventOrganizer":
                return reservation.getEventOrganizer() != null && reservation.getEventOrganizer().getUsername().equals(username);
            default:
                return false;
        }
    }

    private void loadReservations() {
        if (userRole == null) {
            Log.e("ReservationListFragment", "User role is null");
            return;
        }

        reservationsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                reservations.clear();
                originalReservations.clear();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Reservation reservation = documentSnapshot.toObject(Reservation.class);

                        if (shouldIncludeReservation(reservation)) {
                            reservations.add(reservation);
                            originalReservations.add(reservation);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("ReservationListFragment", "No reservations found");
                }
            }
        });
    }

}