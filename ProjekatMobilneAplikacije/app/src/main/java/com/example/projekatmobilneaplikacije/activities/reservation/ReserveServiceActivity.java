package com.example.projekatmobilneaplikacije.activities.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ResourceCursorAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.EditSubcategoryActivity;
import com.example.projekatmobilneaplikacije.activities.HomeActivity;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.Company;
import com.example.projekatmobilneaplikacije.model.DayWorkingHours;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Event;
import com.example.projekatmobilneaplikacije.model.RegistrationRequest;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.example.projekatmobilneaplikacije.model.Service;
import com.example.projekatmobilneaplikacije.model.UserDetails;
import com.example.projekatmobilneaplikacije.model.WorkingHours;
import com.example.projekatmobilneaplikacije.model.enumerations.Owner;
import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;
import com.example.projekatmobilneaplikacije.model.enumerations.UserRole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReserveServiceActivity extends AppCompatActivity {
    private Spinner eventSpinner;
    private Spinner employeeSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private FirebaseFirestore db;
    private TimePicker from, to;
    FirebaseAuth auth;
    String serviceId;
    FirebaseUser eventOrganizer;
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
                int mondayHour = from.getHour();
                int mondayMinute = from.getMinute();
                Date from = new Date(mondayHour, mondayMinute, 0);
                int mondayEndHour = to.getHour();
                int mondayEndMinute = to.getMinute();
                Date to = new Date(mondayEndHour, mondayEndMinute, 0);
                String selectedEvent = eventSpinner.getSelectedItem().toString();
                String selectedEmployee = employeeSpinner.getSelectedItem().toString();


                db.collection("services")
                        .whereEqualTo("id", serviceId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot categorySnapshot : queryDocumentSnapshots.getDocuments()) {
                                    Service service = categorySnapshot.toObject(Service.class);
                                    findEmployee(selectedEmployee, selectedEvent, service, from,to);
                                    return;
                                }
                            } else {

                                Toast.makeText(ReserveServiceActivity.this, "serviceid not found", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ReserveServiceActivity.this, "Failed to search employee", Toast.LENGTH_SHORT).show();
                        });

            }
        });
    }
private void findEmployee(String selectedEmployee, String selectedEvent, Service service, Date from, Date to){
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("employees")
            .whereEqualTo("name", selectedEmployee)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot categorySnapshot : queryDocumentSnapshots.getDocuments()) {
                        Employee selectedEmployees = categorySnapshot.toObject(Employee.class);
                        findEvent(selectedEmployees, selectedEvent,service, from,to );
                        return;
                    }
                } else {

                    Toast.makeText(ReserveServiceActivity.this, "employee not found", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(ReserveServiceActivity.this, "Failed to search employee", Toast.LENGTH_SHORT).show();
            });
}

private void findEvent(Employee selectedEmployee, String selectedEvent, Service service, Date from, Date to){
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("events")
            .whereEqualTo("name", selectedEvent)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot categorySnapshot : queryDocumentSnapshots.getDocuments()) {
                        Event event = categorySnapshot.toObject(Event.class);
                        createReservation(selectedEmployee, event, service, from,to );
                        return;
                    }
                } else {

                    Toast.makeText(ReserveServiceActivity.this, "events not found", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(ReserveServiceActivity.this, "Failed to search events", Toast.LENGTH_SHORT).show();
            });
}

    private void createReservation(Employee employee, Event event, Service service, Date from, Date to){
        auth = FirebaseAuth.getInstance();
        eventOrganizer = auth.getCurrentUser();
        db.collection("userDetails")
                .whereEqualTo("username", eventOrganizer.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Ako postoji rezultat, preuzmite prvi dokument (trebalo bi da bude samo jedan)
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            // Preuzmite UserDetails iz dokumenta
                            UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);

                            // Kreiranje objekta Reservation
                            Reservation reservation = new Reservation("1",  employee, userDetails, ReservationStatus.New, service, null, from, to);

                            // Dodavanje reservation objekta u Firestore
                            db.collection("reservations")
                                    .add(reservation)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(ReserveServiceActivity.this, "Reservation created successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ReserveServiceActivity.this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    private void populateEventSpinner() {
        // Dohvatanje kolekcije "events" iz Firestore baze
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> eventNames = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Dohvatanje imena događaja iz dokumenata
                                String eventName = document.getString("name");
                                eventNames.add(eventName);
                            }

                            // Kreiranje adaptera za Spinner i postavljanje imena događaja
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ReserveServiceActivity.this, android.R.layout.simple_spinner_item, eventNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            eventSpinner.setAdapter(adapter);
                        } else {
                            Log.d("ReserveServiceActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void populateEmployeeSpinner() {
        // Dohvatanje kolekcije "events" iz Firestore baze
        db.collection("employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> employeeNames = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Dohvatanje imena događaja iz dokumenata
                                String employeeName = document.getString("name");
                                employeeNames.add(employeeName);
                            }

                            // Kreiranje adaptera za Spinner i postavljanje imena događaja
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ReserveServiceActivity.this, android.R.layout.simple_spinner_item, employeeNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            employeeSpinner.setAdapter(adapter);
                        } else {
                            Log.d("ReserveServiceActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}