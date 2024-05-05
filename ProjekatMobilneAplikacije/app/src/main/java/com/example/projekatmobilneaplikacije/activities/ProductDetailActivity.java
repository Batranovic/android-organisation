package com.example.projekatmobilneaplikacije.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    EditText titleEditText, descriptionEditText, subcategoryEditText, eventTypeEditText, priceEditText, availabilityEditText, visibilityEditText;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        // Initialize views
        titleEditText = findViewById(R.id.title);
        descriptionEditText = findViewById(R.id.description);
        subcategoryEditText = findViewById(R.id.subcategory);
        eventTypeEditText = findViewById(R.id.eventType);
        priceEditText = findViewById(R.id.price);
        availabilityEditText = findViewById(R.id.availability);
        visibilityEditText = findViewById(R.id.visibility);

        // Retrieve the product ID from the intent
        String productId = getIntent().getStringExtra("productId");

        // Set the product title to the EditText
        titleEditText.setText(getIntent().getStringExtra("title"));
        descriptionEditText.setText(getIntent().getStringExtra("description"));
        subcategoryEditText.setText(getIntent().getStringExtra("subcategory"));
        eventTypeEditText.setText(getIntent().getStringExtra("eventType"));
        priceEditText.setText(String.valueOf(getIntent().getIntExtra("price", 0)));
        availabilityEditText.setText(getIntent().getStringExtra("availability"));
        visibilityEditText.setText(getIntent().getStringExtra("visibility"));


        ImageButton deleteButton = findViewById(R.id.deleteProductButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productId = getIntent().getStringExtra("productId");
                Log.d("ProductDetailActivity","ProductId:" + productId);
                deleteProduct(productId);
            }
        });


        ImageButton editButton = findViewById(R.id.editProductButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productId = getIntent().getStringExtra("productId");
                Log.d("ProductDetailActivity","ProductId:" + productId);
                editProduct(productId);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.productDetail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void deleteProduct(String productId) {
        // Pronalaženje dokumenta sa odgovarajućim productId
        db.collection("products")
                .whereEqualTo("id", productId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Pronađen je bar jedan dokument sa datim productId
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0); // Uzmemo prvi dokument (ako ima više, uzmemo prvi)
                                String documentId = document.getId();
                                Log.d("","Document id: " + documentId);

                                // Ažuriranje polja isDeleted na true
                                db.collection("products")
                                        .document(documentId)
                                        .update("isDeleted", true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ProductDetailActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                                                finish(); // Zatvorite aktivnost nakon brisanja
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ProductDetailActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                                                Log.d("ProductDetailActivity", "Error deleting product", e);
                                            }
                                        });
                            } else {
                                Log.d("ProductDetailActivity", "No document found with productId: " + productId);
                            }
                        } else {
                            Log.d("ProductDetailActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


   
}