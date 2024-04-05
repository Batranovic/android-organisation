package com.example.projekatmobilneaplikacije.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.ActivityEditServiceAndProductBinding;
import com.example.projekatmobilneaplikacije.databinding.ActivityRegistrationBinding;

public class EditServiceAndProductActivity extends AppCompatActivity {

    private ActivityEditServiceAndProductBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditServiceAndProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button button = binding.buttonCreateService;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(intent);
            }

        });


    }
}