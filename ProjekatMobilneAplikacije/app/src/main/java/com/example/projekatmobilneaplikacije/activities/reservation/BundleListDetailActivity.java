package com.example.projekatmobilneaplikacije.activities.reservation;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.EditEventTypeActivity;
import com.example.projekatmobilneaplikacije.activities.ProductDetailActivity;
import com.example.projekatmobilneaplikacije.adapters.BundleProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.BundleServiceListAdapter;
import com.example.projekatmobilneaplikacije.model.AchievedItem;
import com.example.projekatmobilneaplikacije.model.Budget;
import com.example.projekatmobilneaplikacije.model.BundleItem;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.EventOrganization;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.PlannedItem;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.example.projekatmobilneaplikacije.model.Service;
import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BundleListDetailActivity extends AppCompatActivity {
    private BundleServiceListAdapter adapter;
    private BundleProductListAdapter padapter;

    private String bundleId;

    public static ArrayList<Service> services = new ArrayList<>();
    public static ArrayList<Product> products = new ArrayList<>();
    private boolean isFavorite = false;
    private ImageButton favoriteButton3;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference servicesRef = db.collection("services");
    CollectionReference productsRef = db.collection("products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle_list_detail);

        if (getIntent() != null) {
            bundleId = getIntent().getStringExtra("bundleId");
        }

        ListView servicesListView = findViewById(R.id.servicesList);
        ListView productsListView = findViewById(R.id.productsList);
        favoriteButton3 = findViewById(R.id.favoriteButton3);

        if (bundleId != null) {
            // First, retrieve the bundle based on bundleId
            db.collection("bundles")
                    .whereEqualTo("id", bundleId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                CustomBundle bundle = documentSnapshot.toObject(CustomBundle.class);
                                if (bundle != null) {
                                    List<String> serviceIds = bundle.getServiceIds();
                                    if (serviceIds != null && !serviceIds.isEmpty()) {
                                        fetchServices(serviceIds);
                                    }
                                    List<String> productIds = bundle.getProductIds();
                                    if(productIds!=null && !productIds.isEmpty()){
                                        fetchProducts(productIds);
                                    }
                                }
                            }
                        } else {
                            Log.d("BundleListDetailActivity", "Bundle not found");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("BundleListDetailActivity", "Error fetching bundle", e);
                    });
        } else {
            Log.e("BundleListDetailActivity", "Bundle ID is null");
        }
        favoriteButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;
                handleFavorite(bundleId, isFavorite);
            }
        });

        // Set up the adapter
        adapter = new BundleServiceListAdapter(this, services);
        servicesListView.setAdapter(adapter);

        padapter = new BundleProductListAdapter(this, products);
        productsListView.setAdapter(padapter);

        productsListView.setOnItemClickListener((parent, view, position, id) -> {
            Product selectedProduct = products.get(position);
            Intent intent = new Intent(BundleListDetailActivity.this, ReserveProductActivity.class);
            intent.putExtra("bundleId", bundleId);
            intent.putExtra("productId", selectedProduct.getId());
            intent.putExtra("productSubcategory", selectedProduct.getSubcategory());
            intent.putExtra("productName", selectedProduct.getTitle());
            intent.putExtra("productPrice", selectedProduct.getPrice());

            Log.d("ReserveProductActivity", "Bundle ID: " + bundleId);
            Log.d("ReserveProductActivity", "Product ID: " +  selectedProduct.getId());
            startActivity(intent);
        });


        servicesListView.setOnItemClickListener((parent, view, position, id) -> {
            Service selectedService = services.get(position);
            Intent intent = new Intent(BundleListDetailActivity.this, ReserveServiceActivity.class);
            intent.putExtra("bundleId", bundleId);
            intent.putExtra("serviceId", selectedService.getId());
            Log.d("ReserveProductActivity", "Bundle ID: " + bundleId);
            Log.d("ReserveProductActivity", "Product ID: " +  selectedService.getId());
            startActivity(intent);
        });

        Button reserve = findViewById(R.id.reserveButton);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReservation(bundleId);

            }
        });

       // checkReservationStatus(bundleId, servicesListView);
    }
    private void handleFavorite(String bundleId, boolean isFavorite) {
        DocumentReference favoriteRef = db.collection("favorites").document(bundleId);

        if (isFavorite) {
            Map<String, Object> favoriteData = new HashMap<>();
            favoriteData.put("id", bundleId);
            favoriteData.put("title", "style");

            favoriteRef.set(favoriteData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(BundleListDetailActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        Log.d("BundleListDetailActivity", "Added to favorites");
                    })
                    .addOnFailureListener(e -> Log.w("BundleListDetailActivity", "Error adding to favorites", e));
        } else {
            favoriteRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(BundleListDetailActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                        Log.d("BundleListDetailActivity", "Removed from favorites");
                    })
                    .addOnFailureListener(e -> Log.w("BundleListDetailActivity", "Error removing from favorites", e));
        }
    }


    private void makeReservation(String bundleId) {
        db.collection("bundles")
                .whereEqualTo("id", bundleId)
                .get()
                .addOnSuccessListener(bundleSnapshots -> {
                    if (!bundleSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot bundleSnapshot : bundleSnapshots) {
                            CustomBundle bundle = bundleSnapshot.toObject(CustomBundle.class);
                            if (bundle.getServiceIds() == null && !bundle.getProductIds().isEmpty()) {
                                purchaseProducts(bundle);
                            } else {
                                db.collection("reservations")
                                        .whereEqualTo("bundle", bundle)
                                        .whereEqualTo("status", "InProgress")
                                        .get()
                                        .addOnSuccessListener(reservationSnapshots -> {
                                            for (QueryDocumentSnapshot reservationSnapshot : reservationSnapshots) {
                                                Reservation reservation = reservationSnapshot.toObject(Reservation.class);
                                                if (Objects.equals(reservation.getService().getConfirmationMode(), "Automatic")) {
                                                    DocumentReference reservationRef = db.collection("reservations").document(reservationSnapshot.getId());

                                                    // Ažuriramo status rezervacije
                                                    reservationRef.update("status", ReservationStatus.Accepted)
                                                            .addOnSuccessListener(aVoid -> {
                                                                Log.d(TAG, "Reservation status successfully updated.");
                                                                Toast.makeText(BundleListDetailActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.e(TAG, "Error updating reservation status", e);
                                                            });
                                                } else {
                                                    DocumentReference reservationRef = db.collection("reservations").document(reservationSnapshot.getId());

                                                    // Ažuriramo status rezervacije
                                                    reservationRef.update("status", ReservationStatus.New)
                                                            .addOnSuccessListener(aVoid -> {
                                                                Log.d(TAG, "Reservation status successfully updated.");
                                                                Toast.makeText(BundleListDetailActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.e(TAG, "Error updating reservation status", e);
                                                            });
                                                }

                                                String notificationId = db.collection("notifications").document().getId();
                                                Date currentTimestamp = new Date();
                                                Notification notification = new Notification(
                                                        notificationId,
                                                        "Bundle reserved",
                                                        "Bundle reserved: " + bundle.getTitle(),
                                                        false,
                                                        currentTimestamp,
                                                        reservation.getEmployee().getEmail()
                                                );

                                                // Save the notification to Firestore
                                                db.collection("notifications").document(notificationId)
                                                        .set(notification)
                                                        .addOnSuccessListener(aVoid1 -> Toast.makeText(this, "Notification sent", Toast.LENGTH_SHORT).show())
                                                        .addOnFailureListener(e -> Toast.makeText(this, "Error sending notification", Toast.LENGTH_SHORT).show());

                                            }
                                        })

                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Error fetching reservations with bundle: " + bundle, e);
                                        });
                            }
                        }
                    } else {
                        Log.d(TAG, "No bundles found with ID: " + bundleId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching bundle with ID: " + bundleId, e);
                });
    }

    private void  purchaseProducts(CustomBundle bundle){

        db.collection("bundleItem")
                .get()
                .addOnSuccessListener(queryDocumentSnapshot1 -> {
                    for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshot1) {
                        if (documentSnapshot1.exists()) {
                            BundleItem bundleItem = documentSnapshot1.toObject(BundleItem.class);
                            if(bundleItem.getBundleId().equals(bundle.getId())){
                                db.collection("eventOrganizations")
                                        .whereEqualTo("name", bundleItem.getEventName())
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshot -> {
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    EventOrganization event = documentSnapshot.toObject(EventOrganization.class);
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
                                                                                if (item.getSubcategoryType().equals(bundleItem.getProductSubcategory())) {
                                                                                    found = true;
                                                                                    break;
                                                                                }
                                                                            }}
                                                                        if (!found) {
                                                                            // Ako ne postoji, kreirajte novu planiranu stavku
                                                                            PlannedItem newItem = new PlannedItem(bundleItem.getProductSubcategory(), 0.0);
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
                                                                        AchievedItem achievedItem = new AchievedItem(bundleItem.getProductName(), (double) bundleItem.getProductPrice());
                                                                        budget.getAchievedItems().add(achievedItem);
                                                                        Log.d("Reserve product", "achievedItem " + achievedItem.getServiceOrProductName());
                                                                        budget.setSpentBudget(budget.getSpentBudget() + bundleItem.getProductPrice());


                                                                        db.collection("budgets")
                                                                                .document(budget.getId())
                                                                                .update("achievedItems",  budget.getAchievedItems(), "plannedItems",  budget.getPlannedItems(), "spentBudget", budget.getSpentBudget())
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        Toast.makeText(BundleListDetailActivity.this, "Budget updated", Toast.LENGTH_SHORT).show();
                                                                                        db.collection("bundleItem")
                                                                                                .whereEqualTo("id", bundle.getId())
                                                                                                .get()
                                                                                                .addOnSuccessListener(querySnapshot -> {
                                                                                                    for (QueryDocumentSnapshot document : querySnapshot) {
                                                                                                        document.getReference().delete()
                                                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                                        Log.d("BundleListDetailActivity", "BundleItem successfully deleted");
                                                                                                                    }
                                                                                                                })
                                                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                                                    @Override
                                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                                        Log.e("BundleListDetailActivity", "Error deleting BundleItem", e);
                                                                                                                    }
                                                                                                                });
                                                                                                    }
                                                                                                })
                                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                                    @Override
                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                        Log.e("BundleListDetailActivity", "Error fetching BundleItem", e);
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Toast.makeText(BundleListDetailActivity.this, "Failed to update Budget", Toast.LENGTH_SHORT).show();
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


                                                } else {
                                                    Log.d("BundleListDetailActivity", "bundle not found for ID: " + bundle);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("BundleListDetailActivity", "Error fetching bundle with ID: " + bundle, e);
                                        });
                            }


                        } else {
                            Log.d("BundleListDetailActivity", "bundle not found for ID: " + bundle);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("BundleListDetailActivity", "Error fetching bundle with ID: " + bundle, e);
                });
    }

    private void fetchProducts(List<String> productIds) {
        products.clear();

        for (String productId : productIds) {
            productsRef.whereEqualTo("id", productId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                Product product = documentSnapshot.toObject(Product.class);
                                if (product != null) {
                                    products.add(product);
                                }
                            } else {
                                Log.d("BundleListDetailActivity", "Product not found for ID: " + productId);
                            }
                        }
                        padapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("BundleListDetailActivity", "Error fetching product with ID: " + productId, e);
                    });
        }
    }



    private void fetchServices(List<String> serviceIds) {
        services.clear();

        for (String serviceId : serviceIds) {
            servicesRef.whereEqualTo("id", serviceId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                Service service = documentSnapshot.toObject(Service.class);
                                if (service != null) {
                                    services.add(service);
                                }
                            } else {
                                Log.d("BundleListDetailActivity", "Service not found for ID: " + serviceId);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("BundleListDetailActivity", "Error fetching service with ID: " + serviceId, e);
                    });
        }
    }/*
    private void checkReservationStatus(String bundleId, ListView servicesListView) {
        db.collection("bundles")
                .whereEqualTo("id", bundleId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            CustomBundle bundle = documentSnapshot.toObject(CustomBundle.class);
                            if (bundle != null) {
                                List<String> serviceIds = bundle.getServiceIds();
                                for(String serviceId: serviceIds){
                                    db.collection("reservations")
                                            .whereEqualTo("bundle", bundle)
                                            .whereEqualTo("serviceId", serviceId)
                                            .whereEqualTo("status", ReservationStatus.InProgress.toString())
                                            .get()
                                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                if (!queryDocumentSnapshots1.isEmpty()) {
                                                    // Rezervacija postoji, kartica ostaje iste boje
                                                } else {
                                                    // Rezervacija ne postoji, bojimo karticu crveno
                                                    // Prvo pronađemo odgovarajuću karticu
                                                    // Proveravamo da li se bundleId poklapa
                                                        for (int i = 0; i < serviceIds.size(); i++) {
                                                            if (serviceIds.get(i).equals(serviceId)) {

                                                                View view = servicesListView.getChildAt(i);
                                                                if (view != null) {
                                                                    // Pronađena kartica, postavljamo boju
                                                                    Log.d(TAG, "Crven servis" + serviceId);
                                                                    view.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                                                                    break; // Prekidamo petlju nakon što je pronađena prva kartica
                                                                }
                                                            }
                                                        }

                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e(TAG, "Error checking reservation status", e);
                                            });
                                }

                            }
                        }
                    } else {
                        Log.d("BundleListDetailActivity", "Bundle not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("BundleListDetailActivity", "Error fetching bundle", e);
                });

    }
*/

}
