package com.example.projekatmobilneaplikacije.activities.registration;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.ActivityRegistrationBinding;
import com.example.projekatmobilneaplikacije.databinding.ActivityRegistrationRequestsBinding;
import com.example.projekatmobilneaplikacije.fragments.registration.RegistrationFragment;
import com.example.projekatmobilneaplikacije.fragments.registration.RegistrationRequestsFragment;

public class RegistrationRequestsActivity extends AppCompatActivity {
    private ActivityRegistrationRequestsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_registration_requests, new RegistrationRequestsFragment())
                .commit();



    }

}