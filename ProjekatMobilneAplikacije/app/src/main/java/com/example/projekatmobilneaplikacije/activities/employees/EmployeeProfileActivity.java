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
import com.example.projekatmobilneaplikacije.fragments.employees.EmployeeInformationFragment;
import com.example.projekatmobilneaplikacije.fragments.employees.EmployeeRegistrationFragment;

public class EmployeeProfileActivity extends AppCompatActivity {

    ActivityEmployeeProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        long employeeIdLong = getIntent().getLongExtra("id", -1L);
        int employeeId = (int) employeeIdLong;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.employee_information, new EmployeeInformationFragment())
                .commit();


        if(employeeId != -1) {
            Log.i("App", "Employee ID: " + employeeId);
            Log.i("App", "Employee Profile Activiti onCreate() ");


        }
    }
}