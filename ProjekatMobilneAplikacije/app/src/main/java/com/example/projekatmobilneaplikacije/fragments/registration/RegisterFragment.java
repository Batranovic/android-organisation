package com.example.projekatmobilneaplikacije.fragments.registration;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.HomeActivity;
import com.example.projekatmobilneaplikacije.databinding.FragmentRegisterBinding;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.Company;
import com.example.projekatmobilneaplikacije.model.DayWorkingHours;
import com.example.projekatmobilneaplikacije.model.EventType;
import com.example.projekatmobilneaplikacije.model.UserDetails;
import com.example.projekatmobilneaplikacije.model.enumerations.Owner;
import com.example.projekatmobilneaplikacije.model.enumerations.UserRole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.example.projekatmobilneaplikacije.model.WorkingHours;

public class RegisterFragment extends Fragment  {
    EditText companyEmail, companyName, companyAddress, companyDescription, companyPhoneNumber;
    private String email, password, name, surname, address, phone;
    private TextView mondayDayTextView, tuesdayDayTextView, wednesdayDayTextView, thursdayDayTextView, fridayDayTextView, saturdayDayTextView, sundayDayTextView;
    private TimePicker mondayStartClock, mondayEndClock, tuesdayStartClock, tuesdayEndClock, wednesdayStartClock, wednesdayEndClock, thursdayStartClock, thursdayEndClock, fridayStartClock, fridayEndClock, saturdayStartClock, saturdayEndClock, sundayStartClock, sundayEndClock;
    private Button saveButton;
    private FragmentRegisterBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private List<Category> selectedCategories = new ArrayList<>();

    private List<EventType> selectedEventTypes = new ArrayList<>();
    private WorkingHours workingHours;
    private static final int REQUEST_CODE_TIME_PICKER = 1001;



    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle args = getArguments();

        if (args != null && args.containsKey("working_hours")) {
            // Postavljanje radnih sati u RegisterFragment
            workingHours = (WorkingHours) args.getSerializable("working_hours");
            // Ovdje možete koristiti radne sate kako želite, npr. prikazati ih na odgovarajući način
        }
        companyEmail = root.findViewById(R.id.company_email);
        companyName = root.findViewById(R.id.company_name);
        companyAddress = root.findViewById(R.id.company_address);
        companyDescription = root.findViewById(R.id.company_description);
        companyPhoneNumber = root.findViewById(R.id.company_phone);
        //companyPhoto = root.findViewById(R.id.phone);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getArguments();
        if (bundle != null) {
             email = bundle.getString("email");
             password = bundle.getString("password");
             name = bundle.getString("name");
             surname = bundle.getString("surname");
             address = bundle.getString("address");
             phone = bundle.getString("phone");
        }


        mondayDayTextView = root.findViewById(R.id.monday_day);
        tuesdayDayTextView = root.findViewById(R.id.tuesday_day);
        wednesdayDayTextView = root.findViewById(R.id.wednesday_day);
        thursdayDayTextView = root.findViewById(R.id.thursday_day);
        fridayDayTextView = root.findViewById(R.id.friday_day);
        saturdayDayTextView = root.findViewById(R.id.saturday_day);
        sundayDayTextView = root.findViewById(R.id.sunday_day);

        mondayStartClock = root.findViewById(R.id.monday_start_clock);
        mondayEndClock = root.findViewById(R.id.monday_end_clock);
        tuesdayStartClock = root.findViewById(R.id.tuesday_start_clock);
        tuesdayEndClock = root.findViewById(R.id.tuesday_end_clock);
        wednesdayStartClock = root.findViewById(R.id.wednesday_start_clock);
        wednesdayEndClock = root.findViewById(R.id.wednesday_end_clock);
        thursdayStartClock = root.findViewById(R.id.thursday_start_clock);
        thursdayEndClock = root.findViewById(R.id.thursday_end_clock);
        fridayStartClock = root.findViewById(R.id.friday_start_clock);
        fridayEndClock = root.findViewById(R.id.friday_end_clock);
        saturdayStartClock = root.findViewById(R.id.saturday_start_clock);
        saturdayEndClock = root.findViewById(R.id.saturday_end_clock);
        sundayStartClock = root.findViewById(R.id.sunday_start_clock);
        sundayEndClock = root.findViewById(R.id.sunday_end_clock);
        saveButton = root.findViewById(R.id.button_ok);


        FloatingActionButton selectCategory = root.findViewById(R.id.select_category);
        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectCategory.setOnClickListener(s -> {
                    fetchCategories();
                });

            }

        });

        Button registerButtons = root.findViewById(R.id.registerButtons);
        registerButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mondayHour = mondayStartClock.getHour();
                int mondayMinute = mondayStartClock.getMinute();
                Time mondayStartTime = new Time(mondayHour, mondayMinute, 0);
                int mondayEndHour = mondayEndClock.getHour();
                int mondayEndMinute = mondayEndClock.getMinute();
                Time mondayEndTime = new Time(mondayEndHour, mondayEndMinute, 0);
                int tuesdayHour = tuesdayStartClock.getHour();
                int tuesdayMinute = tuesdayStartClock.getMinute();
                Time tuesdayStartTime = new Time(tuesdayHour, tuesdayMinute, 0);
                int tuesdayEndHour = tuesdayEndClock.getHour();
                int tuesdayEndMinute = tuesdayEndClock.getMinute();
                Time tuesdayEndTime = new Time(tuesdayEndHour, tuesdayEndMinute, 0);

                int wednesdayHour = wednesdayStartClock.getHour();
                int wednesdaMinute = wednesdayStartClock.getMinute();
                Time wednesdayStartTime = new Time(wednesdayHour, wednesdaMinute, 0);
                int wednsedayEndHour = wednesdayEndClock.getHour();
                int wednesdayEndMinute = wednesdayEndClock.getMinute();
                Time wednesdayEndTime = new Time(wednsedayEndHour, wednesdayEndMinute, 0);
                int thursdayHour = thursdayStartClock.getHour();
                int thursdayMinute = thursdayStartClock.getMinute();
                Time thursdayStartTime = new Time(thursdayHour, thursdayMinute, 0);
                int thursdayEndHour = thursdayEndClock.getHour();
                int thursdayEndMinute = thursdayEndClock.getMinute();
                Time thursdayEndTime = new Time(thursdayEndHour, thursdayEndMinute, 0);

                int fridayHour = fridayStartClock.getHour();
                int fridayMinute = fridayStartClock.getMinute();
                Time fridayStartTime = new Time(fridayHour, fridayMinute, 0);
                int fridayEndHour = fridayEndClock.getHour();
                int fridayEndMinute = fridayEndClock.getMinute();
                Time fridayEndTime = new Time(fridayEndHour, fridayEndMinute, 0);

                int saturdayHour = saturdayStartClock.getHour();
                int saturdayMinute = saturdayStartClock.getMinute();
                Time saturdayStartTime = new Time(saturdayHour, saturdayMinute, 0);
                int saturdayEndHour = saturdayEndClock.getHour();
                int saturdayEndMinute = saturdayEndClock.getMinute();
                Time saturdayEndTime = new Time(saturdayEndHour, saturdayEndMinute, 0);

                int sundayHour = sundayStartClock.getHour();
                int sundayMinute = sundayStartClock.getMinute();
                Time sundayStartTime = new Time(sundayHour, sundayMinute, 0);
                int sundayEndHour = sundayEndClock.getHour();
                int sundayEndMinute = sundayEndClock.getMinute();
                Time sundayEndTime = new Time(sundayEndHour, sundayEndMinute, 0);

                DayWorkingHours mondayWorkingHours = new DayWorkingHours(mondayStartTime, mondayEndTime);
                DayWorkingHours tuesdayWorkingHours = new DayWorkingHours(tuesdayStartTime, tuesdayEndTime);
                DayWorkingHours wednesdayWorkingHours = new DayWorkingHours(wednesdayStartTime, wednesdayEndTime);
                DayWorkingHours thursdayWorkingHours = new DayWorkingHours(thursdayStartTime, thursdayEndTime);
                DayWorkingHours fridayWorkingHours = new DayWorkingHours(fridayStartTime, fridayEndTime);
                DayWorkingHours saturdayWorkingHours = new DayWorkingHours(saturdayStartTime, saturdayEndTime);
                DayWorkingHours sundayWorkingHours = new DayWorkingHours(sundayStartTime, sundayEndTime);

                Map<String, DayWorkingHours> dayWorkingHoursMap = new HashMap<>();
                dayWorkingHoursMap.put("Monday", mondayWorkingHours);
                dayWorkingHoursMap.put("Tuesday", tuesdayWorkingHours);
                dayWorkingHoursMap.put("Wednesday", wednesdayWorkingHours);
                dayWorkingHoursMap.put("Thursday", thursdayWorkingHours);
                dayWorkingHoursMap.put("Friday", fridayWorkingHours);
                dayWorkingHoursMap.put("Saturday", saturdayWorkingHours);
                dayWorkingHoursMap.put("Sunday", sundayWorkingHours);
                WorkingHours workingHours = new WorkingHours(dayWorkingHoursMap);

                db.collection("working_hours")
                        .document("week_hours")
                        .set(workingHours)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Handle success
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                            }
                        });
                String company_email, company_name, company_phoneNumber, company_description, company_address;
                company_email = String.valueOf(companyEmail.getText());
                company_name = String.valueOf(companyName.getText());
                company_phoneNumber = String.valueOf(companyPhoneNumber.getText());
                company_description = String.valueOf(companyDescription.getText());
                company_address = String.valueOf(companyAddress.getText());

                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(intent);

                Company company = new Company(company_email, company_name, company_address, company_phoneNumber, company_description, "123", workingHours);
                UserDetails userDetails = new UserDetails(email, name, surname, address, phone, UserRole.Owner);
                Owner owner = new Owner(UUID.randomUUID(), company, userDetails, selectedCategories, selectedEventTypes);
                addCompanyToFirestore(company, userDetails, owner);



                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    if (firebaseUser != null) {
                                        Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show();

                                        mAuth.signOut();
                                        Intent intent = new Intent(v.getContext(), HomeActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(v.getContext(), "Error while registering", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // Registracija je uspješna, sada dodajemo UserDetails u Firestore

                                mAuth.signOut();
                                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                                startActivity(intent);
                            }
                        });
            }
        });


        FloatingActionButton selectEvent = root.findViewById(R.id.select_event_type);
        selectEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectEvent.setOnClickListener(s -> {

                    fetchEventTypes();
                });

            }

        });


        return root;
    }


    private void fetchCategories() {
        db.collection("categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> categoryNames = new ArrayList<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String name = document.getString("name");
                            categoryNames.add(name);
                        }
                        Log.d(TAG, "Fetched categories: " + categoryNames.toString()); // Dodajemo log za prikaz preuzetih kategorija
                        showSubcategoryNamesInBottomSheet(categoryNames);

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

    private void showSubcategoryNamesInBottomSheet(List<String> categoryNames) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
        View dialogView = getLayoutInflater().inflate(R.layout.category_sheet_filter, null);
        LinearLayout checkboxLayout = dialogView.findViewById(R.id.checkbox_layout);

        for (String name : categoryNames) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(name);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        fetchCategoryByName(name);
                    } else {
                        removeCategoryByName(name);
                    }
                }
            });
            checkboxLayout.addView(checkBox);
        }

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }


    private void fetchCategoryByName(String name) {
        db.collection("categories")
                .whereEqualTo("name", name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Category category = document.toObject(Category.class);
                                selectedCategories.add(category);
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

    private void removeCategoryByName(String name) {
        for (Category category : selectedCategories) {
            if (category.getName().equals(name)) {
                selectedCategories.remove(category);
                break;
            }
        }
    }


    private void fetchEventTypes() {
        db.collection("eventTypes")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> eventTypeNames = new ArrayList<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String name = document.getString("name");
                            eventTypeNames.add(name);
                        }
                        Log.d(TAG, "Fetched eventTypes: " + eventTypeNames.toString()); // Dodajemo log za prikaz preuzetih kategorija
                        showEventTypeNamesInBottomSheet(eventTypeNames);

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

    private void showEventTypeNamesInBottomSheet(List<String> categoryNames) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
        View dialogView = getLayoutInflater().inflate(R.layout.event_type_sheet_filter, null);
        LinearLayout checkboxLayout = dialogView.findViewById(R.id.checkbox_layout);

        for (String name : categoryNames) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(name);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        fetchEventTypeByName(name);
                    } else {
                        removeEventTypeByName(name);
                    }
                }
            });
            checkboxLayout.addView(checkBox);
        }

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }


    private void fetchEventTypeByName(String name) {
        db.collection("eventTypes")
                .whereEqualTo("name", name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                EventType eventType = document.toObject(EventType.class);
                                selectedEventTypes.add(eventType);
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

    private void removeEventTypeByName(String name) {
        for (EventType eventType : selectedEventTypes) {
            if (eventType.getName().equals(name)) {
                selectedEventTypes.remove(eventType);
                break;
            }
        }
    }


    private void addUserDetailsToFirestore(UserDetails userDetails, Owner owner) {
        // Add userDetails to Firestore
        db.collection("userDetails")
                .add(userDetails)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "UserDetails added with ID: " + documentReference.getId());
                        // Handle success, if needed
                        addOwnerToFirestore(owner);
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
    private void addCompanyToFirestore(Company company, UserDetails userDetails, Owner owner) {
        // Add userDetails to Firestore
        db.collection("companies")
                .add(company)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Company added with ID: " + documentReference.getId());
                        // Handle success, if needed
                        addUserDetailsToFirestore(userDetails, owner);
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

    private void addOwnerToFirestore(Owner owner) {
        // Add userDetails to Firestore
        db.collection("owners")
                .add(owner)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Owner added with ID: " + documentReference.getId());
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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}