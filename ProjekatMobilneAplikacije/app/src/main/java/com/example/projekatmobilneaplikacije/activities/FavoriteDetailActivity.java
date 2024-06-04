package com.example.projekatmobilneaplikacije.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class FavoriteDetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private Button reserveButton, deleteButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);

        titleTextView = findViewById(R.id.text_view_titlef);
        reserveButton = findViewById(R.id.button_reserve);
        deleteButton = findViewById(R.id.button_delete);

        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        String type = getIntent().getStringExtra("type");

        titleTextView.setText(title);

        reserveButton.setOnClickListener(v -> {
            Toast.makeText(this, "Successfully reserved " + title, Toast.LENGTH_SHORT).show();
            // Add your reservation logic here
        });

        deleteButton.setOnClickListener(v -> {
            db.collection("favorites").document(id).delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Favorite item deleted", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity after deletion
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to delete favorite item", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
