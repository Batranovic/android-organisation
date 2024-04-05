package com.example.projekatmobilneaplikacije.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.ActivityRegistrationBinding;
import com.example.projekatmobilneaplikacije.fragments.registration.RegistrationFragment;

public class RegistrationActivity extends AppCompatActivity {
    private ActivityRegistrationBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_registration_container, new RegistrationFragment())
                .commit();



    }


}
