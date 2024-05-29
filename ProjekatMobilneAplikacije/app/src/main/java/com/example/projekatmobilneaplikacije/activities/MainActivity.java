package com.example.projekatmobilneaplikacije.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.ActivityHomeBinding;
import com.example.projekatmobilneaplikacije.databinding.ActivityMainBinding;
import com.example.projekatmobilneaplikacije.databinding.ActivityRegistrationBinding;
import com.example.projekatmobilneaplikacije.fragments.LoginFragment;
import com.example.projekatmobilneaplikacije.fragments.registration.LogoutFragment;
import com.example.projekatmobilneaplikacije.fragments.registration.RegistrationFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    TextView loggedInUser;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();


        auth = FirebaseAuth.getInstance();
        loggedInUser = root.findViewById(R.id.loggedInUser);
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();

        }else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }



    }
}