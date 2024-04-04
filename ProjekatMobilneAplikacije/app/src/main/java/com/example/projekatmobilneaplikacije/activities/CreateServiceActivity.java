package com.example.projekatmobilneaplikacije.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.fragments.CreateServiceFirstFragment;
import com.example.projekatmobilneaplikacije.fragments.CreateServiceSecondFragment;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;

public class CreateServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_service);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_service_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FragmentTransition.to(CreateServiceFirstFragment.newInstance("Fragment 1", "Ovo je fragment 1"), CreateServiceActivity.this, false, R.id.create_service_container);


    }
}