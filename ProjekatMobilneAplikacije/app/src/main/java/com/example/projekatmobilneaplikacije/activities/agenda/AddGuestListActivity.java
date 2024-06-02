package com.example.projekatmobilneaplikacije.activities.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.AddGuestsListBinding;

import com.example.projekatmobilneaplikacije.fragments.GuestListFragment;
import com.example.projekatmobilneaplikacije.model.GuestList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddGuestListActivity extends AppCompatActivity {

    AddGuestsListBinding binding;
    EditText guestName;
    Spinner spinner;
    RadioGroup invitedGroup;
    RadioGroup acceptedGroup;
    CheckBox noReqCheckBox;
    CheckBox veganCheckBox;
    CheckBox vegetarianCheckBox;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static AddGuestListActivity newInstance() {
        return new AddGuestListActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddGuestsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();
        guestName = root.findViewById(R.id.edit_text_guest_name);
        spinner = root.findViewById(R.id.spinner_guest_age);
        invitedGroup = root.findViewById(R.id.radio_group_invited);
        acceptedGroup = root.findViewById(R.id.radio_group_accepted);
        noReqCheckBox = root.findViewById(R.id.checkbox_noreq);
        veganCheckBox = root.findViewById(R.id.checkbox_vegan);
        vegetarianCheckBox = root.findViewById(R.id.checkbox_vegetarian);

        Button createButton = root.findViewById(R.id.button_add_guest);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGuestToFirestore();
            }
        });
        Button allGuestsButton = root.findViewById(R.id.button_all_guests);
        allGuestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllGuestsFragment();
            }
        });
    }
    private void clearInputFields() {
        guestName.setText("");
        spinner.setSelection(0);
        invitedGroup.clearCheck();
        acceptedGroup.clearCheck();
        noReqCheckBox.setChecked(false);
        veganCheckBox.setChecked(false);
        vegetarianCheckBox.setChecked(false);
    }


    private void saveGuestToFirestore() {
        String name = guestName.getText().toString();
        String age = spinner.getSelectedItem().toString();

        int selectedInvitedId = invitedGroup.getCheckedRadioButtonId();
        boolean isInvited = selectedInvitedId == R.id.radio_invited_yes;

        int selectedAcceptedId = acceptedGroup.getCheckedRadioButtonId();
        boolean hasAccepted = selectedAcceptedId == R.id.radio_accepted_yes;

        List<String> specialRequests = new ArrayList<>();
        if (noReqCheckBox.isChecked()) {
            specialRequests.add(getString(R.string.noreq));
        }
        if (veganCheckBox.isChecked()) {
            specialRequests.add(getString(R.string.vegan));
        }
        if (vegetarianCheckBox.isChecked()) {
            specialRequests.add(getString(R.string.vegetarian));
        }

        GuestList guest = new GuestList(name, age, isInvited, hasAccepted, specialRequests);

        db.collection("guestList")
                .add(guest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AddGuestListActivity", "Guest added with ID: " + documentReference.getId());
                        Toast.makeText(AddGuestListActivity.this, "Guest created successfully", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddGuestListActivity", "Error adding guest document", e);
                        Toast.makeText(AddGuestListActivity.this, "Error creating guest", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void showAllGuestsFragment() {
        db.collection("guestList").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<GuestList> guestList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            GuestList guest = document.toObject(GuestList.class);
                            guestList.add(guest);
                        }

                        GuestListFragment guestListFragment = GuestListFragment.newInstance(guestList);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.guest_container, guestListFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Log.w("AddGuestListActivity", "Error getting documents.", task.getException());
                    }
                });
    }

    }

