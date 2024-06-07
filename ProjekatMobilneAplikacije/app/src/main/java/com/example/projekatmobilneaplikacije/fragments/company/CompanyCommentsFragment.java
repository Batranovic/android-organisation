package com.example.projekatmobilneaplikacije.fragments.company;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

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

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.CompanyCommentsAdapter;
import com.example.projekatmobilneaplikacije.adapters.ReservationListAdapter;
import com.example.projekatmobilneaplikacije.fragments.reservations.ReservationListFragment;
import com.example.projekatmobilneaplikacije.model.CompanyReview;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyCommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyCommentsFragment extends ListFragment {

    private static final String ARG_PARAM = "param";
    private CompanyCommentsAdapter adapter;
    public static ArrayList<CompanyReview> reviews;

    private ArrayList<CompanyReview> originalReviews = new ArrayList<>();



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reviewsRef = db.collection("comments");

    private String userRole;
    private String username;

    FirebaseAuth auth;
    FirebaseUser user;

    ListView listView;


    public CompanyCommentsFragment() {
        // Required empty public constructor
    }

    public static CompanyCommentsFragment newInstance(ArrayList<CompanyReview> reviews) {
        CompanyCommentsFragment fragment = new CompanyCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, reviews);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("App", "onCreate Reviews List Fragment");
        if (reviews == null) {
            reviews = new ArrayList<>();
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        adapter = new CompanyCommentsAdapter(getActivity(), reviews);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_comments, container, false);


        loadComments();

        Button btnFilters = view.findViewById(R.id.btnFilters);
        btnFilters.setOnClickListener(v -> {
            Log.i("ProjekatMobilneAplikacije", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_comments_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();


            EditText editTextDay = dialogView.findViewById(R.id.editTextDay);
            EditText editTextMonth = dialogView.findViewById(R.id.editTextMonth);
            EditText editTextYear = dialogView.findViewById(R.id.editTextYear);

            Button btnApplyFilter = dialogView.findViewById(R.id.applyFilterButton);
            btnApplyFilter.setOnClickListener(vi -> {
                // Proverite da li su polja prazna pre nego što ih konvertujete u integer
                int day = 0, month = 0, year = 0;
                if (!editTextDay.getText().toString().isEmpty()) {
                    day = Integer.parseInt(editTextDay.getText().toString());
                }
                if (!editTextMonth.getText().toString().isEmpty()) {
                    month = Integer.parseInt(editTextMonth.getText().toString());
                }
                if (!editTextYear.getText().toString().isEmpty()) {
                    year = Integer.parseInt(editTextYear.getText().toString());
                }

                adapter.filterByDate(day, month, year);
                bottomSheetDialog.dismiss();
            });

            Button removeFiltersButton = view.findViewById(R.id.removeFiltersButton);
            removeFiltersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshReviewList();
                }
            });

        });




        Button newCommentButton = view.findViewById(R.id.new_comment_button);
        newCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dodajte kod za navigaciju na drugi fragment ili bilo koju drugu akciju koja treba da se izvrši na klik dugmeta
                Navigation.findNavController(requireActivity(), R.id.company_comments_fragment)
                        .navigate(R.id.nav_new_company_comment);
            }
        });




        adapter = new CompanyCommentsAdapter(getActivity(), reviews);
        setListAdapter(adapter);


        listView = view.findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        return view;
    }


    public void refreshReviewList() {
        loadComments();
    }

    public void loadComments() {
        db.collection("comments")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Lista za čuvanje učitanih komentara
                        List<CompanyReview> commentsList = new ArrayList<>();

                        // Iterirajte kroz dokumente i konvertujte ih u objekte CompanyReview
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            CompanyReview comment = document.toObject(CompanyReview.class);
                            commentsList.add(comment);
                        }

                        // Ažurirajte listu komentara koju koristi adapter
                        reviews.clear();
                        reviews.addAll(commentsList);
                        adapter.notifyDataSetChanged(); // Obavestite adapter o promeni podataka
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error loading comments", e);
                        // Ukoliko dođe do greške pri učitavanju komentara, možete prikazati odgovarajuću poruku korisniku.
                    }
                });
    }



}


