package com.example.projekatmobilneaplikacije.activities.employees;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.ActivityEmployeeProfileBinding;
import com.example.projekatmobilneaplikacije.databinding.ActivityEmployeeRegistrationBinding;

public class EmployeeProfileActivity extends AppCompatActivity {

    ActivityEmployeeProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        long employeeIdLong = getIntent().getLongExtra("id", -1L);
        int employeeId = (int) employeeIdLong;


        if(employeeId != -1) {
            Log.i("App", "Employee ID: " + employeeId);
            Log.i("App", "Employee Profile Activiti onCreate() ");
            // Ovdje možete izvršiti logiku za dohvaćanje detalja zaposlenika iz baze podataka,
            // web servisa ili nekog drugog izvora podataka pomoću ID-a zaposlenika.

            // Nakon što dobijete podatke o zaposleniku, možete ih koristiti za prikazivanje
            // u vašem layoutu EmployeeProfileActivity.

            // Primjer:
            // binding.textViewEmployeeId.setText(String.valueOf(employeeId));

            // Ovdje možete postaviti ostale podatke o zaposleniku u vaš layout.
        } else {
            // Ako nije pročitan ID zaposlenika iz intenta, prikaži poruku o grešci
            Toast.makeText(this, "Error: Employee ID not found", Toast.LENGTH_SHORT).show();
        }
    }
}