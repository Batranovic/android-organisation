package com.example.projekatmobilneaplikacije.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.reservation.ReserveServiceActivity;
import com.example.projekatmobilneaplikacije.model.RegistrationRequest;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ServiceDetailActivity extends AppCompatActivity {

    EditText titleEditText, descriptionEditText, subcategoryEditText, eventTypeEditText, priceEditText, availabilityEditText, visibilityEditText;
    EditText specificityEditText, discountEditText, durationEditText, engagementEditText, reservationDeadlineEditText, cancellationDeadlineEditText, confirmationModeEditText;
    private boolean isFavorite = false;
    private ImageButton favoriteButton2;
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
        favoriteButton2 = findViewById(R.id.favoriteButton2);

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
        favoriteButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;
                handleFavorite(serviceId, isFavorite);
            }
        });

        ImageButton reserveServiceButton = findViewById(R.id.reserveServiceButton);
        reserveServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Step 1: Get Service ID
                String serviceId = getIntent().getStringExtra("serviceId");

                // Step 2: Retrieve Service from Firestore
                db.collection("services")
                        .whereEqualTo("id", serviceId) // Query for documents where "id" field is equal to serviceId
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        DocumentSnapshot document = querySnapshot.getDocuments().get(0); // Get the first document
                                        Service service = document.toObject(Service.class);

                                        if (service.getAvailability() != null && service.getAvailability().equalsIgnoreCase("Yes")) {
                                            // Service is available, navigate to ReserveServiceActivity
                                            Intent intent = new Intent(ServiceDetailActivity.this, ReserveServiceActivity.class);
                                            intent.putExtra("serviceId", serviceId);
                                            startActivity(intent);
                                        } else {
                                            // Service is not available, show toast message
                                            Toast.makeText(ServiceDetailActivity.this, "Service is unavailable", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.d("ServiceDetailActivity", "No such document found for serviceId: " + serviceId);
                                    }
                                } else {
                                    Log.d("ServiceDetailActivity", "get failed with ", task.getException());
                                }
                            }
                        });
            }
        });


        ImageButton deleteButton = findViewById(R.id.deleteProductButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceId = getIntent().getStringExtra("serviceId");
                Log.d("ServiceDetailActivity","ServiceId:" + serviceId);
                deleteService(serviceId);
            }
        });

        ImageButton editButton = findViewById(R.id.editProductButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceId = getIntent().getStringExtra("serviceId");
                Log.d("ServiceDetailActivity","ServiceId:" + serviceId);
                editService(serviceId);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void handleFavorite(String serviceId, boolean isFavorite) {
        DocumentReference favoriteRef = db.collection("favorites").document(serviceId);

        if (isFavorite) {
            Map<String, Object> favoriteData = new HashMap<>();
            favoriteData.put("id", serviceId);


            favoriteRef.set(favoriteData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ServiceDetailActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        Log.d("ServiceDetailActivity", "Added to favorites");
                    })
                    .addOnFailureListener(e -> Log.w("ProductDetailActivity", "Error adding to favorites", e));
        } else {
            favoriteRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ServiceDetailActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                        Log.d("ServiceDetailActivity", "Removed from favorites");
                    })
                    .addOnFailureListener(e -> Log.w("ServiceDetailActivity", "Error removing from favorites", e));
        }
    }


    private void deleteService(String serviceId) {
        // Pronalaženje dokumenta sa odgovarajućim productId
        db.collection("services")
                .whereEqualTo("id", serviceId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0); // Uzmemo prvi dokument (ako ima više, uzmemo prvi)
                                String documentId = document.getId();
                                Log.d("","Document id: " + documentId);

                                // Ažuriranje polja isDeleted na true
                                db.collection("services")
                                        .document(documentId)
                                        .update("isDeleted", true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ServiceDetailActivity.this, "Service deleted", Toast.LENGTH_SHORT).show();
                                                finish(); // Zatvorite aktivnost nakon brisanja
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ServiceDetailActivity.this, "Failed to delete service", Toast.LENGTH_SHORT).show();
                                                Log.d("ServiceDetailActivity", "Error deleting service", e);
                                            }
                                        });
                            } else {
                                Log.d("ServiceDetailActivity", "No document found with serviceId: " + serviceId);
                            }
                        } else {
                            Log.d("ServiceDetailActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void editService(String serviceId) {
        String newTitle = titleEditText.getText().toString();
        String newDescription = descriptionEditText.getText().toString();
        String newSubcategory = subcategoryEditText.getText().toString();
        String newEventType = eventTypeEditText.getText().toString();
        int newPrice = Integer.parseInt(priceEditText.getText().toString());
        String newAvailability = availabilityEditText.getText().toString();
        String newVisibility = visibilityEditText.getText().toString();
        String newSpecificity = specificityEditText.getText().toString();
        String newDiscount = discountEditText.getText().toString();
        String newDuration = durationEditText.getText().toString();
        String newEngagement = engagementEditText.getText().toString();
        String newReservationDeadline = reservationDeadlineEditText.getText().toString();
        String newCancellationDeadline = cancellationDeadlineEditText.getText().toString();
        String newConfirmationMode = confirmationModeEditText.getText().toString();
        // Pronalaženje dokumenta sa odgovarajućim productId
        db.collection("services")
                .whereEqualTo("id", serviceId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0); // Uzmemo prvi dokument (ako ima više, uzmemo prvi)
                                String documentId = document.getId();
                                Log.d("","Document id: " + documentId);

                                // Mapa koja sadrži nove vrijednosti polja
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("title", newTitle);
                                updates.put("description", newDescription);
                                updates.put("subcategory", newSubcategory);
                                updates.put("eventType", newEventType);
                                updates.put("price", newPrice);
                                updates.put("availability", newAvailability);
                                updates.put("visibility", newVisibility);
                                updates.put("specificity", newSpecificity);
                                updates.put("discount", newDiscount);
                                updates.put("duration", newDuration);
                                updates.put("engagement", newEngagement);
                                updates.put("reservationDeadline", newReservationDeadline);
                                updates.put("cancellationDeadline", newCancellationDeadline);
                                updates.put("confirmationMode", newConfirmationMode);


                                // Ažuriranje dokumenta s novim vrijednostima polja
                                db.collection("services")
                                        .document(documentId)
                                        .update(updates)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ServiceDetailActivity.this, "Service updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ServiceDetailActivity.this, "Failed to update service", Toast.LENGTH_SHORT).show();
                                                Log.d("ServiceDetailActivity", "Error updating service", e);
                                            }
                                        });
                            } else {
                                Log.d("ServiceDetailActivity", "No document found with serviceId: " + serviceId);
                            }
                        } else {
                            Log.d("ServiceDetailActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}