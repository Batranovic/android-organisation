package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeProfileActivity;
import com.example.projekatmobilneaplikacije.fragments.CreateBundleThirdFragment;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Event;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.example.projekatmobilneaplikacije.model.WorkCalendar;
import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class ReservationListAdapter extends ArrayAdapter<Reservation> implements Filterable{
    private ArrayList<Reservation> aReservations;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context mContext;

    Button acceptButton;

    private String userRole;
    private String username;

    FirebaseAuth auth;
    FirebaseUser user;

    public ReservationListAdapter(Context context, ArrayList<Reservation> reservations){
        super(context, R.layout.reservation_card, reservations);
        aReservations = reservations;
        mContext = context;
    }

    /*
     * Ova metoda vraca ukupan broj elemenata u listi koje treba prikazati
     * */
    @Override
    public int getCount() {
        return aReservations.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Reservation getItem(int position) {
        return aReservations.get(position);
    }

    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra da bude identifikator.
     * Naravno mozemo iskoristiti i jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reservation reservation = getItem(position);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_card,
                    parent, false);
        }
        ConstraintLayout reservationCard = convertView.findViewById(R.id.reservation_card_item);
        TextView serviceTitle = convertView.findViewById(R.id.service_title);
        TextView status = convertView.findViewById(R.id.reservation_status);
        TextView start = convertView.findViewById(R.id.reservation_start);


        Button acceptButton = convertView.findViewById(R.id.accept_reservation_button);

        Button cancelButton = convertView.findViewById(R.id.cancel_reservation_button);

        if (user != null) {
            username = user.getEmail();
            if (username == null) {
                Log.e("ReservationListAdapter", "Username is null");
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
                                        if(userRole.equals("EventOrganizer")){
                                            acceptButton.setVisibility(View.GONE);
                                        }
                                        Log.d("ReservationListAdapter", "User role: " + userRole);
                                        return;
                                    }
                                }
                            } else {
                                Log.d("ReservationListAdapter", "User document not found for username: " + username);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ReservationListAdapter", "Error fetching user role", e);
                        }
                    });
        } else {
            Log.e("ReservationListAdapter", "User not logged in");
        }

        //Button cancelButton = convertView.findViewById(R.id.cancel_reservation_button);


        if(reservation != null){
            serviceTitle.setText(reservation.getService().getTitle());
            status.setText(reservation.getStatus().name());
            Date fromDate = reservation.getFrom();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.");
            String fromDateString = dateFormat.format(fromDate);
            start.setText(fromDateString);
            reservationCard.setOnClickListener(v -> {
                Log.i("App", "Clicked: " +
                        reservation.getService().getTitle());
                Toast.makeText(getContext(), "Clicked: " + reservation.getService().getTitle(), Toast.LENGTH_SHORT).show();
            });

            if ("EventOrganizer".equals(userRole)) {
                acceptButton.setVisibility(View.GONE);
            } else {
                acceptButton.setVisibility(View.VISIBLE);
            }
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Owner".equals(userRole) || "Employee".equals(userRole)) {
                    if (reservation.getStatus() == ReservationStatus.New) {
                        updateReservationStatus(reservation, ReservationStatus.Accepted);
                    } else {
                        Toast.makeText(getContext(), "This reservation has already been accepted.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "You are not authorized to accept reservations.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("EventOrganizer".equals(userRole)) {
                    ReservationStatus status = reservation.getStatus();
                    Date startDate = reservation.getFrom();
                    int cancellationDeadline = reservation.getService().getCancellationDeadline();

                    Calendar deadlineCalendar = Calendar.getInstance();
                    deadlineCalendar.setTime(startDate);
                    deadlineCalendar.add(Calendar.DAY_OF_YEAR, -cancellationDeadline);

                    Date cancellationDeadlineDate = deadlineCalendar.getTime();
                    Date currentDate = new Date();

                    if ((status == ReservationStatus.New || status == ReservationStatus.Accepted) && currentDate.before(cancellationDeadlineDate)) {
                        updateReservationStatus(reservation, ReservationStatus.CancelledByEO);
                    } else {
                        Toast.makeText(getContext(), "Not allowed. Cancellation deadline has passed.", Toast.LENGTH_SHORT).show();
                    }
                }
                if (("Employee".equals(userRole) || "Owner".equals(userRole)) && (reservation.getStatus().equals(ReservationStatus.Accepted) || reservation.getStatus().equals(ReservationStatus.New))) {
                    updateReservationStatus(reservation, ReservationStatus.CancelledByPUP);
                } else {
                    Toast.makeText(getContext(), "Not allowed.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return convertView;
    }

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();
//                ArrayList<Reservation> filteredReservations = new ArrayList<>();
//
//                if (constraint == null || constraint.length() == 0) {
//                    // No filter implemented, return all products
//                    for (Reservation reservation : aReservations) {
//
//                        filteredReservations.add(reservation);
//
//                    }
//                } else {
//                    String filterPattern = constraint.toString().toLowerCase().trim();
//                    if(userRole.equals("Owner") || userRole.equals("Employee")){
//                        for (Reservation reservation : aReservations) {
//                            if (reservation.getEventOrganizer().getName().contains(filterPattern) && reservation.getEventOrganizer().getSurname().contains(filterPattern)) {
//                                filteredReservations.add(reservation);
//                            }else if(reservation.getService().getTitle().contains(filterPattern)){
//                                filteredReservations.add(reservation);
//                            }
//                        }
//                    }if(userRole.equals("Owner")){
//                        for (Reservation reservation : aReservations) {
//                            if(reservation.getEmployee().getName().contains(filterPattern) && reservation.getEmployee().getSurname().contains(filterPattern)){
//                                filteredReservations.add(reservation);
//                            }
//                        }
//                    }
//
//                }
//
//                results.values = filteredReservations;
//                results.count = filteredReservations.size();
//                return results;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                clear();
//                addAll((ArrayList<Reservation>) results.values);
//                notifyDataSetChanged();
//            }
//        };
//    }

    public void filterByStatus(String status) {
        ArrayList<Reservation> filteredReservations = new ArrayList<>();
        for(Reservation reservation : aReservations) {
            if(reservation.getStatus().name().equalsIgnoreCase(status.trim())) {
                filteredReservations.add(reservation);
            }
        }

        if(filteredReservations.isEmpty()) {
            Log.d("ReservationListAdapter", "No reservations found for status: " + status);
        } else {
            Log.d("ReservationListAdapter", "Filtered reservations for status " + status + ": " + filteredReservations.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredReservations); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

    public void updateReservations(ArrayList<Reservation> newReservations) {
        this.aReservations.clear();
        this.aReservations.addAll(newReservations);
        notifyDataSetChanged();
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
                                            Toast.makeText(getContext(), "Reservation status updated successfully.", Toast.LENGTH_SHORT).show();
                                            if (newStatus == ReservationStatus.CancelledByEO) {
                                                removeEventFromEmployeeWorkCalendar(reservation);
                                            } else {
                                                addEventToEmployeeWorkCalendar(reservation);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ReservationListAdapter", "Error updating reservation status", e);
                                            Toast.makeText(getContext(), "Failed to update reservation status.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ReservationListAdapter", "Error fetching reservation document", e);
                        Toast.makeText(getContext(), "Failed to fetch reservation document.", Toast.LENGTH_SHORT).show();
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
                                                        Log.d("ReservationListAdapter", "Event removed from work calendar.");
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
