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
import com.example.projekatmobilneaplikacije.model.BundleItem;
import com.example.projekatmobilneaplikacije.model.EventOrganization;
import com.example.projekatmobilneaplikacije.model.PlannedItem;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
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
    private String productId, productName, productSubcategory, bundleId,productPrice;

    private TextView productNameText, productPriceText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reserve_product);
        TextView productPriceTextVIew = findViewById(R.id.product_price);
        TextView productSubcateogryTextVIew = findViewById(R.id.subcategory);
        TextView categoryTextVIew = findViewById(R.id.category);
        TextView avTextVIew = findViewById(R.id.availability);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            bundleId = getIntent().getStringExtra("bundleId");
            productId = intent.getStringExtra("productId");
            productSubcategory = intent.getStringExtra("productSubcategory");
            productName = intent.getStringExtra("productName");
            productPrice = intent.getStringExtra("productPrice");

            Log.d("ReserveProductActivity", "Bundle ID: " + bundleId);
            Log.d("ReserveProductActivity", "Product ID: " + productId);
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
                                    productPrice = String.valueOf(product.getPrice());
                                    productPriceTextVIew.setText(String.valueOf(product.getPrice()));
                                    productSubcateogryTextVIew.setText(product.getSubcategory());
                                    categoryTextVIew.setText(product.getCategory());
                                    avTextVIew.setText(product.getAvailability());

                                } else {
                                    Log.d("ProductDetailActivity", "No such document found for productId: " + productId);
                                }
                            } else {
                                Log.d("ProductDetailActivity", "get failed with ", task.getException());
                            }
                        }
                    });


        }

        productNameText = findViewById(R.id.productName);

        productNameText.setText(productName);

        eventSpinner = findViewById(R.id.eventSpinner);
        populateEventSpinner();


        Button purchase = findViewById(R.id.reserveButton);
        if(bundleId != null){
            purchase.setText("Purchase for bundle");
        }else {
            purchase.setText("Purchase");

        }
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedEventName = eventSpinner.getSelectedItem().toString();
                handlePurchase(selectedEventName);
            }
        });
    }

    private void handlePurchase(String eventName) {


        if(bundleId== null) {
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
                                                if(budget.getPlannedItems() != null){
                                                for (PlannedItem item : budget.getPlannedItems()) {
                                                    if (item.getSubcategoryType().equals(productSubcategory)) {
                                                        found = true;
                                                        break;
                                                    }
                                                }}
                                                if (!found) {
                                                    // Ako ne postoji, kreirajte novu planiranu stavku
                                                    PlannedItem newItem = new PlannedItem(productSubcategory, 0.0);
                                                    budget.getPlannedItems().add(newItem);

                                                    db.collection("plannedItem")
                                                            .add(newItem)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Log.d("EventTypeFragment", "EventType document added with ID: " + documentReference.getId());
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("EventTypeFragment", "Error adding EventType document", e);
                                                                }
                                                            });

                                                }
                                                AchievedItem achievedItem = new AchievedItem(productName, Double.valueOf(productPrice));
                                                budget.getAchievedItems().add(achievedItem);
                                                Log.d("Reserve product", "achievedItem " + achievedItem.getServiceOrProductName());
                                                budget.setSpentBudget(Double.valueOf(budget.getSpentBudget() + productPrice));


                                                db.collection("budgets")
                                                        .document(budget.getId())
                                                        .update("achievedItems",  budget.getAchievedItems(), "plannedItems",  budget.getPlannedItems(), "spentBudget", budget.getSpentBudget())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ReserveProductActivity.this, "Budget updated", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(ReserveProductActivity.this, "Failed to update Budget", Toast.LENGTH_SHORT).show();
                                                                Log.d("ProductDetailActivity", "Error updating product", e);
                                                            }
                                                        });

                                                db.collection("achievedItem")
                                                        .add(achievedItem)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Log.d("EventTypeFragment", "EventType document added with ID: " + documentReference.getId());
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("EventTypeFragment", "Error adding EventType document", e);
                                                            }
                                                        });


                                            }
                                        });
                            }
                        }
                    });
        }else {
            handleBundlePurchase(eventName);
        }
    }

    private void handleBundlePurchase(String eventName) {
          BundleItem bundleItem = new BundleItem(productName, eventName,productSubcategory, bundleId, Double.valueOf(productPrice));
          db.collection("bundleItem")
                    .add(bundleItem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(ReserveProductActivity.this, "Successfuly added product for bundle", Toast.LENGTH_SHORT).show();

                            Log.d("EventTypeFragment", "bundleItem document added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ReserveProductActivity.this, "Failed to reserve product for bundle", Toast.LENGTH_SHORT).show();
                            Log.w("EventTypeFragment", "Error adding bundleItem document", e);
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