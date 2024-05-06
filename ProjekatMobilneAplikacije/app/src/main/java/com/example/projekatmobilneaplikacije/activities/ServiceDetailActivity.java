package com.example.projekatmobilneaplikacije.activities;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ServiceDetailActivity extends AppCompatActivity {

    EditText titleEditText, descriptionEditText, subcategoryEditText, eventTypeEditText, priceEditText, availabilityEditText, visibilityEditText;
    EditText specificityEditText, discountEditText, durationEditText, engagementEditText, reservationDeadlineEditText, cancellationDeadlineEditText, confirmationModeEditText;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_detail);

        // Initialize views
        titleEditText = findViewById(R.id.title);
        descriptionEditText = findViewById(R.id.description);
        subcategoryEditText = findViewById(R.id.subcategory);
        eventTypeEditText = findViewById(R.id.eventType);
        priceEditText = findViewById(R.id.price);
        availabilityEditText = findViewById(R.id.availability);
        visibilityEditText = findViewById(R.id.visibility);
        specificityEditText = findViewById(R.id.specificity);
        discountEditText = findViewById(R.id.discount);
        durationEditText = findViewById(R.id.duration);
        engagementEditText = findViewById(R.id.engagement);
        reservationDeadlineEditText = findViewById(R.id.reservationDeadline);
        cancellationDeadlineEditText = findViewById(R.id.cancellationDeadline);
        confirmationModeEditText = findViewById(R.id.confirmationMode);

        // Retrieve the product ID from the intent
        String serviceId = getIntent().getStringExtra("serviceId");

        // Set the product title to the EditText
        titleEditText.setText(getIntent().getStringExtra("title"));
        descriptionEditText.setText(getIntent().getStringExtra("description"));
        subcategoryEditText.setText(getIntent().getStringExtra("subcategory"));
        eventTypeEditText.setText(getIntent().getStringExtra("eventType"));
        priceEditText.setText(String.valueOf(getIntent().getIntExtra("price", 0)));
        availabilityEditText.setText(getIntent().getStringExtra("availability"));
        visibilityEditText.setText(getIntent().getStringExtra("visibility"));
        specificityEditText.setText(getIntent().getStringExtra("specificity"));
        discountEditText.setText(getIntent().getStringExtra("discount"));
        durationEditText.setText(getIntent().getStringExtra("duration"));
        engagementEditText.setText(getIntent().getStringExtra("engagement"));
        reservationDeadlineEditText.setText(getIntent().getStringExtra("reservationDeadline"));
        cancellationDeadlineEditText.setText(getIntent().getStringExtra("cancellationDeadline"));
        confirmationModeEditText.setText(getIntent().getStringExtra("confirmationMode"));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}