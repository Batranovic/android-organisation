package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.projekatmobilneaplikacije.activities.OneGuestActivity;
import com.example.projekatmobilneaplikacije.activities.agenda.AgendaAndGuestsActivity;
import com.example.projekatmobilneaplikacije.model.GuestList;

import java.util.ArrayList;
import java.util.List;

public class GuestListAdapter extends ArrayAdapter<GuestList> {
    private ArrayList<GuestList> aGuests;

    public GuestListAdapter(Context context, ArrayList<GuestList> guests) {
        super(context, R.layout.one_guest_page, guests);
        aGuests = guests;
    }

    @Override
    public int getCount() {
        return aGuests.size();
    }

    @Nullable
    @Override
    public GuestList getItem(int position) {
        return aGuests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GuestList guest = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_guest_page, parent, false);
        }
        LinearLayout guestCard = convertView.findViewById(R.id.one_guest);
        TextView guestName = convertView.findViewById(R.id.text_view_guest_name);
        TextView guestAge = convertView.findViewById(R.id.text_view_guest_age);
        TextView guestInvited = convertView.findViewById(R.id.text_view_guest_invited);
        TextView guestAccepted = convertView.findViewById(R.id.text_view_guest_accepted);
        TextView guestRequest = convertView.findViewById(R.id.text_view_guest_special_requests);

        if (guest != null) {
            guestName.setText(guest.getName());
            guestAge.setText(guest.getAge());
            guestInvited.setText(guest.isInvited() ? "Yes" : "No");
            guestAccepted.setText(guest.isHasAccepted() ? "Yes" : "No");
            guestRequest.setText(convertListToString(guest.getSpecialRequests()));
            guestCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + guest.getName());
                Toast.makeText(getContext(), "Clicked: " + guest.getName(), Toast.LENGTH_SHORT).show();
            });
        }

        View itemView = convertView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event
                Intent intent = new Intent(v.getContext(), OneGuestActivity.class);

                intent.putExtra("Id", guest.getId());
                intent.putExtra("Name", guest.getName());
                intent.putExtra("Age", guest.getAge());
                intent.putExtra("Invited", guest.isInvited());
                intent.putExtra("Accepted", guest.isHasAccepted());
                intent.putExtra("Requests", convertListToString(guest.getSpecialRequests()));

                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private String convertListToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "None";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : list) {
            stringBuilder.append(item).append(", ");
        }
        // Remove the last comma and space
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        return stringBuilder.toString();
    }
}
