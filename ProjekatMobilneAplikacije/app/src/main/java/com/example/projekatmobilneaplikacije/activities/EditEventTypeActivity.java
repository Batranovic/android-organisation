package com.example.projekatmobilneaplikacije.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.EventType;
import com.example.projekatmobilneaplikacije.model.Subcategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditEventTypeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> selectedSubcategories = new ArrayList<>(); // Declare and initialize here
    private Map<String, Boolean> selectedSubcategoriesMap = new HashMap<>(); // Mapa za praćenje stanja selekcije potkategorija

    private TextView eventNameEditText;
    private EditText eventDescriptionEditText;

    private CheckBox activeCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event_type);


        eventNameEditText = findViewById(R.id.event_name);
        eventDescriptionEditText = findViewById(R.id.edit_event_description);
        activeCheckbox = findViewById(R.id.deactivate);

        String eventName = getIntent().getStringExtra("eventName");
        String eventDescription = getIntent().getStringExtra("eventDescription");

        eventNameEditText.setText(eventName);
        eventDescriptionEditText.setText(eventDescription);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventTypes")
                .whereEqualTo("name", eventName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            EventType eventType = documentSnapshot.toObject(EventType.class);
                            if(eventType != null){
                                Boolean isActive = eventType.isActive();
                                if(!isActive){
                                    activeCheckbox.setChecked(true);
                                }else {
                                    activeCheckbox.setChecked(false);
                                }
                            }
                        }
                    } else {
                    }
                });

        Button updateDescriptionButton = findViewById(R.id.buttonUpdateEvent);

        updateDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = !activeCheckbox.isChecked();
                String updatedDescription = eventDescriptionEditText.getText().toString();
                updateEventType(updatedDescription, eventName, isChecked);
            }
        });

    }

    private void updateEventType(String updatedDescription, String eventName, Boolean active){
        db.collection("eventTypes")
                .whereEqualTo("name", eventName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        DocumentReference documentRef = db.collection("eventTypes").document(document.getId());

                        documentRef.update("description", updatedDescription, "active",active)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Opis događaja je uspešno ažuriran.");
                                        Toast.makeText(EditEventTypeActivity.this, "Successfuly updated", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Greška prilikom ažuriranja opisa događaja", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Greška prilikom pretrage događaja", e);
                });
    }
    private void fetchSubcategories() {
        String eventName = getIntent().getStringExtra("eventName");

        db.collection("eventTypes")
                .whereEqualTo("name", eventName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<EventType> eventTypes = queryDocumentSnapshots.toObjects(EventType.class);
                    if (!eventTypes.isEmpty()) {
                        EventType eventType = eventTypes.get(0);
                        List<Subcategory> subcategories = eventType.getSubcategories();
                        List<String> productSubcategoryNames = new ArrayList<>();
                        List<String> serviceSubcategoryNames = new ArrayList<>();

                        for (Subcategory subcategory : subcategories) {
                            String name = subcategory.getName();
                            String type = subcategory.getSubcategoryType().toString();

                            if ("PRODUCT".equals(type)) {
                                productSubcategoryNames.add(name);
                            } else if ("SERVICE".equals(type)) {
                                serviceSubcategoryNames.add(name);
                            }
                        }

                        showSubcategoryNamesInBottomSheet(productSubcategoryNames, serviceSubcategoryNames);
                    }
                })
                .addOnFailureListener(e -> Log.d("Firestore", "Error getting documents: ", e));
    }

    private void showSubcategoryNamesInBottomSheet(List<String> productSubcategoryNames, List<String> serviceSubcategoryNames) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditEventTypeActivity.this);
        bottomSheetDialog.setContentView(R.layout.service_and_product_sheet);

        LinearLayout serviceSection = bottomSheetDialog.findViewById(R.id.service_section);
        LinearLayout productSection = bottomSheetDialog.findViewById(R.id.product_section);

        TextView serviceHeader = new TextView(EditEventTypeActivity.this);
        serviceHeader.setText("SERVICE");
        serviceSection.addView(serviceHeader);

        TextView productHeader = new TextView(EditEventTypeActivity.this);
        productHeader.setText("PRODUCT");
        productSection.addView(productHeader);

        db.collection("subcategories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Subcategory> allSubcategories = queryDocumentSnapshots.toObjects(Subcategory.class);

                    // Razvrstavanje podkategorija u odgovarajuće sekcije
                    for (Subcategory subcategory : allSubcategories) {
                        String name = subcategory.getName();
                        String type = subcategory.getSubcategoryType().toString();

                        CheckBox checkBox = new CheckBox(EditEventTypeActivity.this);
                        checkBox.setText(name);

                        // Postavljanje stanja selekcije potkategorije u mapu
                        boolean isSelected = (type.equals("PRODUCT") && productSubcategoryNames.contains(name)) ||
                                (type.equals("SERVICE") && serviceSubcategoryNames.contains(name));
                        selectedSubcategoriesMap.put(name, isSelected);

                        // Dodavanje slušača događaja za ažuriranje mape prilikom promene stanja ček-boksa
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                selectedSubcategoriesMap.put(name, isChecked);
                            }
                        });

                        if ("PRODUCT".equals(type)) {
                            productSection.addView(checkBox);
                        } else if ("SERVICE".equals(type)) {
                            serviceSection.addView(checkBox);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d("Firestore", "Error getting documents: ", e));

        bottomSheetDialog.show();
    }


}







