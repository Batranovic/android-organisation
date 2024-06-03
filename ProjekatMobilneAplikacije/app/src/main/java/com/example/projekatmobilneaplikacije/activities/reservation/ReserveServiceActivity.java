package com.example.projekatmobilneaplikacije.activities.reservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.WorkCalendarAdapter;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Event;
import com.example.projekatmobilneaplikacije.model.EventOrganization;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.example.projekatmobilneaplikacije.model.Service;
import com.example.projekatmobilneaplikacije.model.UserDetails;
import com.example.projekatmobilneaplikacije.model.WorkCalendar;
import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReserveServiceActivity extends AppCompatActivity {
    private Spinner eventSpinner;
    private Spinner employeeSpinner;
    private FirebaseFirestore db;
    private String from, to;
    private FirebaseAuth auth;
    private String serviceId, bundleId;
    private FirebaseUser eventOrganizer;

    private List<WorkCalendar> workCalendarList;
    private Service currentService;
    EditText fromTimeEditText,toTimeEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            serviceId = intent.getStringExtra("serviceId");
            bundleId = getIntent().getStringExtra("bundleId");
            Log.d("ReserveProductActivity", "Bundle ID: " + bundleId);
            Log.d("ReserveProductActivity", "Service ID: " + serviceId);
        }


        setContentView(R.layout.activity_reserve_service);
        eventSpinner = findViewById(R.id.eventSpinner);
        employeeSpinner = findViewById(R.id.employeeSpinner);
        fromTimeEditText = findViewById(R.id.fromTimeEditText);
        toTimeEditText = findViewById(R.id.toTimeEditText);

        from = fromTimeEditText.getText().toString();
        to = toTimeEditText.getText().toString();


        populateEventSpinner();
        populateEmployeeSpinner();

        fromTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                db.collection("services")
                        .whereEqualTo("id", serviceId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            validateTimeSlot(fromTimeEditText.getText().toString(), toTimeEditText.getText().toString());
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot serviceSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    Service service = serviceSnapshot.toObject(Service.class);
                                    currentService = service;
                                    if (currentService != null && currentService.getDuration() > 0) {
                                        String fromTimeText = s.toString();
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                        try {
                                            Date fromDate = sdf.parse(fromTimeText);
                                            Calendar cal = Calendar.getInstance();
                                            cal.setTime(fromDate);
                                            cal.add(Calendar.MINUTE, currentService.getDuration());
                                            Date toDate = cal.getTime();
                                            toTimeEditText.setText(sdf.format(toDate));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
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

        toTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                db.collection("services")
                        .whereEqualTo("id", serviceId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                validateTimeSlot(fromTimeEditText.getText().toString(), toTimeEditText.getText().toString());
                                for (DocumentSnapshot serviceSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    Service service = serviceSnapshot.toObject(Service.class);
                                    currentService = service;
                                    if (currentService != null && currentService.getEngagement() > 0) {
                                        String fromTimeText = fromTimeEditText.getText().toString();
                                        String toTimeText = s.toString();
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                        try {
                                            Date fromDate = sdf.parse(fromTimeText);
                                            Date toDate = sdf.parse(toTimeText);
                                            Calendar cal = Calendar.getInstance();
                                            cal.setTime(fromDate);
                                            cal.add(Calendar.MINUTE, currentService.getEngagement());
                                            Date maxToDate = cal.getTime();
                                            if (toDate.after(maxToDate) ) {
                                                toTimeEditText.setTextColor(Color.RED);
                                                Toast.makeText(ReserveServiceActivity.this, "Maksimalno angazovanje je premaseno", Toast.LENGTH_SHORT).show();

                                            } else {
                                                toTimeEditText.setTextColor(Color.BLACK);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
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

        Button reserve = findViewById(R.id.reserveButton);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromTimeText = fromTimeEditText.getText().toString();
                String toTimeText = toTimeEditText.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date fromDate = null;
                Date toDate = null;
                try {
                    fromDate = sdf.parse(fromTimeText);
                    toDate = sdf.parse(toTimeText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final Date finalFromDate = fromDate;
                final Date finalToDate = toDate;

                String selectedEvent = eventSpinner.getSelectedItem().toString();
                String selectedEmployee = employeeSpinner.getSelectedItem().toString();

                db.collection("services")
                        .whereEqualTo("id", serviceId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot serviceSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    Service service = serviceSnapshot.toObject(Service.class);
                                    currentService = service;
                                    findEmployee(selectedEmployee, selectedEvent, service, finalFromDate, finalToDate);
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


        Button workCalendarButton = findViewById(R.id.workCalendarButton); // Dodajte ovu liniju iznad onCreate metode
        workCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedEmployee = employeeSpinner.getSelectedItem().toString();
                db.collection("employees")
                        .whereEqualTo("name", selectedEmployee)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot employeeSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    Employee employee = employeeSnapshot.toObject(Employee.class);
                                    String selectedEvent = eventSpinner.getSelectedItem().toString();
                                    db.collection("eventOrganizations")
                                            .whereEqualTo("name", selectedEvent)
                                            .get()
                                            .addOnSuccessListener(queryDocumentSnapshot -> {
                                                if (!queryDocumentSnapshot.isEmpty()) {
                                                    for (DocumentSnapshot eventSnapshot : queryDocumentSnapshot.getDocuments()) {
                                                        EventOrganization event = eventSnapshot.toObject(EventOrganization.class);
                                                        Date selectedDate = event.getDate();
                                                        showWorkCalendarDialog(employee.getEmail(), selectedDate);
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
                            } else {
                                Toast.makeText(ReserveServiceActivity.this, "Employee not found", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ReserveServiceActivity.this, "Failed to search for employee", Toast.LENGTH_SHORT).show();
                        });

            }
        });





        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedEventName = parent.getItemAtPosition(position).toString();

                db.collection("eventOrganizations")
                        .whereEqualTo("name", selectedEventName)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot eventSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    EventOrganization event = eventSnapshot.toObject(EventOrganization.class);

                                    db.collection("services")
                                            .whereEqualTo("id", serviceId)
                                            .get()
                                            .addOnSuccessListener(serviceSnapshots -> {
                                                if (!serviceSnapshots.isEmpty()) {
                                                    for (DocumentSnapshot serviceSnapshot : serviceSnapshots.getDocuments()) {
                                                        Service service = serviceSnapshot.toObject(Service.class);

                                                        if (!canScheduleEvent(event, service)) {
                                                            Toast.makeText(ReserveServiceActivity.this, "Nije moguce zakazati uslugu za taj event zbog reservation deadline-a", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(ReserveServiceActivity.this, "Event not found", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ReserveServiceActivity.this, "Failed to search for event", Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }

    private boolean canScheduleEvent(EventOrganization event, Service service) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, service.getReservationDeadline());  // Dodajemo reservationDeadline dana današnjem datumu
        Date deadlineDate = today.getTime();

        return event.getDate().after(deadlineDate);  // Proveravamo da li je datum događaja nakon deadline datuma
    }

    private void showWorkCalendarDialog(String selectedEmployee, Date selectedDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Work Calendar");

        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        db.collection("workCalendars")
                .whereEqualTo("employee", selectedEmployee)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<WorkCalendar> employeeWorkCalendarList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            WorkCalendar workCalendar = document.toObject(WorkCalendar.class);
                            // Prolazimo kroz listu događaja u workCalendar objektu
                            for (Event event : workCalendar.getEvents()) {
                                // Proveravamo da li datum i vreme početka događaja odgovaraju odabranom datumu
                                Calendar eventCal = Calendar.getInstance();
                                eventCal.setTime(event.getStartTime());
                                int eventYear = eventCal.get(Calendar.YEAR);
                                int eventMonth = eventCal.get(Calendar.MONTH);
                                int eventDay = eventCal.get(Calendar.DAY_OF_MONTH);
                                if (year == eventYear && month == eventMonth && day == eventDay) {
                                    employeeWorkCalendarList.add(workCalendar);

                                }
                            }
                            if (employeeWorkCalendarList.isEmpty()) {
                                // Ako je lista prazna, dodajte poruku "Free Whenever"
                                WorkCalendar freeCalendar = new WorkCalendar();
                                freeCalendar.setWorkStart(workCalendar.getWorkStart());
                                freeCalendar.setWorkEnd(workCalendar.getWorkEnd());
                                employeeWorkCalendarList.add(freeCalendar);
                            }
                        }

                        // Ostatak koda ostaje nepromenjen
                        WorkCalendarAdapter adapter = new WorkCalendarAdapter(this, employeeWorkCalendarList);
                        builder.setAdapter(adapter, null);
                        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
                        builder.show();
                    } else {
                        Log.d("ReserveServiceActivity", "Error getting work calendar documents: ", task.getException());
                        Toast.makeText(ReserveServiceActivity.this, "Failed to load work calendar", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void validateTimeSlot(String fromTime, String toTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date fromDate = sdf.parse(fromTime);
            Date toDate = sdf.parse(toTime);

            String selectedEmployee = employeeSpinner.getSelectedItem().toString();
            db.collection("employees")
                    .whereEqualTo("name", selectedEmployee)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot employeeSnapshot : task.getResult().getDocuments()) {
                                Employee employee = employeeSnapshot.toObject(Employee.class);
                                db.collection("workCalendars")
                                        .whereEqualTo("employee", employee.getEmail())
                                        .get()
                                        .addOnCompleteListener(calendarTask -> {
                                            if (calendarTask.isSuccessful() && calendarTask.getResult() != null) {
                                                boolean isValid = true;
                                                for (QueryDocumentSnapshot document : calendarTask.getResult()) {
                                                    WorkCalendar workCalendar = document.toObject(WorkCalendar.class);
                                                    for (Event event : workCalendar.getEvents()) {
                                                        Date eventStartTime = event.getStartTime();
                                                        Date eventEndTime = event.getEndTime();

                                                        if ((fromDate.before(eventEndTime) && fromDate.after(eventStartTime)) ||
                                                                (toDate.before(eventEndTime) && toDate.after(eventStartTime)) ||
                                                                (fromDate.before(eventStartTime) && toDate.after(eventEndTime))) {
                                                            toTimeEditText.setTextColor(Color.RED);
                                                            Toast.makeText(ReserveServiceActivity.this, "Preklapanje sa postojecim dogadjajem", Toast.LENGTH_SHORT).show();
                                                            isValid = false;
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (isValid) {
                                                    toTimeEditText.setTextColor(Color.BLACK);
                                                }
                                            }
                                        });
                            }
                        }
                    });
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    private void createReservation(Employee selectedEmployee, EventOrganization event, Service service, Date fromTime, Date toTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date fromDate = event.getDate();
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(fromDate);
        Calendar fromTimeCal = Calendar.getInstance();
        fromTimeCal.setTime(fromTime);
        fromCal.set(Calendar.HOUR_OF_DAY, fromTimeCal.get(Calendar.HOUR_OF_DAY));
        fromCal.set(Calendar.MINUTE, fromTimeCal.get(Calendar.MINUTE));
        fromDate = fromCal.getTime();

        Date toDate = event.getDate();
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(toDate);
        Calendar toTimeCal = Calendar.getInstance();
        toTimeCal.setTime(toTime);
        toCal.set(Calendar.HOUR_OF_DAY, toTimeCal.get(Calendar.HOUR_OF_DAY));
        toCal.set(Calendar.MINUTE, toTimeCal.get(Calendar.MINUTE));
        toDate = toCal.getTime();

        Date finalFromDate = fromDate;
        Date finalToDate = toDate;
        db.collection("workCalendars")
                .whereEqualTo("employee", selectedEmployee.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isValid = true;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            WorkCalendar workCalendar = document.toObject(WorkCalendar.class);
                            for (Event event1 : workCalendar.getEvents()) {
                                Date eventStartTime = event1.getStartTime();
                                Date eventEndTime = event1.getEndTime();

                                if ((finalFromDate.before(eventEndTime) && finalFromDate.after(eventStartTime)) ||
                                        (finalToDate.before(eventEndTime) && finalToDate.after(eventStartTime)) ||
                                        (finalFromDate.before(eventStartTime) && finalToDate.after(eventEndTime))) {
                                    Toast.makeText(ReserveServiceActivity.this, "Preklapanje sa postojecim dogadjajem", Toast.LENGTH_SHORT).show();
                                    isValid = false;
                                    break;
                                }
                            }
                        }
                        if (isValid) {
                            auth = FirebaseAuth.getInstance();
                            eventOrganizer = auth.getCurrentUser();
                            db.collection("userDetails")
                                    .whereEqualTo("username", eventOrganizer.getEmail())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            QuerySnapshot querySnapshot = task1.getResult();
                                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                                UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                                                String reservationId = UUID.randomUUID().toString();
                                                if(bundleId == null){
                                                    if (service.getConfirmationMode().equals("Automatic")) {
                                                        Reservation reservation = new Reservation(reservationId, selectedEmployee, userDetails, ReservationStatus.Accepted, service, null, finalFromDate, finalToDate, event);
                                                        db.collection("reservations")
                                                                .add(reservation)
                                                                .addOnSuccessListener(documentReference -> {
                                                                    Toast.makeText(ReserveServiceActivity.this, "Reservation created successfully", Toast.LENGTH_SHORT).show();
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    Toast.makeText(ReserveServiceActivity.this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
                                                                });
                                                    } else {
                                                        Reservation reservation = new Reservation("1", selectedEmployee, userDetails, ReservationStatus.New, service, null, finalFromDate, finalToDate, event);
                                                        db.collection("reservations")
                                                                .add(reservation)
                                                                .addOnSuccessListener(documentReference -> {
                                                                    Toast.makeText(ReserveServiceActivity.this, "Reservation created successfully", Toast.LENGTH_SHORT).show();
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    Toast.makeText(ReserveServiceActivity.this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
                                                                });
                                                    }
                                                }else {
                                                    db.collection("bundles")
                                                            .whereEqualTo("id", bundleId)
                                                            .get()
                                                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                                    for (DocumentSnapshot eventSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                                        CustomBundle bundle = eventSnapshot.toObject(CustomBundle.class);
                                                                        if (service.getConfirmationMode().equals("Automatic")) {
                                                                            Reservation reservation = new Reservation(reservationId, selectedEmployee, userDetails, ReservationStatus.InProgress, service, bundle, finalFromDate, finalToDate, event);
                                                                            db.collection("reservations")
                                                                                    .add(reservation)
                                                                                    .addOnSuccessListener(documentReference -> {
                                                                                        Toast.makeText(ReserveServiceActivity.this, "Reservation for bundle created successfully", Toast.LENGTH_SHORT).show();
                                                                                    })
                                                                                    .addOnFailureListener(e -> {
                                                                                        Toast.makeText(ReserveServiceActivity.this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
                                                                                    });
                                                                        } else {
                                                                            Reservation reservation = new Reservation(reservationId, selectedEmployee, userDetails, ReservationStatus.InProgress, service, bundle, finalFromDate, finalToDate, event);
                                                                            db.collection("reservations")
                                                                                    .add(reservation)
                                                                                    .addOnSuccessListener(documentReference -> {
                                                                                        Toast.makeText(ReserveServiceActivity.this, "Reservation for bundle created successfully", Toast.LENGTH_SHORT).show();
                                                                                    })
                                                                                    .addOnFailureListener(e -> {
                                                                                        Toast.makeText(ReserveServiceActivity.this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
                                                                                    });
                                                                        }
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
                                            }
                                        } else {
                                            Log.w("Firestore", "Error getting documents.", task1.getException());
                                        }
                                    });
                        }
                    } else {
                        Log.d("ReserveServiceActivity", "Error getting documents: ", task.getException());
                        Toast.makeText(ReserveServiceActivity.this, "Failed to load work calendar", Toast.LENGTH_SHORT).show();
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
