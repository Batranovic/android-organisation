package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.fragments.events.EventTypeFragment;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.EventType;

import java.util.ArrayList;

public class EventTypeListAdapter extends ArrayAdapter<EventType> {

    private ArrayList<EventType> aEventTypes;
    private FragmentManager fragmentManager;
    public EventTypeListAdapter(Context context, ArrayList<EventType> eventTypes) {
        super(context, R.layout.event_type_card, eventTypes);
        aEventTypes = eventTypes;
    }

    @Override
    public int getCount() {
        return aEventTypes.size();
    }
    @Nullable
    @Override
    public EventType getItem(int position) {
        return aEventTypes.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventType eventType = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_type_card, parent, false);
        }
        LinearLayout eventCard = convertView.findViewById(R.id.event_card_item);
        TextView eventName = convertView.findViewById(R.id.event_name_card);
        TextView eventDescription = convertView.findViewById(R.id.event_description_card);
        ImageButton activateDeactivateButton = convertView.findViewById(R.id.activate_deactivate_event);

        if (eventType != null) {
            eventName.setText(eventType.getName());
            eventDescription.setText(eventType.getDescription());

            // Postavi odgovarajuću ikonicu na osnovu statusa događaja
            int iconResource = eventType.isActive() ? R.drawable.active_icon : R.drawable.inactive_icon;
            activateDeactivateButton.setImageResource(iconResource);

            eventCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + eventType.getName() + ", id: " + eventType.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + eventType.getName() + ", id: " + eventType.getId().toString(), Toast.LENGTH_SHORT).show();
            });

            activateDeactivateButton.setOnClickListener(v -> {
                // Toggle status događaja
                eventType.setActive(!eventType.isActive());

                // Ažuriraj ikonicu na osnovu novog statusa
                int newIconResource = eventType.isActive() ? R.drawable.active_icon : R.drawable.inactive_icon;
                activateDeactivateButton.setImageResource(newIconResource);
            });
        }
        ImageButton  editEventButton = convertView.findViewById(R.id.edit_event); // Dodajte dugme edit_event

        editEventButton.setOnClickListener(v -> {
            // Create a new instance of EventTypeFragment
            EventTypeFragment fragment = new EventTypeFragment();

            // Pass data if needed using Bundle
            Bundle bundle = new Bundle();
            bundle.putString("eventName", eventType.getName());
            fragment.setArguments(bundle);

            // Replace the current fragment with EventTypeFragment
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_event_type_page, fragment) // Replace R.id.fragment_container with your container id
                    .addToBackStack(null)
                    .commit();
        });



        return convertView;
    }


}
