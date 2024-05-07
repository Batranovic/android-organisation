package com.example.projekatmobilneaplikacije.fragments;

import static android.graphics.Insets.add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.PopupWindow;


import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.HomeActivity;
import com.example.projekatmobilneaplikacije.activities.budget.BudgetActivity;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateEventBinding;
import com.example.projekatmobilneaplikacije.fragments.budget.BudgetFragment;
import com.example.projekatmobilneaplikacije.model.CreateEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class EventOrganizationFragment extends Fragment {

    private FragmentCreateEventBinding binding;
    private Spinner spinner;
    private RadioGroup radioGroup;

    private TextView eventDateField;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static EventOrganizationFragment newInstance() {
        return new EventOrganizationFragment();
        // Required empty public constructor
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spinner = root.findViewById(R.id.spinner_event_create); // Zamijenite s ID-om vašeg Spinnera
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Uhvatimo odabranu vrijednost Spinnera
                String selectedValue = parent.getItemAtPosition(position).toString();
                // Sada možemo raditi što god želimo s ovom vrijednošću, poput spremanja u CreateEvent objekt
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Implementacija nije potrebna ako nije potrebno rukovati s događajem kada nije ništa odabrano
            }
        });

        EditText createName = root.findViewById(R.id.event_name_field);
        EditText createDescription = root.findViewById(R.id.event_description_field);
        EditText createLocation = root.findViewById(R.id.event_address_field);
        EditText createDistance = root.findViewById(R.id.event_km_field);
        EditText createParticipants = root.findViewById(R.id.event_guest_number_field);




        super.onViewCreated(root, savedInstanceState);

        Button createButton = root.findViewById(R.id.create_event_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to plan budget for event you have created?")

                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(requireActivity(), BudgetActivity.class);
                                startActivity(intent);


                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Korisnik je kliknuo na No
                                saveCreateEventDataToFirestore(createName,createDescription, createLocation, createDistance,createParticipants);
                                String message = "Created Succesfully";
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                                startActivity(intent);

                            }
                        });
// Kreiranje i prikazivanje AlertDialog-a
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        eventDateField = root.findViewById(R.id.event_date_field);
        eventDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Initialize the dateSetListener
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Update the EditText with the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String selectedDate = dateFormat.format(calendar.getTime());
                eventDateField.setText(selectedDate);
            }
        };
return root;
    }

private void saveCreateEventDataToFirestore(EditText createName, EditText createDescription, EditText createLocation, EditText createDistance, EditText createParticipants) {
    String name = createName.getText().toString();
    String description = createDescription.getText().toString();
    String location = createLocation.getText().toString();
    String distance = createDistance.getText().toString();
    String locationAndDistance = location + ", up to " + distance + " km";
    String participants = createParticipants.getText().toString();
    int maxParticipants = Integer.parseInt(participants);
    String selectedSpinnerValue = spinner.getSelectedItem().toString();
    String selectedDate = eventDateField.getText().toString();

    // Sada možete dodati ovu vrijednost u CreateEvent objekt

    CreateEvent createEvent = new CreateEvent();

    createEvent.setEventType(selectedSpinnerValue);
    createEvent.setName(name);
    createEvent.setDescription(description);
    createEvent.setLocation(locationAndDistance);
    createEvent.setParticipants(maxParticipants);
    createEvent.setDate(selectedDate);
    createEvent.setPrivate(true);


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("createEvents")
            .add(createEvent)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("EventOrganizationFragment", "CreateEvent added with ID: " + documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("EventOrganizationFragment", "Error adding EventCreate document", e);
                }
            });





}



    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                dateSetListener,
                year,
                month,
                dayOfMonth
        );
        datePickerDialog.show();
    }
}
