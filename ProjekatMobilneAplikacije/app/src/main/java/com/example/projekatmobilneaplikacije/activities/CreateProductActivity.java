package com.example.projekatmobilneaplikacije.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateProductActivity extends AppCompatActivity {

    EditText title, description;
    Spinner spinner, spinnerSubcategory;
    String available, visible;
    String eventType = "";
    Button btnSubmit;

    boolean isPrivateEventChecked = false;
    boolean isCorporateEventChecked = false;
    boolean isEducationEventChecked = false;
    boolean isSportEventChecked = false;
    boolean isCulturalEventChecked = false;
    boolean isHumanitarianEventChecked = false;

    private final int GALLERY_REQ_CODE = 1000;
    ImageView imgGallery;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        imgGallery = findViewById(R.id.imgGallery);
        ImageButton btnGallery = findViewById(R.id.btnCamera);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });


        Button btnAddEventTypes = findViewById(R.id.btnAddEventTypes);
        btnAddEventTypes.setOnClickListener(v -> {
            Log.i("ShopApp", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_event_types, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();

            CheckBox privateEventCheckbox = dialogView.findViewById(R.id.privateEventCheckbox);
            privateEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isPrivateEventChecked = isChecked;
            });

            CheckBox corporateEventCheckbox = dialogView.findViewById(R.id.corporateEventCheckbox);
            corporateEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isCorporateEventChecked = isChecked;
            });

            CheckBox educationEventCheckbox = dialogView.findViewById(R.id.educationEventCheckbox);
            educationEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isEducationEventChecked = isChecked;
            });

            CheckBox culturalEventCheckbox = dialogView.findViewById(R.id.culturalEventCheckbox);
            culturalEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isCulturalEventChecked = isChecked;
            });

            CheckBox sportEventCheckbox = dialogView.findViewById(R.id.sportEventCheckbox);
            sportEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isSportEventChecked = isChecked;
            });

            CheckBox humanitarianEventCheckbox = dialogView.findViewById(R.id.humanitarianEventCheckbox);
            humanitarianEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isHumanitarianEventChecked = isChecked;
            });



        });

        ImageButton addNewSubcategoryButton = findViewById(R.id.addNewSubcategoryButton);
        addNewSubcategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pozovi funkciju za prikazivanje dijaloga kada se klikne dugme
                openDialog();
            }
        });

        spinner = findViewById(R.id.btnCategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.category_list));
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinnerSubcategory = findViewById(R.id.btnSubcategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> arrayAdapterSubcategory = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.subcategory_list));
        // Specify the layout to use when the list of choices appears
        arrayAdapterSubcategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubcategory.setAdapter(arrayAdapterSubcategory);

        SeekBar priceSeekBar = findViewById(R.id.priceSeekBar);
        TextView priceText = findViewById(R.id.textViewPrice);

        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priceText.setVisibility(View.VISIBLE);
                priceText.setText(progress+"/100");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //kreiranje proizvoda
        title = findViewById(R.id.title);
        description = findViewById(R.id.editDescription);



        btnSubmit = findViewById(R.id.btnSubmit);

        db = FirebaseFirestore.getInstance();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = title.getText().toString();
                String Description = description.getText().toString();
                String Category = spinner.getSelectedItem().toString();
                String Subcategory = spinnerSubcategory.getSelectedItem().toString();
                int Price = priceSeekBar.getProgress();

                RadioGroup radioGroupAvailability = findViewById(R.id.availability); // Postavite pravilan ID za RadioGroup
                int selectedRadioButtonId = radioGroupAvailability.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    String selectedText = selectedRadioButton.getText().toString();
                    available = selectedText;
                } else {
                    // Nijedan RadioButton nije odabran
                    available = "";
                }

                RadioGroup radioGroupVisibility = findViewById(R.id.visibility); // Postavite pravilan ID za RadioGroup
                int selectedRadioVisibilityButtonId = radioGroupVisibility.getCheckedRadioButtonId();

                if (selectedRadioVisibilityButtonId != -1) {
                    RadioButton selectedRadioVisibilityButton = findViewById(selectedRadioVisibilityButtonId);
                    String selectedText = selectedRadioVisibilityButton.getText().toString();
                    visible = selectedText;
                } else {
                    visible = "";
                }

                String Available = available;
                String Visible = visible;




                String EventType = "";
                if (isPrivateEventChecked) {
                    EventType += getString(R.string.privateEvent) + ", ";
                }
                if (isCorporateEventChecked) {
                    EventType += getString(R.string.corporateEvent) + ", ";
                }
                if (isEducationEventChecked) {
                    EventType += getString(R.string.educationEvent) + ", ";
                }
                if (isCulturalEventChecked) {
                    EventType += getString(R.string.culturalEvent) + ", ";
                }
                if (isSportEventChecked) {
                    EventType += getString(R.string.sportEvent) + ", ";
                }
                if (isHumanitarianEventChecked) {
                    EventType += getString(R.string.humanitarianEvent) + ", ";
                }


                // Uklonite poslednji zarez iz eventType ako je potrebno
                if (!EventType.isEmpty() && EventType.endsWith(", ")) {
                    EventType = EventType.substring(0, EventType.length() - 2);
                }



                Map<String,Object> product = new HashMap<>();
                product.put("Title", Title);
                product.put("Description", Description);
                product.put("Category", Category);
                product.put("Subcategory", Subcategory);
                product.put("Price", Price);
                product.put("Available", Available);
                product.put("Visible", Visible);
                product.put("Event type", EventType);

                db.collection("products")
                        .add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("Tag", "Uspesno");
                                Toast.makeText(CreateProductActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Tag", "Neuspesno");
                                Toast.makeText(CreateProductActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unesite novu podkategoriju");

        // Postavljanje layouta unutar dijaloga
        View dialogView = getLayoutInflater().inflate(R.layout.subcategory_dialog, null);
        builder.setView(dialogView);

        EditText editText = dialogView.findViewById(R.id.editText);

        builder.setPositiveButton("Potvrdi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ovdje možete obraditi uneseni tekst iz editTexta
                String newSubcategory = editText.getText().toString();
                // Primjer: Prikazivanje unesenog teksta u Logcatu
                Log.d("Novi tekst:", newSubcategory);
            }
        });

        builder.setNegativeButton("Otkaži", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    //za sliku
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQ_CODE){
                //FOR GALLERY
                imgGallery.setImageURI(data.getData());

            }
        }
    }
}