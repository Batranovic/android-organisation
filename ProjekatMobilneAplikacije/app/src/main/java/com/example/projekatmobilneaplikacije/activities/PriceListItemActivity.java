package com.example.projekatmobilneaplikacije.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class PriceListItemActivity extends AppCompatActivity {

    TextView title;
    EditText priceEditText, discountEditText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String collectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_price_list_item);

        // Initialize views
        title = findViewById(R.id.title);
        priceEditText = findViewById(R.id.price);
        discountEditText = findViewById(R.id.discount);


        // Retrieve the product ID and item type from the intent
        String productId = getIntent().getStringExtra("productId");
        String serviceId = getIntent().getStringExtra("serviceId");
        String itemType = getIntent().getStringExtra("itemType");

        // Set collection name based on item type
        collectionName = itemType.equals("service") ? "services" : "products";


        // Set the product title to the EditText
        title.setText(getIntent().getStringExtra("title"));
        priceEditText.setText(String.valueOf(getIntent().getIntExtra("price", 0)));
        discountEditText.setText(String.valueOf(getIntent().getIntExtra("discount", 0)));

        ImageButton editButton = findViewById(R.id.editProductButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = itemType.equals("service") ? getIntent().getStringExtra("serviceId") : getIntent().getStringExtra("productId");
                editItem(id);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.price_list_item), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void editItem(String id) {
        int newPrice = Integer.parseInt(priceEditText.getText().toString());
        int newDiscount = Integer.parseInt(discountEditText.getText().toString());
        // Pronalaženje dokumenta sa odgovarajućim productId
        db.collection(collectionName)
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0); // Uzmemo prvi dokument (ako ima više, uzmemo prvi)
                                String documentId = document.getId();
                                Log.d("", "Document id: " + documentId);

                                // Mapa koja sadrži nove vrijednosti polja
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("price", newPrice);
                                updates.put("discount", newDiscount);

                                // Ažuriranje dokumenta s novim vrijednostima polja
                                db.collection(collectionName)
                                        .document(documentId)
                                        .update(updates)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(PriceListItemActivity.this, "Item  updated", Toast.LENGTH_SHORT).show();
                                                setResult(RESULT_OK);  // Indicate success
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PriceListItemActivity.this, "Failed to update tem", Toast.LENGTH_SHORT).show();
                                                Log.d("PriceListItemActivity", "Error updating item", e);
                                            }
                                        });
                            } else {
                                Log.d("PriceListItemActivity", "No document found with id: " + id);
                            }
                        } else {
                            Log.d("PriceListItemActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}