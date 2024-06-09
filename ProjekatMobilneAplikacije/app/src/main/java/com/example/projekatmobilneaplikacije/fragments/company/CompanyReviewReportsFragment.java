package com.example.projekatmobilneaplikacije.fragments.company;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.CompanyCommentReportsAdapter;
import com.example.projekatmobilneaplikacije.adapters.CompanyCommentsAdapter;
import com.example.projekatmobilneaplikacije.model.CompanyReview;
import com.example.projekatmobilneaplikacije.model.CompanyReviewReport;
import com.example.projekatmobilneaplikacije.model.enumerations.CompanyReviewReportStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
 * Use the {@link CompanyReviewReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyReviewReportsFragment extends ListFragment {

    private static final String ARG_PARAM = "param";
    private CompanyCommentReportsAdapter adapter;
    public static ArrayList<CompanyReviewReport> reports;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reportsRef = db.collection("reports");

    private String userRole;
    private String username;

    FirebaseAuth auth;
    FirebaseUser user;

    ListView listView;

    public CompanyReviewReportsFragment() {
        // Required empty public constructor
    }

    public static CompanyReviewReportsFragment newInstance(ArrayList<CompanyReviewReport> reports) {
        CompanyReviewReportsFragment fragment = new CompanyReviewReportsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, reports);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("App", "onCreate Review Reports List Fragment");
        if (reports == null) {
            reports = new ArrayList<>();
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        adapter = new CompanyCommentReportsAdapter(getActivity(), reports);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_review_reports, container, false);


        loadReports();

        adapter = new CompanyCommentReportsAdapter(getActivity(), reports);
        setListAdapter(adapter);


        listView = view.findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        return view;
    }

    public void loadReports() {
        db.collection("companyReviewReports")
                .whereEqualTo("status", CompanyReviewReportStatus.Reported.toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<CompanyReviewReport> reportsList = new ArrayList<>();


                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            CompanyReviewReport report = document.toObject(CompanyReviewReport.class);
                            reportsList.add(report);
                        }

                        reports.clear();
                        reports.addAll(reportsList);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error loading reports", e);
                    }
                });
    }

}