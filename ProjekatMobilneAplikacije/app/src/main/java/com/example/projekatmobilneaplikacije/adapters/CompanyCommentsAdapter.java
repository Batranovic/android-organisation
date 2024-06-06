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
import com.example.projekatmobilneaplikacije.model.CompanyReview;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CompanyCommentsAdapter extends ArrayAdapter<CompanyReview> {
    private ArrayList<CompanyReview> aComments;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context mContext;

    public CompanyCommentsAdapter(Context context, ArrayList<CompanyReview> comments){
        super(context, R.layout.company_comment_card, comments);
        aComments = comments;
        mContext = context;
    }

    @Override
    public int getCount() {
        return aComments.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public CompanyReview getItem(int position) {
        return aComments.get(position);
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
        CompanyReview review = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_comment_card,
                    parent, false);
        }
        LinearLayout reviewCard = convertView.findViewById(R.id.company_comment_card);
        TextView text = convertView.findViewById(R.id.comment);
        TextView grade = convertView.findViewById(R.id.grade);
        TextView user = convertView.findViewById(R.id.user);
        TextView date = convertView.findViewById(R.id.date);

        if(review != null){
            text.setText(review.getText());
            grade.setText(String.valueOf(review.getGrade()));
            user.setText(review.getEventOrganizer().getDetails().getUsername());

            Date reviewDate = review.getReviewDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String fromDateString = dateFormat.format(reviewDate);
            date.setText(fromDateString);

            reviewCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("App", "Clicked: " + review.getText() + ", id: " +
                        review.getId());
            });
        }


        return convertView;
    }
}
