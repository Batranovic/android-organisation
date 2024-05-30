package com.example.projekatmobilneaplikacije.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.ReportListAdapter;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Report;
import com.example.projekatmobilneaplikacije.model.enumerations.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportListFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ReportListAdapter adapter;

    public static ArrayList<Report> reports = new ArrayList<Report>();

    ListView listView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reportsRef = db.collection("reports");

    public ReportListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportLitsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportListFragment newInstance(String param1, String param2) {
        ReportListFragment fragment = new ReportListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, reports);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reports = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new ReportListAdapter(getActivity(), reports);
            setListAdapter(adapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //prepareProductList(products);

        adapter = new ReportListAdapter(getActivity(), reports);
        // Set adapter for the ListFragment
        setListAdapter(adapter);

        listView = view.findViewById(android.R.id.list);

        reportsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                reports.clear();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Report report = documentSnapshot.toObject(Report.class);
                        if (report.getStatus() == Status.Pending){
                            reports.add(report);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("ReportListFragment", "No reports found");
                }
            }
        });

        listView.setAdapter(adapter);

    }
}