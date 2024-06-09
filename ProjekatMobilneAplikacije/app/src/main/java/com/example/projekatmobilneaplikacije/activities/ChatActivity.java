package com.example.projekatmobilneaplikacije.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.MessageAdapter;
import com.example.projekatmobilneaplikacije.model.Chat;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements SensorEventListener {

    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private List<Chat> messageList;

    private EditText editTextMessage;
    private Button buttonSend;
    private Spinner spinnerEmployees;

    private FirebaseFirestore db;
    private CollectionReference messagesRef;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private String selectedEmployeeEmail;
    private List<Employee> employees;

    private static final String TAG = "ChatActivity";

    private SensorManager sensorManager;
    private float accel; // current acceleration including gravity
    private float accelCurrent; // current acceleration excluding gravity
    private float accelLast; // last acceleration excluding gravity

    private enum ViewType {
        UNREAD,
        READ,
        ALL
    }

    private ViewType currentViewType = ViewType.UNREAD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_chat);

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        spinnerEmployees = findViewById(R.id.spinnerEmployees);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        loadEmployees();

        spinnerEmployees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEmployeeEmail = employees.get(position).getEmail();
                loadMessages(); // Izmenjeno: Učitavanje poruka
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedEmployeeEmail = null;
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Send button clicked");
                String messageText = editTextMessage.getText().toString();
                if (!TextUtils.isEmpty(messageText)) {
                    sendMessage(messageText); // Izmenjeno: Slanje poruke
                } else {
                    Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        accel = 10f;
        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        accelLast = accelCurrent;
        accelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = accelCurrent - accelLast;
        accel = accel * 0.9f + delta; // perform low-cut filter

        if (accel > 12) {
            onShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private void onShake() {
        rotateView();
    }

    private void rotateView() {
        switch (currentViewType) {
            case UNREAD:
                currentViewType = ViewType.READ;
                loadNotifications(ViewType.READ);
                break;
            case READ:
                currentViewType = ViewType.ALL;
                loadNotifications(ViewType.ALL);
                break;
            case ALL:
                currentViewType = ViewType.UNREAD;
                loadNotifications(ViewType.UNREAD);
                break;
        }
    }

    private void loadNotifications(ViewType viewType) {
        // Implement logic to load and display notifications based on the viewType
        switch (viewType) {
            case UNREAD:
                Toast.makeText(this, "Displaying Unread Notifications", Toast.LENGTH_SHORT).show();
                // Load unread notifications from Firestore
                break;
            case READ:
                Toast.makeText(this, "Displaying Read Notifications", Toast.LENGTH_SHORT).show();
                // Load read notifications from Firestore
                break;
            case ALL:
                Toast.makeText(this, "Displaying All Notifications", Toast.LENGTH_SHORT).show();
                // Load all notifications from Firestore
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void sendMessage(String messageText) {
        if (selectedEmployeeEmail == null) {
            Toast.makeText(this, "Select a receiver", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Sending message: " + messageText);
        String senderEmail = currentUser.getEmail();
        Chat message = new Chat(senderEmail, selectedEmployeeEmail, messageText, Timestamp.now(), "sent");

        messagesRef = db.collection("chats").document(currentUser.getUid() + "_" + selectedEmployeeEmail).collection("messages");
        messagesRef.add(message).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "Message sent successfully");
            Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
            editTextMessage.setText(""); // Dodato: Brisanje teksta iz polja za unos poruke
            loadMessages(); // Izmenjeno: Učitavanje poruka nakon slanja poruke
            if ("tamara.miljevic28@gmail.com".equals(selectedEmployeeEmail)) {
                String notificationId = db.collection("notifications").document().getId();
                Date currentDate = new Date();
                Notification notification = new Notification(
                        notificationId,
                        "New Message",
                        "You have a new message",
                        false,
                        currentDate,
                        selectedEmployeeEmail
                );

                db.collection("notifications").document(notificationId)
                        .set(notification)
                        .addOnSuccessListener(aVoid1 -> Log.d(TAG, "Notification sent"))
                        .addOnFailureListener(e -> Log.e(TAG, "Error sending notification", e));
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error sending message", e);
            Toast.makeText(ChatActivity.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadEmployees() {
        CollectionReference employeesRef = db.collection("employees");
        employees = new ArrayList<>();

        employeesRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Employee employee = documentSnapshot.toObject(Employee.class);
                employees.add(employee);
            }

            List<String> employeeDisplayList = new ArrayList<>();
            for (Employee employee : employees) {
                employeeDisplayList.add(employee.getEmail());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, employeeDisplayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEmployees.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error loading employees", e);
        });
    }

    private void loadMessages() {
        if (selectedEmployeeEmail == null) return;

        messagesRef = db.collection("chats").document(currentUser.getUid() + "_" + selectedEmployeeEmail).collection("messages");
        messagesRef.orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(ChatActivity.this, "Error loading messages: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error loading messages", error);
                    return;
                }

                messageList.clear(); // Izmenjeno: Brisanje liste pre dodavanja novih poruka
                for (QueryDocumentSnapshot doc : value) {
                    Chat message = doc.toObject(Chat.class);
                    messageList.add(message);
                    Log.d(TAG, "Message loaded: " + message.getContent());
                }
                messageAdapter.notifyDataSetChanged(); // Izmenjeno: Obaveštavanje adaptera o promeni podataka
                recyclerViewMessages.scrollToPosition(messageList.size() - 1); // Izmenjeno: Pomicanje na poslednju poruku
            }
        });
    }

    private void redirectToLogin() {
        // Implement the logic to redirect the user to the login activity
        // For example, startActivity(new Intent(this, LoginActivity.class));
        // finish();
    }
}
