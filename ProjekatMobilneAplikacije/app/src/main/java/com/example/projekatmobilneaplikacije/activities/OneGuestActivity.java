package com.example.projekatmobilneaplikacije.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.ActivityOneGuestBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OneGuestActivity extends AppCompatActivity {
    private ActivityOneGuestBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "OneGuestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOneGuestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Preuzimanje podataka iz Intent-a
        String guestId = getIntent().getStringExtra("Id");
        String guestName = getIntent().getStringExtra("Name");
        String guestAge = getIntent().getStringExtra("Age");
        boolean guestInvited = getIntent().getBooleanExtra("Invited", false);
        boolean guestAccepted = getIntent().getBooleanExtra("Accepted", false);
        String guestRequests = getIntent().getStringExtra("Requests");

        // Logovi za debug
        Log.d(TAG, "Guest ID: " + guestId);
        Log.d(TAG, "Guest Name: " + guestName);
        Log.d(TAG, "Guest Age: " + guestAge);
        Log.d(TAG, "Guest Invited: " + guestInvited);
        Log.d(TAG, "Guest Accepted: " + guestAccepted);
        Log.d(TAG, "Guest Requests: " + guestRequests);

        // Prikazivanje podataka
        binding.textViewGuestName.setText(guestName);
        binding.textViewGuestAge.setText(guestAge);
        binding.textViewGuestInvited.setText(guestInvited ? "Yes" : "No");
        binding.textViewGuestAccepted.setText(guestAccepted ? "Yes" : "No");
        binding.textViewGuestSpecialRequests.setText(guestRequests);

        // Postavljanje onClickListener-a za dugme Delete
        binding.buttonDeleteGuest.setOnClickListener(v -> {
            if (guestId != null && !guestId.isEmpty()) {
                showDeleteConfirmationDialog(guestId);
            } else {
                Toast.makeText(OneGuestActivity.this, "Guest ID is invalid", Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonEditGuest.setOnClickListener(v -> {
            enableEditing(true);
        });

        // Postavljanje onClickListener-a za dugme Save
        binding.buttonSaveGuest.setOnClickListener(v -> {
            saveGuestChanges(guestId);
        });
    }
    private void enableEditing(boolean enable) {
        binding.textViewGuestName.setEnabled(enable);
        binding.textViewGuestInvited.setEnabled(enable);
        binding.textViewGuestAccepted.setEnabled(enable);
        binding.textViewGuestSpecialRequests.setEnabled(enable);
        binding.buttonSaveGuest.setVisibility(enable ? View.VISIBLE : View.GONE);
        binding.buttonEditGuest.setVisibility(enable ? View.GONE : View.VISIBLE);
    }
    private void saveGuestChanges(String guestId) {
        String updatedName = binding.textViewGuestName.getText().toString();
        boolean updatedInvited = binding.textViewGuestInvited.getText().toString().equalsIgnoreCase("Yes");
        boolean updatedAccepted = binding.textViewGuestAccepted.getText().toString().equalsIgnoreCase("Yes");
        String updatedRequests = binding.textViewGuestSpecialRequests.getText().toString();

        List<String> specialRequests = new ArrayList<>();
        specialRequests.add(updatedRequests);



        db.collection("guestList").document(guestId)
                .update("name", updatedName,
                        "invited", updatedInvited,
                        "hasAccepted", updatedAccepted,
                        "specialRequests", specialRequests)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(OneGuestActivity.this, "Guest updated successfully", Toast.LENGTH_SHORT).show();
                    enableEditing(false);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OneGuestActivity.this, "Failed to update guest: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showDeleteConfirmationDialog(String guestId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Guest")
                .setMessage("Are you sure you want to delete this guest?")
                .setPositiveButton("Yes", (dialog, which) -> deleteGuest(guestId))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteGuest(String guestId) {
        db.collection("guestList").document(guestId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(OneGuestActivity.this, "Guest deleted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Zatvara aktivnost i vraća se na prethodnu
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OneGuestActivity.this, "Failed to delete guest: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
