package com.example.projekatmobilneaplikacije.activities.employees;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.ActivityEmployeeRegistrationBinding;
import com.example.projekatmobilneaplikacije.databinding.ActivityHomeBinding;
import com.example.projekatmobilneaplikacije.fragments.employees.EmployeeRegistrationFragment;

public class EmployeeRegistrationActivity extends AppCompatActivity {

    private ActivityEmployeeRegistrationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.employee_registration, new EmployeeRegistrationFragment())
                .commit();
    }
}