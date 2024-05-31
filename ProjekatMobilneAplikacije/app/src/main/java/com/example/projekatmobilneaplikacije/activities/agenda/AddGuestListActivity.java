package com.example.projekatmobilneaplikacije.activities.agenda;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.AddAgendaBinding;
import com.example.projekatmobilneaplikacije.databinding.AddGuestsListBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddGuestListActivity extends AppCompatActivity {

    AddGuestsListBinding binding;
    EditText name_surname;
    Spinner spinner;
    boolean isInvitedChecked = false;
    boolean isConfirmedChecked = false;
    boolean isVeganChecked= false;
    boolean isVegeterianChecked= false;
    boolean isNoReqChecked = false;

    public static AddGuestListActivity newInstance(){return new AddGuestListActivity();}
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddGuestsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();
        EditText guestName = root.findViewById(R.id.edit_text_guest_name);
        spinner = root.findViewById(R.id.spinner_guest_age);
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
        Button createButton = root.findViewById(R.id.button_add_guest);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveAgendaToFirestore(agendaName, agendaDescription, agendaLocation);
                String message = "Created Succesfully";
                //Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();


            }
        });
    }
    }











