package com.example.projekatmobilneaplikacije.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditServiceAndProductActivity extends AppCompatActivity {

    private EditText updateName;
    private EditText updateDesc;
    String oldName, categoryName, categoryDescription;
    private DocumentReference categoryRef; // Document reference for the category

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_and_product);

        // Inicijalizacija EditText polja
        updateName = findViewById(R.id.updateName);
        updateDesc = findViewById(R.id.updateDesc);

        Intent intent = getIntent();
        if (intent != null) {
            oldName = intent.getStringExtra("old_name");
            categoryName = intent.getStringExtra("category_name");
            categoryDescription = intent.getStringExtra("category_description");
            String subcategoryName = intent.getStringExtra("subcategory_name");
            String subcategoryDescription = intent.getStringExtra("subcategory_description");

            if (subcategoryName != null && subcategoryDescription != null) {
                updateName.setText(subcategoryName);
                updateDesc.setText(subcategoryDescription);
            } else if (categoryName != null && categoryDescription != null) {
                updateName.setText(categoryName);
                updateDesc.setText(categoryDescription);
            }

            // Ovde postavljamo vrednost oldName polja
            if (oldName != null) {
                updateName.setTag(oldName);
            }
        }

        Button buttonCreateService = findViewById(R.id.buttonCreateService);
        buttonCreateService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pozivanje metode za ažuriranje podataka
                String updatedName = updateName.getText().toString();
                String updatedDesc = updateDesc.getText().toString();

                // Pozivanje metode za ažuriranje podataka
                updateCategory(oldName, updatedName, updatedDesc);
            }
        });

        Button buttonDeleteCategory = findViewById(R.id.buttonDeleteService);
        buttonDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory(oldName);
            }
        });
    }


    private void updateCategory(String initialName, String categoryName, String categoryDescription) {


        if (categoryName.isEmpty() || categoryDescription.isEmpty()) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories")
                .whereEqualTo("name", initialName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            DocumentReference docRef = documentSnapshot.getReference();
                            docRef.update("name", categoryName, "description", categoryDescription)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(EditServiceAndProductActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditServiceAndProductActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(EditServiceAndProductActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Dokument sa inicijalnim imenom nije pronađen
                        Toast.makeText(EditServiceAndProductActivity.this, "Category not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Greška prilikom pretrage dokumenta
                    Toast.makeText(EditServiceAndProductActivity.this, "Failed to search category", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteCategory(String categoryNameToDelete) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories")
                .whereEqualTo("name", categoryNameToDelete)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Pronađen je dokument sa datim imenom
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            DocumentReference docRef = documentSnapshot.getReference();
                            docRef.delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Uspešno brisanje dokumenta
                                        Toast.makeText(EditServiceAndProductActivity.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                                        // Opciono, možete se vratiti na prethodnu aktivnost nakon brisanja
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Greška prilikom brisanja dokumenta
                                        Toast.makeText(EditServiceAndProductActivity.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Dokument sa datim imenom nije pronađen
                        Toast.makeText(EditServiceAndProductActivity.this, "Category not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Greška prilikom pretrage dokumenta
                    Toast.makeText(EditServiceAndProductActivity.this, "Failed to search category", Toast.LENGTH_SHORT).show();
                });
    }

}
