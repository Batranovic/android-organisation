package com.example.projekatmobilneaplikacije.activities;

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
import java.util.List;

public class ChatActivity extends AppCompatActivity {

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
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error sending message", e);
            Toast.makeText(ChatActivity.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
