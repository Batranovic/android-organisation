package com.example.projekatmobilneaplikacije.activities.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.ProductDetailActivity;
import com.example.projekatmobilneaplikacije.model.AchievedItem;
import com.example.projekatmobilneaplikacije.model.Budget;
import com.example.projekatmobilneaplikacije.model.EventOrganization;
import com.example.projekatmobilneaplikacije.model.PlannedItem;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReserveProductActivity extends AppCompatActivity {
    private Spinner eventSpinner;
    private FirebaseFirestore db;
    private String productId, productName, productSubcategory;
    private int productPrice;
    private TextView productNameText, productPriceText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reserve_product);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("productId");
            db.collection("products")
                    .whereEqualTo("id", productId) // Query for documents where "id" field is equal to serviceId
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0); // Get the first document
                                    Product product = document.toObject(Product.class);
                                    productName = product.getTitle();
                                    productPrice = product.getPrice();

                                } else {
                                    Log.d("ProductDetailActivity", "No such document found for productId: " + productId);
                                }
                            } else {
                                Log.d("ProductDetailActivity", "get failed with ", task.getException());
                            }
                        }
                    });
            productName = intent.getStringExtra("productName");
            productSubcategory = intent.getStringExtra("productSubcategory");

        }

        productNameText = findViewById(R.id.productName);

        productNameText.setText(productName);

        eventSpinner = findViewById(R.id.eventSpinner);
        populateEventSpinner();

        Button purchase = findViewById(R.id.reserveButton);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedEventName = eventSpinner.getSelectedItem().toString();
                handlePurchase(selectedEventName);
            }
        });
    }

    private void handlePurchase(String eventName) {
        // Pronađite odabrani događaj koristeći whereEqualTo
        db.collection("eventOrganizations")
                .whereEqualTo("name", eventName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot eventDoc = task.getResult().getDocuments().get(0);
                        EventOrganization event = eventDoc.toObject(EventOrganization.class);
                        if (event != null) {
                            // Nabavite budžet za događaj koristeći whereEqualTo
                            db.collection("budgets")
                                    .whereEqualTo("id", event.getBudgetId())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful() && task1.getResult() != null && !task1.getResult().isEmpty()) {
                                            DocumentSnapshot budgetDoc = task1.getResult().getDocuments().get(0);
                                            Budget budget = budgetDoc.toObject(Budget.class);
                                            Log.d("Reserve product", "budget " + budget.getId());

                                            boolean found = false;
                                            for (PlannedItem item : budget.getPlannedItems()) {
                                               if (item.getSubcategoryType().equals(productSubcategory)) {
                                                   found = true;
                                                   break;
                                                }
                                            }
                                                if (!found) {
                                                    // Ako ne postoji, kreirajte novu planiranu stavku
                                                    PlannedItem newItem = new PlannedItem(productSubcategory, 0.0);
                                                    budget.getPlannedItems().add(newItem);
                                                    Log.d("Reserve product", "newItem " + newItem.getSubcategoryType());
                                                    db.collection("plannedItem").document(newItem.getSubcategoryType())
                                                            .set(newItem)
                                                            .addOnSuccessListener(aVoid -> {
                                                                Toast.makeText(ReserveProductActivity.this, "Product reserved successfully", Toast.LENGTH_SHORT).show();
                                                                Log.d("Reserve product", "achievedItem " + newItem.getSubcategoryType());

                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Toast.makeText(ReserveProductActivity.this, "Failed to reserve product", Toast.LENGTH_SHORT).show();
                                                                Log.d("Reserve product", "nije kupljeno " + newItem.getSubcategoryType());

                                                            });
                                                }
                                                // Dodajte ostvarenu stavku u budžet
                                                AchievedItem achievedItem = new AchievedItem(productName, (double) productPrice);
                                                budget.getAchievedItems().add(achievedItem);
                                                Log.d("Reserve product", "achievedItem " + achievedItem.getServiceOrProductName());
                                                budget.setSpentBudget(budget.getSpentBudget() + productPrice);

                                                // Ažurirajte budžet u Firestore
                                                db.collection("budgets").document(budget.getId())
                                                        .set(budget)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(ReserveProductActivity.this, "Product reserved successfully", Toast.LENGTH_SHORT).show();
                                                            Log.d("Reserve product", "budget " + budget.getId());

                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(ReserveProductActivity.this, "Failed to reserve product", Toast.LENGTH_SHORT).show();
                                                            Log.d("Reserve product", "budget " + budget.getId());

                                                        });


                                                db.collection("achievedItem").document(achievedItem.getServiceOrProductName())
                                                        .set(achievedItem)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(ReserveProductActivity.this, "Product reserved successfully", Toast.LENGTH_SHORT).show();
                                                            Log.d("Reserve product", "kupljeno " + achievedItem.getServiceOrProductName());

                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(ReserveProductActivity.this, "Failed to reserve product", Toast.LENGTH_SHORT).show();
                                                            Log.d("Reserve product", "nije kupljeno " + achievedItem.getServiceOrProductName());

                                                        });

                                        }
                                    });
                        }
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

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ReserveProductActivity.this, android.R.layout.simple_spinner_item, eventNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        eventSpinner.setAdapter(adapter);
                    } else {
                        Log.d("ReserveServiceActivity", "Error getting documents: ", task.getException());
                    }
                });
    }
}