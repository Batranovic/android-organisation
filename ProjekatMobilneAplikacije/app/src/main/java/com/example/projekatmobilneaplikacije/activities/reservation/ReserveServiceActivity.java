package com.example.projekatmobilneaplikacije.activities.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Event;
import com.example.projekatmobilneaplikacije.model.EventOrganization;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.example.projekatmobilneaplikacije.model.Service;
import com.example.projekatmobilneaplikacije.model.UserDetails;
import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReserveServiceActivity extends AppCompatActivity {
    private Spinner eventSpinner;
    private Spinner employeeSpinner;
    private FirebaseFirestore db;
    private TimePicker from, to;
    private FirebaseAuth auth;
    private String serviceId;
    private FirebaseUser eventOrganizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            serviceId = intent.getStringExtra("serviceId");
        }

        setContentView(R.layout.activity_reserve_service);
        eventSpinner = findViewById(R.id.eventSpinner);
        employeeSpinner = findViewById(R.id.employeeSpinner);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);

        populateEventSpinner();
        populateEmployeeSpinner();

        Button reserve = findViewById(R.id.reserveButton);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, from.getHour());
                calendar.set(Calendar.MINUTE, from.getMinute());
                Date fromDate = calendar.getTime();

                calendar.set(Calendar.HOUR_OF_DAY, to.getHour());
                calendar.set(Calendar.MINUTE, to.getMinute());
                Date toDate = calendar.getTime();

                String selectedEvent = eventSpinner.getSelectedItem().toString();
                String selectedEmployee = employeeSpinner.getSelectedItem().toString();

                db.collection("services")
                        .whereEqualTo("id", serviceId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot serviceSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    Service service = serviceSnapshot.toObject(Service.class);
                                    findEmployee(selectedEmployee, selectedEvent, service, fromDate, toDate);
                                    return;
                                }
                            } else {
                                Toast.makeText(ReserveServiceActivity.this, "Service ID not found", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ReserveServiceActivity.this, "Failed to search for service", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    private void findEmployee(String selectedEmployee, String selectedEvent, Service service, Date from, Date to) {
        db.collection("employees")
                .whereEqualTo("name", selectedEmployee)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot employeeSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Employee employee = employeeSnapshot.toObject(Employee.class);
                            findEvent(employee, selectedEvent, service, from, to);
                            return;
                        }
                    } else {
                        Toast.makeText(ReserveServiceActivity.this, "Employee not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ReserveServiceActivity.this, "Failed to search for employee", Toast.LENGTH_SHORT).show();
                });
    }

    private void findEvent(Employee employee, String selectedEvent, Service service, Date from, Date to) {
        db.collection("eventOrganizations")
                .whereEqualTo("name", selectedEvent)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot eventSnapshot : queryDocumentSnapshots.getDocuments()) {
                            EventOrganization event = eventSnapshot.toObject(EventOrganization.class);
                            createReservation(employee, event, service, from, to);
                            return;
                        }
                    } else {
                        Toast.makeText(ReserveServiceActivity.this, "Event not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ReserveServiceActivity.this, "Failed to search for event", Toast.LENGTH_SHORT).show();
                });
    }

    private void createReservation(Employee employee, EventOrganization event, Service service, Date from, Date to) {
        auth = FirebaseAuth.getInstance();
        eventOrganizer = auth.getCurrentUser();
        db.collection("userDetails")
                .whereEqualTo("username", eventOrganizer.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                            String reservationId = UUID.randomUUID().toString();
                            if(service.getConfirmationMode().equals("Automatic")){
                                Reservation reservation = new Reservation(reservationId, employee, userDetails, ReservationStatus.Accepted, service, null, from, to, event);
                                db.collection("reservations")
                                        .add(reservation)
                                        .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(ReserveServiceActivity.this, "Reservation created successfully", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(ReserveServiceActivity.this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
                                        });
                            }else {
                                Reservation reservation = new Reservation("1", employee, userDetails, ReservationStatus.New, service, null, from, to, event);
                                db.collection("reservations")
                                        .add(reservation)
                                        .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(ReserveServiceActivity.this, "Reservation created successfully", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(ReserveServiceActivity.this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
                                        });
                            }

                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    private void populateEventSpinner() {
        db.collection("eventOrganizations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> eventNames = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String eventName = document.getString("name");
                            eventNames.add(eventName);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ReserveServiceActivity.this, android.R.layout.simple_spinner_item, eventNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        eventSpinner.setAdapter(adapter);
                    } else {
                        Log.d("ReserveServiceActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void populateEmployeeSpinner() {
        db.collection("employees")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> employeeNames = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String employeeName = document.getString("name");
                            employeeNames.add(employeeName);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ReserveServiceActivity.this, android.R.layout.simple_spinner_item, employeeNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        employeeSpinner.setAdapter(adapter);
                    } else {
                        Log.d("ReserveServiceActivity", "Error getting documents: ", task.getException());
                    }
                });
    }
}
