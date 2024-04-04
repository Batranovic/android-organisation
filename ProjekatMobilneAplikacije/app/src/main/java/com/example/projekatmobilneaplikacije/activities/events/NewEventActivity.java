package com.example.projekatmobilneaplikacije.activities.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeProfileActivity;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeRegistrationActivity;
import com.example.projekatmobilneaplikacije.databinding.ActivityEmployeeProfileBinding;
import com.example.projekatmobilneaplikacije.databinding.ActivityNewEventBinding;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.fragments.employees.EmployeeWHOverview;
import com.example.projekatmobilneaplikacije.fragments.employees.EmployeeWorkCalendarFragment;
import com.example.projekatmobilneaplikacije.fragments.employees.EmployeeWorkingHoursFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NewEventActivity extends AppCompatActivity {

    ActivityNewEventBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button finishButton = binding.finishNewEvent;
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEventActivity.this, EmployeeProfileActivity.class);
                startActivity(intent);
            }
        });

    }
}