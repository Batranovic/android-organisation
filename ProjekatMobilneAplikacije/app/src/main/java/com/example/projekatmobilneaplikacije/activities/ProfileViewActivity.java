package com.example.projekatmobilneaplikacije.activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.Report;
import com.example.projekatmobilneaplikacije.model.enumerations.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class ProfileViewActivity extends AppCompatActivity {
    private Button btnReport;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "ProfileViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        String reportedEntityUsername = getIntent().getStringExtra("username");

        TextView nameTextView = findViewById(R.id.name);
        TextView surnameTextView = findViewById(R.id.surname);

        nameTextView.setText(name);
        surnameTextView.setText(surname);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnReport = findViewById(R.id.btn_report);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Report button clicked");
                showReportDialog(reportedEntityUsername);
            }
        });



    }

    private void showReportDialog(String reportedEntityUsername) {
        Log.d(TAG, "Showing report dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Report reason");

        final EditText input = new EditText(this);
        input.setHint("Enter reason");
        builder.setView(input);

        builder.setPositiveButton("Report", (dialog, which) -> {
            String reason = input.getText().toString();
            if (!reason.isEmpty()) {
                if (user != null) {
                    String reportId = db.collection("reports").document().getId();
                    Date currentTimestamp = new Date();
                    // Napravite novi objekat Report koristeći name i surname
                    Report report = new Report(reportId, reportedEntityUsername, user.getEmail(), reason, Status.Pending, currentTimestamp);

                    db.collection("reports").document(reportId)
                            .set(report)
                            .addOnSuccessListener(aVoid1 -> Toast.makeText(this, "Report saved", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Error saving report", Toast.LENGTH_SHORT).show());

                    String notificationId = db.collection("notifications").document().getId();
                    String adminEmail = "nina@gmail.com";
                    Notification notification = new Notification(
                            notificationId,
                            "Report",
                            "Reason: " + reason,
                            false,
                            currentTimestamp,
                            adminEmail
                    );

                    db.collection("notifications").document(notificationId)
                            .set(notification)
                            .addOnSuccessListener(aVoid1 -> Toast.makeText(this, "Notification sent", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Error sending notification", Toast.LENGTH_SHORT).show());
                }
            } else {
                input.setError("Enter reason");
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}