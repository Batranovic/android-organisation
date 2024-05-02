package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.projekatmobilneaplikacije.activities.EditEventTypeActivity;
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

        if(eventType != null){
            eventName.setText(eventType.getName());
            eventDescription.setText(eventType.getDescription());
        eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditEventTypeActivity.class);
                intent.putExtra("eventName", eventType.getName());
                intent.putExtra("eventDescription", eventType.getDescription());
                getContext().startActivity(intent);
            }
        });
        }


        return convertView;
    }


}
