package com.example.projekatmobilneaplikacije.activities.agenda;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.ActivityAgGuestBinding;
import com.example.projekatmobilneaplikacije.databinding.ActivityEmployeeProfileBinding;
import com.example.projekatmobilneaplikacije.fragments.EventInfoFragment;
import com.example.projekatmobilneaplikacije.fragments.employees.EmployeeInformationFragment;

public class AgendaAndGuestsActivity  extends AppCompatActivity {
ActivityAgGuestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgGuestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        long eventIdLong = getIntent().getLongExtra("id", -1L);
        int eventId = (int) eventIdLong;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.event_info, new EventInfoFragment())
                .commit();


        if(eventId != 1) {
            Log.i("App", "Event ID: " + eventId);
            Log.i("App", "Event ");


        }
    }
}
