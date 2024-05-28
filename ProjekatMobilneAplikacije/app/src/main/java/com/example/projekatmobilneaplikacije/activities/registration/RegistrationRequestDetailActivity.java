package com.example.projekatmobilneaplikacije.activities.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.EventType;
import com.example.projekatmobilneaplikacije.model.UserDetails;
import com.example.projekatmobilneaplikacije.model.enumerations.Owner;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

public class RegistrationRequestDetailActivity extends AppCompatActivity {
    String companyName, companyMail, ownerName, ownerMail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_request_detail);

        TextView companyNameTextView = findViewById(R.id.request_company_name);
        TextView companyMailTextView = findViewById(R.id.request_company_mail);
        TextView ownerNameTextView = findViewById(R.id.request_company_owner_name);
        TextView ownerMailTextView = findViewById(R.id.request_company_owner_mail);

        Intent intent = getIntent();
        if (intent != null) {
            companyName = intent.getStringExtra("company_name");
            companyMail = intent.getStringExtra("company_mail");
            ownerName = intent.getStringExtra("owner_name");
            ownerMail = intent.getStringExtra("owner_mail");
        }

        db.collection("owners")
                .whereEqualTo("userDetails.username", ownerMail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            Owner owner = documentSnapshot.toObject(Owner.class);
                            if (owner != null) {
                                Log.d("Firestore", "Company Name: " + owner.getCompany().getName());
                                Log.d("Firestore", "Owner Name: " + owner.getUserDetails().getName());

                                companyNameTextView.setText(companyName);
                                companyMailTextView.setText(companyMail);
                                ownerNameTextView.setText(ownerName);
                                ownerMailTextView.setText(ownerMail);
                            }
                        }
                    } else {
                        // Handle errors here
                    }
                });
    }

}