package com.example.projekatmobilneaplikacije.activities.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.example.projekatmobilneaplikacije.databinding.PlanBudgetPopupBinding;
public class PlanBudgetActivity extends AppCompatActivity {
    PlanBudgetPopupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PlanBudgetPopupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Button finishButton = binding.btnCancel;
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
