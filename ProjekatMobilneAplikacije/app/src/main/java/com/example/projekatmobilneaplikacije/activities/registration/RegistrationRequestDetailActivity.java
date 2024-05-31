package com.example.projekatmobilneaplikacije.activities.registration;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.RegistrationRequest;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Properties;


public class RegistrationRequestDetailActivity extends AppCompatActivity {
    String companyName, companyMail, ownerName, ownerMail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText denialReasonEditText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_request_detail);
        mAuth = FirebaseAuth.getInstance();
        TextView companyNameTextView = findViewById(R.id.request_company_name);
        TextView companyMailTextView = findViewById(R.id.request_company_mail);
        TextView ownerNameTextView = findViewById(R.id.request_company_owner_name);
        TextView ownerMailTextView = findViewById(R.id.request_company_owner_mail);
        denialReasonEditText = findViewById(R.id.denial_reason);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            companyName = intent.getStringExtra("company_name");
            companyMail = intent.getStringExtra("company_mail");
            ownerName = intent.getStringExtra("owner_name");
            ownerMail = intent.getStringExtra("owner_mail");
        }

        db.collection("registrationRequests")
                .whereEqualTo("owner.userDetails.username", ownerMail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            RegistrationRequest registrationRequest = documentSnapshot.toObject(RegistrationRequest.class);
                            if (registrationRequest != null) {
                                Log.d("Firestore", "Company Name: " + registrationRequest.getOwner().getCompany().getName());
                                Log.d("Firestore", "Owner Name: " + registrationRequest.getOwner().getUserDetails().getName());

                                companyNameTextView.setText(companyName);
                                companyMailTextView.setText(companyMail);
                                ownerNameTextView.setText(ownerName);
                                ownerMailTextView.setText(ownerMail);
                            }
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });

        Button approveRequest = findViewById(R.id.buttonApproveRequest);


        Button rejectRequest = findViewById(R.id.buttonRejectRequest);

        approveRequest.setOnClickListener(v -> {
            sendActivationEmail(ownerMail);
        });

        rejectRequest.setOnClickListener(v -> {
            String denialReason = denialReasonEditText.getText().toString();
            updateDenialReason(denialReason, true);
        });
    }

    private void updateDenialReason(String denialReason, boolean isRejected) {
        db.collection("registrationRequests")
                .whereEqualTo("owner.userDetails.username", ownerMail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();

                            db.collection("registrationRequests").document(documentId)
                                    .update("denialReason", denialReason, "isApproved", !isRejected)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "DocumentSnapshot successfully updated!");
                                        sendRejectionEmail(ownerMail, denialReason);
                                    })
                                    .addOnFailureListener(e -> Log.w("Firestore", "Error updating document", e));
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    private void sendRejectionEmail(String recipientEmail, String denialReason) {
        if ( mAuth.getCurrentUser() != null) {
            String subject = "Registracija odbijena";
            String body = "Nazalost ste odbijeni. Razlog: " + denialReason;

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            emailIntent.setType("message/rfc822");

            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        } else {
            Log.w("Email", "Current user is not logged in.");
        }
    }

    private void sendActivationEmail(String recipientEmail) {
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://projekatmobilneaplikacije.web.app/4gJH?email=" + recipientEmail)  // Dodajte email kao parametar u URL-u
                .setHandleCodeInApp(true)
                .setIOSBundleId("com.example.ios")
                .setAndroidPackageName(
                        "com.example.projekatmobilneaplikacije",
                        true,  // install if not available
                        "12"   // minimum version
                )
                .build();
        FirebaseAuth.getInstance().sendSignInLinkToEmail(recipientEmail,actionCodeSettings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Email", "Sign-in email sent successfully to: " + recipientEmail);
                        // Dodajte kod za obaveštenje korisnika da je mejl poslat
                    } else {
                        Log.e("Email", "Error sending sign-in email", task.getException());
                        // Dodajte kod za obaveštenje korisnika o grešci
                    }
                });
    }
}
