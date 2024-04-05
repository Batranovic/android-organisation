package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.CreateEvent;


import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<CreateEvent> {

    private ArrayList<CreateEvent> aEvents;

    public EventListAdapter(Context context, ArrayList<CreateEvent> events){
        super(context, R.layout.one_event_fragment, events);
        aEvents = events;

    }

    @Override
    public int getCount() {
        return aEvents.size();
    }


    @Nullable
    @Override
    public CreateEvent getItem(int position) {
        return aEvents.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CreateEvent event = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_event_fragment,
                    parent, false);
        }
        LinearLayout eventCard = convertView.findViewById(R.id.one_event);
        TextView eventName = convertView.findViewById(R.id.event_name);
        TextView eventType = convertView.findViewById(R.id.event_type);
        TextView eventDescription = convertView.findViewById(R.id.event_description);
        TextView eventGuestNumber = convertView.findViewById(R.id.event_guest_number);
        TextView eventLocation = convertView.findViewById(R.id.event_location);
        TextView eventDate = convertView.findViewById(R.id.event_date);
        TextView eventPrivacy = convertView.findViewById(R.id.event_privacy);

        if(event != null){
            eventName.setText(event.getName());
            eventType.setText(event.getEventType());
            eventDescription.setText(event.getDescription());
            eventGuestNumber.setText(String.valueOf(event.getParticipants()));
            eventLocation.setText(event.getLocation());
            eventDate.setText(String.valueOf(event.getDate()));
            eventPrivacy.setText(event.isPrivate() ? "Yes" : "No");
            eventCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + event.getName() + ", id: " +
                        event.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + event.getName()  +
                        ", id: " + event.getId().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Clicked: ", Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
