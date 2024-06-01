package com.example.projekatmobilneaplikacije.activities.registration;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
        TextView ownerPhoneTextView = findViewById(R.id.request_company_owner_phone);
        TextView companyPhoneTextView = findViewById(R.id.request_company__phone);
        TextView companyAddressTextView = findViewById(R.id.request_company__adress);



        //denialReasonEditText = findViewById(R.id.denial_reason);
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
                                ownerPhoneTextView.setText(registrationRequest.getOwner().getUserDetails().getPhone());
                                companyPhoneTextView.setText(registrationRequest.getOwner().getCompany().getPhoneNumber());
                                companyAddressTextView.setText(registrationRequest.getOwner().getCompany().getAddress());
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
/*
        rejectRequest.setOnClickListener(v -> {
            String denialReason = denialReasonEditText.getText().toString();
            updateDenialReason(denialReason, true);
        });*/

        rejectRequest.setOnClickListener(v -> {
            showDenialReasonDialog();
        });
    }
    private void showDenialReasonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Denial Reason");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String denialReason = input.getText().toString();
            updateDenialReason(denialReason, true);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
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
                .setUrl("https://projekat-mobilne-aplikacije.web.app/verify?email=" + recipientEmail)
                .setHandleCodeInApp(true)
                .setIOSBundleId("com.example.ios")
                .setAndroidPackageName(
                        "com.example.projekatmobilneaplikacije",
                        true,
                        "12"
                )
                .build();
        FirebaseAuth.getInstance().sendSignInLinkToEmail(recipientEmail,actionCodeSettings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Email", "Sign-in email sent successfully to: " + recipientEmail);

                    } else {
                        Log.e("Email", "Error sending sign-in email", task.getException());

                    }
                });
    }
}
