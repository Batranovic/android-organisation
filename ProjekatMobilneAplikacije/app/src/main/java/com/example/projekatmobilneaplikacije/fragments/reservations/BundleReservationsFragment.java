package com.example.projekatmobilneaplikacije.fragments.reservations;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.BundleReservationListAdapter;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BundleReservationsFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private BundleReservationListAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reservationsRef = db.collection("reservations");
    ExpandableListView expandableListView;
    SearchView searchView;
    private String userRole;
    private String username;
    FirebaseAuth auth;
    FirebaseUser user;
    public static ArrayList<Reservation> reservations;
    private ArrayList<Reservation> originalReservations = new ArrayList<>();
    private List<String> bundleNames = new ArrayList<>();
    private HashMap<String, List<Reservation>> reservationsByBundle = new HashMap<>();

    public BundleReservationsFragment() {
        // Required empty public constructor
    }

    public static BundleReservationsFragment newInstance(ArrayList<Reservation> reservations) {
        BundleReservationsFragment fragment = new BundleReservationsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, reservations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("App", "onCreate Bundle Reservations Fragment");
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
            Log.d("BundleReservationsFragment", "Retrieved username: " + username);
            fetchUserRole(); // Fetch the user role
        } else {
            Log.e("BundleReservationsFragment", "User not logged in");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("App", "onCreateView Bundle Reservations Fragment");
        return inflater.inflate(R.layout.fragment_bundle_reservations, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandableListView = view.findViewById(R.id.bundle_reservations);
        searchView = view.findViewById(R.id.reservation_search);

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

        Button removeFiltersButton = view.findViewById(R.id.removeFiltersButton);
        removeFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshReservationList();
            }
        });
    }

    private void fetchUserRole() {
        if (username == null) {
            Log.e("BundleReservationsFragment", "Username is null");
            return;
        }

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
                                    Log.d("BundleReservationsFragment", "User role: " + userRole);
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
                            Log.d("BundleReservationsFragment", "User document not found for username: " + username);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("BundleReservationsFragment", "Error fetching user role", e);
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
            Log.e("BundleReservationsFragment", "User role is null");
            return;
        }

        reservationsRef.whereNotEqualTo("bundle", null).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Reservation> bundleReservations = new ArrayList<>();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Reservation reservation = documentSnapshot.toObject(Reservation.class);

                        if (shouldIncludeReservation(reservation)) {
                            bundleReservations.add(reservation);
                        }
                    }

                    Log.d("BundleReservationsFragment", "Fetched reservations: " + bundleReservations.size());

                    for (Reservation reservation : bundleReservations) {
                        String bundleName = reservation.getBundle().getTitle();
                        List<Reservation> bundleRes = reservationsByBundle.get(bundleName);
                        if (bundleRes == null) {
                            bundleRes = new ArrayList<>();
                            reservationsByBundle.put(bundleName, bundleRes);
                        }
                        bundleRes.add(reservation);
                    }

                    adapter = new BundleReservationListAdapter(getActivity(), new ArrayList<>(reservationsByBundle.keySet()), reservationsByBundle);
                    expandableListView.setAdapter(adapter);
                } else {
                    Log.d("BundleReservationsFragment", "No reservations found for bundles");
                }
            }
        });
    }

    private void refreshReservationList() {
        loadReservations();
    }

}
