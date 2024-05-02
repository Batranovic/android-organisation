package com.example.projekatmobilneaplikacije.activities;
import com.example.projekatmobilneaplikacije.model.Category;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditSubcategoryActivity extends AppCompatActivity {

    private EditText editSubname;
    private EditText editSubdesc;
    private RadioGroup radioGroupType;
    private Spinner categorySpinner;
    private Button buttonSaveSubcategory;
    private Button buttonDeleteSubCategory;
    private FirebaseFirestore db;
    private ArrayAdapter<String> spinnerAdapter;
    String oldName, subcategoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subcategory);
        db = FirebaseFirestore.getInstance();
        // Inicijalizacija polja
        editSubname = findViewById(R.id.edit_subname);
        editSubdesc = findViewById(R.id.edit_subdesc);
        radioGroupType = findViewById(R.id.radioGroupType);
        categorySpinner = findViewById(R.id.categorySpinner);
        buttonSaveSubcategory = findViewById(R.id.buttonSaveSubcategory);
        buttonDeleteSubCategory = findViewById(R.id.buttonDeleteSubCategory);

        // Dodajte ovdje poziv metode za dohvaćanje kategorija
        getAllCategoriesFromFirestore();

        // Dobavljanje podataka iz Intent-a
        Intent intent = getIntent();
        if (intent != null) {
            String subcategoryName = intent.getStringExtra("subcategory_name");
            String subcategoryDescription = intent.getStringExtra("subcategory_description");
            subcategoryType = intent.getStringExtra("subcategory_type");
            String subcategoryCategoryName = intent.getStringExtra("subcategory_category_name");
            oldName = intent.getStringExtra("old_subname");


            // Postavljanje vrijednosti u EditText polja
            editSubname.setText(subcategoryName);
            editSubdesc.setText(subcategoryDescription);

            // Postavljanje odabira tipa podkategorije u RadioGroup
            if (subcategoryType.equals("SERVICE")) {
                radioGroupType.check(R.id.service);
            } else if (subcategoryType.equals("PRODUCT")) {
                radioGroupType.check(R.id.product);
            }

            // Postavljanje odabrane kategorije u Spinner
            if (spinnerAdapter != null) {
                int spinnerPosition = spinnerAdapter.getPosition(subcategoryCategoryName);
                categorySpinner.setSelection(spinnerPosition);
            }
        }

        // Dodavanje slušača događaja na dugmad
        buttonSaveSubcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = editSubname.getText().toString();
                String updatedDesc = editSubdesc.getText().toString();
                int selectedRadioButtonId = radioGroupType.getCheckedRadioButtonId();

                if (selectedRadioButtonId == R.id.service) {
                    subcategoryType = "SERVICE";
                } else if (selectedRadioButtonId == R.id.product) {
                    subcategoryType = "PRODUCT";
                }


                String selectedCategory = categorySpinner.getSelectedItem().toString();

                // Pozivanje metode za ažuriranje podataka
                updateSubcategory(oldName, updatedName, updatedDesc, subcategoryType, selectedCategory);
            }
        });
        buttonDeleteSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSubcategory(oldName);
            }
        });
    }
    private void updateSubcategory(String initialName, String subcategoryName, String subcategoryDescription, String subcategoryType, String selectedCategoryName) {
        // Provera da li su unesena prazna polja
        if (subcategoryName.isEmpty() || subcategoryDescription.isEmpty() || subcategoryType.isEmpty() || selectedCategoryName.isEmpty()) {
            Toast.makeText(EditSubcategoryActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();

            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories")
                .whereEqualTo("name", selectedCategoryName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot categorySnapshot : queryDocumentSnapshots.getDocuments()) {
                            Category selectedCategory = categorySnapshot.toObject(Category.class);
                            updateSubcategoryWithCategory(initialName, subcategoryName, subcategoryDescription, subcategoryType, selectedCategory);
                            return;
                        }
                    } else {

                        Toast.makeText(EditSubcategoryActivity.this, "Category not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditSubcategoryActivity.this, "Failed to search category", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateSubcategoryWithCategory(String initialName, String subcategoryName, String subcategoryDescription, String subcategoryType, Category selectedCategory) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("subcategories")
                .whereEqualTo("name", initialName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            DocumentReference docRef = documentSnapshot.getReference();
                            docRef.update("name", subcategoryName, "description", subcategoryDescription, "subcategoryType", subcategoryType, "category", selectedCategory)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(EditSubcategoryActivity.this, "Subcategory updated successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(EditSubcategoryActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(EditSubcategoryActivity.this, "Failed to update subcategory", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(EditSubcategoryActivity.this, "Subcategory not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditSubcategoryActivity.this, "Failed to search subcategory", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteSubcategory(String subcategoryNameToDelete) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("subcategories")
                .whereEqualTo("name", subcategoryNameToDelete)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            DocumentReference docRef = documentSnapshot.getReference();
                            docRef.delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(EditSubcategoryActivity.this, "Subcategory deleted successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(EditSubcategoryActivity.this, "Failed to delete subcategory", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(EditSubcategoryActivity.this, "Subategory not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditSubcategoryActivity.this, "Failed to search subcategory", Toast.LENGTH_SHORT).show();
                });
    }


    private void getAllCategoriesFromFirestore() {
        db.collection("categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> categoryNames = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Dohvatite samo naziv kategorije iz dokumenta
                            String categoryName = documentSnapshot.getString("name");
                            categoryNames.add(categoryName);
                        }

                        // Postavite adapter za Spinner sa dohvaćenim imenima kategorija
                        spinnerAdapter = new ArrayAdapter<>(EditSubcategoryActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySpinner.setAdapter(spinnerAdapter);

                        // Dobavljanje podataka iz Intent-a
                        Bundle extras = getIntent().getExtras();
                        if (extras != null) {
                            String subcategoryCategoryName = extras.getString("subcategory_category_name");
                            if (subcategoryCategoryName != null) {
                                int position = categoryNames.indexOf(subcategoryCategoryName);
                                if (position != -1) {
                                    categorySpinner.setSelection(position);
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // U slučaju neuspjeha dohvaćanja kategorija, ovdje možete postaviti odgovarajuću poruku ili radnju
                    }
                });
    }

}
