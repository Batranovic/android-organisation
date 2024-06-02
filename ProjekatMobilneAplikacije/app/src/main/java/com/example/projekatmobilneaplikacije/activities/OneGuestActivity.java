package com.example.projekatmobilneaplikacije.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.ActivityOneGuestBinding;
import com.example.projekatmobilneaplikacije.model.GuestList;

import java.util.List;

public class OneGuestActivity extends AppCompatActivity {
    private ActivityOneGuestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOneGuestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GuestList guest = getIntent().getParcelableExtra("guest");

        if (guest != null) {
            binding.textViewGuestName.setText(guest.getName());
            binding.textViewGuestAge.setText(guest.getAge());
            binding.textViewGuestInvited.setText(guest.isInvited() ? "Yes" : "No");
            binding.textViewGuestAccepted.setText(guest.isHasAccepted() ? "Yes" : "No");
            binding.textViewGuestSpecialRequests.setText(convertListToString(guest.getSpecialRequests()));
        }
    }

    private String convertListToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "None";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : list) {
            stringBuilder.append(item).append(", ");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        return stringBuilder.toString();
    }
}