package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeProfileActivity;
import com.example.projekatmobilneaplikacije.fragments.CreateBundleThirdFragment;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReservationListAdapter extends ArrayAdapter<Reservation> implements Filterable{
    private ArrayList<Reservation> aReservations;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context mContext;

    public ReservationListAdapter(Context context, ArrayList<Reservation> reservations){
        super(context, R.layout.reservation_card, reservations);
        aReservations = reservations;
        mContext = context;
    }

    /*
     * Ova metoda vraca ukupan broj elemenata u listi koje treba prikazati
     * */
    @Override
    public int getCount() {
        return aReservations.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Reservation getItem(int position) {
        return aReservations.get(position);
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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reservation reservation = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_card,
                    parent, false);
        }
        LinearLayout reservationCard = convertView.findViewById(R.id.reservation_card_item);
        TextView serviceTitle = convertView.findViewById(R.id.service_title);
        TextView status = convertView.findViewById(R.id.reservation_status);


        if(reservation != null){
            serviceTitle.setText(reservation.getService().getTitle());
            status.setText(reservation.getStatus().name());
            reservationCard.setOnClickListener(v -> {
                Log.i("App", "Clicked:, id: " +
                        reservation.getId());
                Toast.makeText(getContext(), "Clicked: id: " + reservation.getId(), Toast.LENGTH_SHORT).show();
            });
        }



        return convertView;
    }

    public void filterByStatus(String status) {
        ArrayList<Reservation> filteredReservations = new ArrayList<>();
        for(Reservation reservation : aReservations) {
            if(reservation.getStatus().name().equalsIgnoreCase(status)) {
                filteredReservations.add(reservation);
            }
        }

        if(filteredReservations.isEmpty()) {
            Log.d("ReservationListAdapter", "No reservations found for status: " + status);
        } else {
            Log.d("ReservationListAdapter", "Filtered reservations for status " + status + ": " + filteredReservations.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredReservations); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }

}
