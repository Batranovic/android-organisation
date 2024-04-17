package com.example.projekatmobilneaplikacije.activities.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeProfileActivity;
import com.example.projekatmobilneaplikacije.activities.events.NewEventActivity;
import com.example.projekatmobilneaplikacije.databinding.ActivityEventBudgetBinding;

public class BudgetActivity extends AppCompatActivity {
    ActivityEventBudgetBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBudgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Button finishButton = binding.btnAddNewItem;
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BudgetActivity.this, PlanBudgetActivity.class);
                startActivity(intent);
            }
        });

    }
}

