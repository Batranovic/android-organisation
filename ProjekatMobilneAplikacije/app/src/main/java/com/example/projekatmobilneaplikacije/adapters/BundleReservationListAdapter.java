package com.example.projekatmobilneaplikacije.adapters;


import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.Event;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.example.projekatmobilneaplikacije.model.WorkCalendar;
import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

    public BundleReservationListAdapter(Context context, List<String> bundleNames, HashMap<String, List<Reservation>> reservationsByBundle) {
        this.mContext = context;
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

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Employee".equals(userRole)) {
                    if (reservation.getStatus() == ReservationStatus.New) {
                        updateReservationStatus(reservation, ReservationStatus.WaitingToBeAccepted);
                    } else {
                        Toast.makeText(mContext, "This reservation has already been accepted.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "You are not authorized to accept reservations.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationStatus status = reservation.getStatus();
                Date startDate = reservation.getFrom();
                int cancellationDeadline = reservation.getBundle().getCancellationDeadline();

                Calendar deadlineCalendar = Calendar.getInstance();
                deadlineCalendar.setTime(startDate);
                deadlineCalendar.add(Calendar.DAY_OF_YEAR, -cancellationDeadline);

                Date cancellationDeadlineDate = deadlineCalendar.getTime();
                Date currentDate = new Date();
                if ("EventOrganizer".equals(userRole) && (status == ReservationStatus.New || status == ReservationStatus.Accepted) && currentDate.before(cancellationDeadlineDate)) {

                    updateReservationStatus(reservation, ReservationStatus.CancelledByEO);

                    cancelOtherReservationsForBundleEO(reservation.getBundle().getTitle());

                    String notificationId = db.collection("notifications").document().getId();
                    Date currentTimestamp = new Date();
                    Notification notification = new Notification(
                            notificationId,
                            "Bundle Reservations Cancellation",
                            "Event Organizer " + username + " cancelled reservations for bundle: " + reservation.getBundle().getTitle(),
                            false,
                            currentTimestamp,
                            reservation.getEmployee().getEmail()
                    );

                    db.collection("notifications").document(notificationId)
                            .set(notification)
                            .addOnSuccessListener(aVoid1 -> Toast.makeText(mContext, "Notification sent", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(mContext, "Error sending notification", Toast.LENGTH_SHORT).show());

                } else if (("Employee".equals(userRole) || "Owner".equals(userRole)) && (reservation.getStatus().equals(ReservationStatus.Accepted) || reservation.getStatus().equals(ReservationStatus.New)) && currentDate.before(cancellationDeadlineDate)) {

                    updateReservationStatus(reservation, ReservationStatus.CancelledByPUP);

                    cancelOtherReservationsForBundlePUP(reservation.getBundle().getTitle());

                    String notificationId = db.collection("notifications").document().getId();
                    Date currentTimestamp = new Date();
                    Notification notification = new Notification(
                            notificationId,
                            "Bundle Reservations Cancellation",
                            "PUP " + username + " cancelled reservations for bundle: " + reservation.getBundle().getTitle(),
                            false,
                            currentTimestamp,
                            reservation.getEventOrganizer().getUsername()
                    );

                    db.collection("notifications").document(notificationId)
                            .set(notification)
                            .addOnSuccessListener(aVoid1 -> Toast.makeText(mContext, "Notification sent", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(mContext, "Error sending notification", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(mContext, "Not allowed.", Toast.LENGTH_SHORT).show();


                }
            }
        });


        return convertView;
    }

    private void loadReservationsByBundle(String bundleTitle) {
        db.collection("reservations")
                .whereEqualTo("bundle.title", bundleTitle)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        boolean allWaiting = true;
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Reservation reservation = documentSnapshot.toObject(Reservation.class);
                            if (reservation != null && reservation.getStatus() != ReservationStatus.WaitingToBeAccepted) {
                                allWaiting = false;
                                break;
                            }
                        }
                        if (allWaiting) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                Reservation reservation = documentSnapshot.toObject(Reservation.class);
                                if (reservation != null) {
                                    reservation.setStatus(ReservationStatus.Accepted);
                                    updateReservationStatus(reservation, ReservationStatus.Accepted);
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("BundleReservationListAdapter", "Error loading reservations by bundle", e);
                    }
                });
    }


    private void cancelOtherReservationsForBundleEO(String bundleName) {
        List<Reservation> bundleReservations = reservationsByBundle.get(bundleName);
        if (bundleReservations != null) {
            for (Reservation res : bundleReservations) {
                if ((res.getStatus() == ReservationStatus.New || res.getStatus() == ReservationStatus.Accepted)) {
                    updateReservationStatus(res, ReservationStatus.CancelledByEO);
                }
            }
        }
    }

    private void cancelOtherReservationsForBundlePUP(String bundleName) {
        List<Reservation> bundleReservations = reservationsByBundle.get(bundleName);
        if (bundleReservations != null) {
            for (Reservation res : bundleReservations) {
                if ((res.getStatus() == ReservationStatus.New || res.getStatus() == ReservationStatus.Accepted)) {
                    updateReservationStatus(res, ReservationStatus.CancelledByPUP);
                }
            }
        }
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void filterByStatus(String status) {
        HashMap<String, List<Reservation>> filteredReservationsByBundle = new HashMap<>();
        List<String> filteredBundleNames = new ArrayList<>();

        for (Map.Entry<String, List<Reservation>> entry : reservationsByBundle.entrySet()) {
            String bundleName = entry.getKey();
            List<Reservation> bundleReservations = entry.getValue();
            if (bundleReservations != null && !bundleReservations.isEmpty()) {
                List<Reservation> filteredBundleReservations = new ArrayList<>();

                for (Reservation reservation : bundleReservations) {
                    if (reservation.getStatus().name().equalsIgnoreCase(status.trim())) {
                        filteredBundleReservations.add(reservation);
                    }
                }

                if (!filteredBundleReservations.isEmpty()) {
                    filteredReservationsByBundle.put(bundleName, filteredBundleReservations);
                    filteredBundleNames.add(bundleName);
                }
            }
        }

        if (filteredReservationsByBundle.isEmpty()) {
            Log.d("BundleReservationListAdapter", "No reservations found for status: " + status);
        } else {
            Log.d("BundleReservationListAdapter", "Filtered reservations for status " + status + ": " + filteredReservationsByBundle.size());
        }

        // Očisti postojeće podatke u adapteru
        reservationsByBundle.clear();
        bundleNames.clear();

        // Dodajte filtrirane rezervacije po bundle-u u adapter
        reservationsByBundle.putAll(filteredReservationsByBundle);
        bundleNames.addAll(filteredBundleNames);

        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    private void updateReservationStatus(Reservation reservation, ReservationStatus newStatus) {
        reservation.setStatus(newStatus);
        db.collection("reservations")
                .whereEqualTo("id", reservation.getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            // Pronađen je dokument rezervacije, ažurirajte status
                            db.collection("reservations").document(documentSnapshot.getId())
                                    .update("status", newStatus)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (newStatus == ReservationStatus.CancelledByEO || newStatus == ReservationStatus.CancelledByPUP) {
                                                removeEventFromEmployeeWorkCalendar(reservation);
                                            }else if(newStatus == ReservationStatus.Accepted) {
                                                addEventToEmployeeWorkCalendar(reservation);
                                           }else {
                                                loadReservationsByBundle(reservation.getBundle().getTitle());
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ReservationListAdapter", "Error updating reservation status", e);
                                            Toast.makeText(mContext, "Failed to update reservation status.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ReservationListAdapter", "Error fetching reservation document", e);
                        Toast.makeText(mContext, "Failed to fetch reservation document.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeEventFromEmployeeWorkCalendar(Reservation reservation) {
        String employeeEmail = reservation.getEmployee().getEmail();
        db.collection("workCalendars")
                .whereEqualTo("employee", employeeEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    WorkCalendar workCalendar = documentSnapshot.toObject(WorkCalendar.class);
                                    if (workCalendar != null) {
                                        Iterator<Event> iterator = workCalendar.getEvents().iterator();
                                        while (iterator.hasNext()) {
                                            Event event = iterator.next();
                                            if (event.getStartTime().equals(new Date(reservation.getFrom().getTime())) &&
                                                    event.getEndTime().equals(new Date(reservation.getTo().getTime()))) {
                                                iterator.remove();
                                                break;
                                            }
                                        }

                                        db.collection("workCalendars").document(documentSnapshot.getId())
                                                .set(workCalendar)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("BundleReservationListAdapter", "Event removed from work calendar.");
                                                        notifyDataSetChanged();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("BundleReservationListAdapter", "Error updating work calendar", e);
                                                    }
                                                });
                                    }
                                }
                            }
                        } else {
                            Log.d("BundleReservationListAdapter", "Work calendar not found for employee: " + employeeEmail);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("BundleReservationListAdapter", "Error fetching work calendar", e);
                    }
                });
    }

    private void addEventToEmployeeWorkCalendar(Reservation reservation) {
        String employeeEmail = reservation.getEmployee().getEmail();
        db.collection("workCalendars")
                .whereEqualTo("employee", employeeEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    WorkCalendar workCalendar = documentSnapshot.toObject(WorkCalendar.class);
                                    if (workCalendar != null) {
                                        Event newEvent = new Event(
                                                System.currentTimeMillis(), // Unique ID for the event
                                                new Date(reservation.getFrom().getTime()),
                                                new Date(reservation.getTo().getTime()),
                                                "reserved"
                                        );
                                        workCalendar.getEvents().add(newEvent);

                                        db.collection("workCalendars").document(documentSnapshot.getId())
                                                .set(workCalendar)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("ReservationListAdapter", "Event added to work calendar.");
                                                        notifyDataSetChanged();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("ReservationListAdapter", "Error updating work calendar", e);
                                                    }
                                                });
                                    }
                                }
                            }
                        } else {
                            Log.d("ReservationListAdapter", "Work calendar not found for employee: " + employeeEmail);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ReservationListAdapter", "Error fetching work calendar", e);
                    }
                });
    }




}
