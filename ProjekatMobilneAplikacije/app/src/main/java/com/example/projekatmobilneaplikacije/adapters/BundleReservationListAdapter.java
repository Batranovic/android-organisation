package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BundleReservationListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> bundleNames;

    private HashMap<String, List<Reservation>> reservationsByBundle;

    private String userRole;
    private String username;

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BundleReservationListAdapter(Context mContext, List<String> bundleNames, HashMap<String, List<Reservation>> reservationsByBundle) {
        this.mContext = mContext;
        this.bundleNames = bundleNames;
        this.reservationsByBundle = reservationsByBundle;
    }

    @Override
    public int getGroupCount() {
        return this.bundleNames.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.reservationsByBundle.get(this.bundleNames.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.bundleNames.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return reservationsByBundle.get(this.bundleNames.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String bundleName = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.reservation_group, null);
        }
        TextView bundleNameTextView = convertView.findViewById(R.id.bundle_name);
        bundleNameTextView.setText(bundleName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Reservation reservation = (Reservation) getChild(groupPosition, childPosition);
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(convertView == null){
            convertView = inflater.inflate(R.layout.reservation_card, null);
        }

        TextView reservationTitle = convertView.findViewById(R.id.service_title);
        TextView status = convertView.findViewById(R.id.reservation_status);
        TextView start = convertView.findViewById(R.id.reservation_start);
        Button acceptButton = convertView.findViewById(R.id.accept_reservation_button);
        Button cancelButton = convertView.findViewById(R.id.cancel_reservation_button);


        if (user != null) {
            username = user.getEmail();
            if (username == null) {
                Log.e("BundleReservationListAdapter", "Username is null");
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
                                        if(userRole.equals("EventOrganizer")){
                                            acceptButton.setVisibility(View.GONE);
                                        }
                                        Log.d("BundleReservationListAdapter", "User role: " + userRole);
                                        return;
                                    }
                                }
                            } else {
                                Log.d("BundleReservationListAdapter", "User document not found for username: " + username);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("BundleReservationListAdapter", "Error fetching user role", e);
                        }
                    });
        } else {
            Log.e("BundleReservationListAdapter", "User not logged in");
        }


        reservationTitle.setText(reservation.getService().getTitle());
        status.setText(reservation.getStatus().name());
        Date fromDate = reservation.getFrom();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String fromDateString = dateFormat.format(fromDate);
        start.setText(fromDateString);



        // Dodajte ostale informacije o rezervaciji koje želite prikazati

        return convertView;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
