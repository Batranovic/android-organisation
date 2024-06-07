package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeProfileActivity;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Event;

import java.util.ArrayList;



public class EmployeeEventListAdapter extends ArrayAdapter<Event> {
        private ArrayList<Event> aEvents;

        public EmployeeEventListAdapter(Context context, ArrayList<Event> events){
            super(context, R.layout.employee_event_card, events);
            aEvents = events;

        }

        @Override
        public int getCount() {
            return aEvents.size();
        }

        /*
         * Ova metoda vraca pojedinacan element na osnovu pozicije
         * */
        @Nullable
        @Override
        public Event getItem(int position) {
            return aEvents.get(position);
        }

        /*
         * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
         * listu ili niz, pozicija je dovoljno dobra da bude identifikator.
         * Naravno mozemo iskoristiti i jedinstveni identifikator objekta, ako on postoji.
         * */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /*
         * Ova metoda popunjava pojedinacan element ListView-a podacima.
         * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
         * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
         * uzece java objekat sa odredjene pozicije (model) koji cuva podatke,
         * i layout koji treba da prikaze te podatke (view) npr R.layout.product_card.
         * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
         * popuniti view podacima i poslati listview da prikaze, i nastavice
         * sledecu iteraciju.
         * */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Event event = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.employee_event_card,
                        parent, false);
            }
            LinearLayout eventCard = convertView.findViewById(R.id.employee_event_card);
            TextView eventName = convertView.findViewById(R.id.event_name);
            TextView eventDate = convertView.findViewById(R.id.event_date);
            TextView eventStartTime = convertView.findViewById(R.id.event_start_time);
            TextView eventEndTime = convertView.findViewById(R.id.event_end_time);
            TextView eventType = convertView.findViewById(R.id.event_type);

            if(event != null){
                //eventName.setText(event.getName());
                //eventDate.setText(event.getDate());
                //eventStartTime.setText(event.getStartTime());
                //eventEndTime.setText(event.getEndTime());
                eventType.setText(event.getType());
//                eventCard.setOnClickListener(v -> {
//                    // Handle click on the item at 'position'
//                    Log.i("App", "Clicked: " + event.getName() + ", id: " +
//                            event.getId().toString());
//                    Toast.makeText(getContext(), "Clicked: " + event.getName()  +
//                            ", id: " + event.getId().toString(), Toast.LENGTH_SHORT).show();
//                });
            }



            return convertView;
        }



}
