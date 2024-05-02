package com.example.projekatmobilneaplikacije.fragments.serviceAndProduct;

import static android.content.ContentValues.TAG;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.fragments.registration.RegistrationFragment;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.EventOrganizer;
import com.example.projekatmobilneaplikacije.model.Subcategory;
import com.example.projekatmobilneaplikacije.model.enumerations.SubcategoryType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ServiceAndProductFragment extends Fragment {

    private RadioGroup radioGroupCategory;
    private RadioButton radioButtonSubcategory;
    private Spinner categorySpinner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static ServiceAndProductFragment newInstance() {
        return new ServiceAndProductFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service_and_product, container, false);

        radioGroupCategory = rootView.findViewById(R.id.radioGroupCategory);
        radioButtonSubcategory = rootView.findViewById(R.id.sub_category);
        categorySpinner = rootView.findViewById(R.id.categorySpinner);



        Button buttonCreate = rootView.findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dobijanje unetih podataka iz EditText polja
                String categoryName = ((EditText) rootView.findViewById(R.id.cat_name)).getText().toString();
                String categoryDescription = ((EditText) rootView.findViewById(R.id.cat_desc)).getText().toString();
                String selectedCategoryName = categorySpinner.getSelectedItem().toString(); // Dobijanje imena odabrane kategorije

                // Provera da li je odabrana kategorija ili potkategorija
                boolean isCategorySelected = radioGroupCategory.getCheckedRadioButtonId() == R.id.category;
                boolean isServiceSelected = ((RadioButton) rootView.findViewById(R.id.service)).isChecked();

                // Provera da li su sva polja uneta
                if (categoryName.isEmpty() || categoryDescription.isEmpty()) {
                    // Ako nisu sva polja uneta, prikaži poruku korisniku
                    if(!isCategorySelected){
                        if(selectedCategoryName.isEmpty())
                            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kreiranje nove kategorije ili potkategorije na osnovu izbora korisnika
                if (isCategorySelected) {
                    // Kreiranje nove kategorije
                    Category category = new Category(categoryName, categoryDescription);
                    // Dodavanje kategorije u bazu podataka ili neki lokalni repozitorijum
                    // Na primer, možete koristiti Firebase Firestore za čuvanje podataka
                    // Nakon toga, možete obavestiti korisnika o uspešnom kreiranju kategorije
                    addCategoryToFirestore(category);
                    Toast.makeText(getContext(), "Category created successfully.", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(ServiceAndProductFragment.this)
                            .navigate(R.id.action_nav_service_to_nav_service_product);
                } else {
                    findCategoryByName(selectedCategoryName, new CategoryCallback() {
                        @Override
                        public void onCategoryFound(Category selectedCategory) {
                            SubcategoryType subcategoryType = isServiceSelected ? SubcategoryType.SERVICE : SubcategoryType.PRODUCT;
                            Subcategory subcategory = new Subcategory(categoryName, categoryDescription, subcategoryType, selectedCategory);
                            // Dodavanje potkategorije u bazu podataka
                            addSubcategoryToFirestore(subcategory);
                            Toast.makeText(getContext(), "Subcategory created successfully.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCategoryNotFound() {
                            // If selected category is null, show a message to the user
                            Toast.makeText(getContext(), "Please select a valid category.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                // Resetovanje EditText polja nakon uspešnog kreiranja kategorije ili potkategorije
                ((EditText) rootView.findViewById(R.id.cat_name)).setText("");
                ((EditText) rootView.findViewById(R.id.cat_desc)).setText("");
                // Resetovanje Radio Button-a na podrazumevanu vrednost
                ((RadioButton) rootView.findViewById(R.id.category)).setChecked(true);
                ((RadioButton) rootView.findViewById(R.id.service)).setChecked(true);
                // Omogući ponovno klikanje na Radio Button-e ako su bili onemogućeni
                rootView.findViewById(R.id.service).setEnabled(true);
                rootView.findViewById(R.id.product).setEnabled(true);
            }
        });


        radioGroupCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isCategorySelected = checkedId == R.id.category;

                if (isCategorySelected) {
                    rootView.findViewById(R.id.service).setEnabled(false);
                    rootView.findViewById(R.id.product).setEnabled(false);
                    setRadioButtonColor(rootView.findViewById(R.id.service), Color.RED); // Postavi boju radiobuttona na crvenu
                    setRadioButtonColor(rootView.findViewById(R.id.product), Color.RED); // Postavi boju radiobuttona na crvenu
                } else {
                    rootView.findViewById(R.id.service).setEnabled(true);
                    rootView.findViewById(R.id.product).setEnabled(true);
                    setRadioButtonColor(rootView.findViewById(R.id.service), Color.BLACK); // Vrati boju radiobuttona na podrazumevanu vrednost (crnu)
                    setRadioButtonColor(rootView.findViewById(R.id.product), Color.BLACK); // Vrati boju radiobuttona na podrazumevanu vrednost (crnu)
                }

            }
        });

        // Postavljanje slušača događaja na RadioButton "Subcategory"
        radioButtonSubcategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Omogući ili onemogući unos u editTextCatName2 ovisno o označenosti "Subcategory"
                categorySpinner.setEnabled(isChecked);
            }
        });
        getAllCategoriesFromFirestore();
        return rootView;
    }

    // Metoda za postavljanje boje radiobuttona
    private void setRadioButtonColor(RadioButton radioButton, int color) {
        ColorStateList colorStateList = new ColorStateList(new int[][]{{-android.R.attr.state_enabled}, {}},
                new int[]{color, radioButton.getCurrentTextColor()});
        radioButton.setButtonTintList(colorStateList);
    }

    private void addCategoryToFirestore(Category category) {
        // Add your logic to add eventOrganizer to Firestore
        db.collection("categories")
                .add(category)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        // Handle success, if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        // Handle failure, if needed
                    }
                });
    }
    private void addSubcategoryToFirestore(Subcategory subcategory) {
        // Add your logic to add eventOrganizer to Firestore
        db.collection("subcategories")
                .add(subcategory)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        // Handle success, if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        // Handle failure, if needed
                    }
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

                        // Update the Spinner's adapter with category names
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySpinner.setAdapter(spinnerAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting documents.", e);
                    }
                });
    }




    private void findCategoryByName(String categoryName, CategoryCallback callback) {
        db.collection("categories")
                .whereEqualTo("name", categoryName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            Category category = documentSnapshot.toObject(Category.class);
                            // Here, you have the entire category object
                            // You can pass it back through the callback
                            callback.onCategoryFound(category);
                        } else {
                            // Handle the case where the category with the provided name is not found
                            callback.onCategoryNotFound();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting documents.", e);
                        callback.onCategoryNotFound();
                    }
                });
    }

    public interface CategoryCallback {
        void onCategoryFound(Category selectedCategory);
        void onCategoryNotFound();
    }

}
