package com.example.projekatmobilneaplikacije.activities;

import android.os.Bundle;

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

    }
}