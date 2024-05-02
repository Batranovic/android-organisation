package com.example.projekatmobilneaplikacije.fragments.events;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentEventTypeBinding;
import com.example.projekatmobilneaplikacije.model.EventType;
import com.example.projekatmobilneaplikacije.model.Subcategory;
import com.example.projekatmobilneaplikacije.model.enumerations.SubcategoryType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EventTypeFragment extends Fragment {
    private FragmentEventTypeBinding binding;
    private boolean isFirstSelection = true;
    private Spinner spinner;
    private RadioGroup radioGroup;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Subcategory> selectedSubcategories = new ArrayList<>();

    public static EventTypeFragment newInstance() {
        return new EventTypeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEventTypeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton selectSubcategory = root.findViewById(R.id.select_subcategory);
        selectSubcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ShopApp", "Bottom Sheet Dialog");

                // Dobavljanje liste imena subkategorija iz baze podataka
                fetchSubcategories();
            }
        });
        EditText eventNameEditText = root.findViewById(R.id.event_name);
        EditText eventDescriptionEditText = root.findViewById(R.id.event_description);

        Button buttonCreateEvent = root.findViewById(R.id.buttonCreateEvent);
        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigacija ka ServiceAndProductPageFragment
                saveEventDataToFirestore(eventNameEditText, eventDescriptionEditText);

                NavHostFragment.findNavController(EventTypeFragment.this)
                        .navigate(R.id.action_nav_event_type_create_to_nav_event_type_page);
            }
        });
        return root;
    }

    private void saveEventDataToFirestore(EditText eventNameEditText, EditText eventDescriptionEditText) {
        String eventName = eventNameEditText.getText().toString();
        String eventDescription = eventDescriptionEditText.getText().toString();

        EventType eventType = new EventType();
        eventType.setName(eventName);
        eventType.setDescription(eventDescription);
        eventType.setActive(true);
        eventType.setSubcategories(selectedSubcategories); // Postavljanje odabranih subkategorija

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventTypes")
                .add(eventType)
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setRadioButtonColor(RadioButton radioButton, int color) {
        ColorStateList colorStateList = new ColorStateList(new int[][]{{-android.R.attr.state_enabled}, {}},
                new int[]{color, radioButton.getCurrentTextColor()});
        radioButton.setButtonTintList(colorStateList);
    }

    private void fetchSubcategories() {
        db.collection("subcategories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Ovdje rukujte s uspjehom
                        List<String> serviceSubcategoryNames = new ArrayList<>();
                        List<String> productSubcategoryNames = new ArrayList<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String id = document.getId(); // Dohvati ID dokumenta
                            String name = document.getString("name");
                            String type = document.getString("subcategoryType");

                            if ("SERVICE".equals(type)) {
                                serviceSubcategoryNames.add(name);
                            } else if ("PRODUCT".equals(type)) {
                                productSubcategoryNames.add(name);
                            }
                        }

                        showSubcategoryNamesInBottomSheet(serviceSubcategoryNames, productSubcategoryNames);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ovdje rukujte s greškom ako se dogodi
                        Log.d("Firestore", "Error getting documents: ", e);
                    }
                });
    }

    private void showSubcategoryNamesInBottomSheet(List<String> serviceSubcategoryNames, List<String> productSubcategoryNames) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
        View dialogView = getLayoutInflater().inflate(R.layout.service_and_product_sheet, null);
        bottomSheetDialog.setContentView(dialogView);

        LinearLayout serviceSection = dialogView.findViewById(R.id.service_section);
        LinearLayout productSection = dialogView.findViewById(R.id.product_section);

        TextView serviceLabel = new TextView(getContext());
        serviceLabel.setText("Services");
        serviceSection.addView(serviceLabel);

        for (String name : serviceSubcategoryNames) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(name);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        fetchSubcategoryByName(name, SubcategoryType.SERVICE);
                    } else {
                        // Uklonite odabranu subkategoriju iz liste
                        removeSubcategoryByName(name);
                    }
                }
            });
            serviceSection.addView(checkBox);
        }

        TextView productLabel = new TextView(getContext());
        productLabel.setText("Products");
        productSection.addView(productLabel);

        for (String name : productSubcategoryNames) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(name);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        fetchSubcategoryByName(name, SubcategoryType.PRODUCT);
                    } else {
                        // Uklonite odabranu subkategoriju iz liste
                        removeSubcategoryByName(name);
                    }
                }
            });
            productSection.addView(checkBox);
        }

        bottomSheetDialog.show();
    }

    private void fetchSubcategoryByName(String name, SubcategoryType type) {
        db.collection("subcategories")
                .whereEqualTo("name", name)
                .whereEqualTo("subcategoryType", type)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Subcategory subcategory = document.toObject(Subcategory.class);
                                selectedSubcategories.add(subcategory);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Error getting documents: ", e);
                    }
                });
    }

    private void removeSubcategoryByName(String name) {
        for (Subcategory subcategory : selectedSubcategories) {
            if (subcategory.getName().equals(name)) {
                selectedSubcategories.remove(subcategory);
                break; // Prekidamo petlju kada nađemo i uklonimo odabranu podkategoriju
            }
        }
    }

}
