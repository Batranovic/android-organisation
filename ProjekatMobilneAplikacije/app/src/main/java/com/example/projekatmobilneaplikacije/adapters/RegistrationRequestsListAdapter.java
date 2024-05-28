package com.example.projekatmobilneaplikacije.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.EditServiceAndProductActivity;
import com.example.projekatmobilneaplikacije.activities.registration.RegistrationRequestDetailActivity;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.fragments.registration.RegistrationRequestsListFragment;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.EventType;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.RegistrationRequest;
import com.example.projekatmobilneaplikacije.model.enumerations.Owner;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class RegistrationRequestsListAdapter extends ArrayAdapter<RegistrationRequest> implements Filterable {

    public ArrayList<RegistrationRequest> mregistrationRequests;
    private Context mContext;

    public RegistrationRequestsListAdapter(Context context, ArrayList<RegistrationRequest> registrationRequests) {
        super(context, R.layout.registration_request_card, registrationRequests);
        mContext = context;
        mregistrationRequests = registrationRequests;
    }

    @Override
    public int getCount() {
        return mregistrationRequests.size();
    }
    @Nullable
    @Override
    public RegistrationRequest getItem(int position) {
        return mregistrationRequests.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RegistrationRequest item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.registration_request_card, parent, false);
        }


        LinearLayout card = convertView.findViewById(R.id.registration_request_card);
        TextView companyName = convertView.findViewById(R.id.registration_request_card_company_name);
        TextView ownerName = convertView.findViewById(R.id.registration_request_card_owner_name);
        TextView ownerMail = convertView.findViewById(R.id.registration_request_card_owner_email);
        TextView companyMail = convertView.findViewById(R.id.registration_request_card_company_email);


        RegistrationRequest registrationRequest = (RegistrationRequest) item;
        companyName.setText(registrationRequest.owner.getCompany().getName());
        ownerName.setText(registrationRequest.owner.getUserDetails().getName());
        ownerMail.setText(registrationRequest.owner.getUserDetails().getUsername());
        companyMail.setText(registrationRequest.owner.getCompany().getEmail());

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegistrationRequestDetailActivity.class);
                intent.putExtra("company_name", registrationRequest.owner.getCompany().getName());
                intent.putExtra("owner_name",registrationRequest.owner.getUserDetails().getName());
                intent.putExtra("owner_mail",registrationRequest.owner.getUserDetails().getUsername());
                intent.putExtra("company_mail",registrationRequest.owner.getCompany().getEmail());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    public void filterByEventType(String eventType) {
        ArrayList<RegistrationRequest> filteredRequests = new ArrayList<>();
        for(RegistrationRequest registrationRequest : mregistrationRequests) {
            for (EventType eventType1 : registrationRequest.owner.getEventTypes()) {
                if (eventType1.getName().equalsIgnoreCase(eventType)) {
                    filteredRequests.add(registrationRequest);
                    break;
                }
            }
        }

        if(filteredRequests.isEmpty()) {
            Log.d("RegistrationRequestListAdapter", "No filteredRequests found for subcategory: " + eventType);
        } else {
            Log.d("RegistrationRequestListAdapter", "Filtered filteredRequests for category " + eventType + ": " + filteredRequests.size());
        }

        clear();
        addAll(filteredRequests);
        notifyDataSetChanged();
    }

    public void filterByCategory(String category) {
        ArrayList<RegistrationRequest> filteredRequests = new ArrayList<>();
        for (RegistrationRequest registrationRequest : mregistrationRequests) {
            for (Category cat : registrationRequest.owner.getCategories()) {
                if (cat.getName().equalsIgnoreCase(category)) {
                    filteredRequests.add(registrationRequest);
                    break; // Prekida petlju ako je pronađena odgovarajuća kategorija
                }
            }
        }

        if (filteredRequests.isEmpty()) {
            Log.d("RegistrationRequestListAdapter", "No registrationRequest found for category: " + category);
        } else {
            Log.d("RegistrationRequestListAdapter", "Filtered registrationRequest for category " + category + ": " + filteredRequests.size());
        }

        clear(); // Očisti postojeće podatke u adapteru
        addAll(filteredRequests); // Dodajte filtrirane proizvode u adapter
        notifyDataSetChanged(); // Obavestite adapter o promenama
    }
    public void filterByMonthAndYear(int month, int year) {
        ArrayList<RegistrationRequest> filteredRequests = new ArrayList<>();
        for (RegistrationRequest registrationRequest : mregistrationRequests) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(registrationRequest.getSentRequest());
            int requestMonth = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH počinje od 0, pa dodajemo 1
            int requestYear = calendar.get(Calendar.YEAR);

            // Ovde ćemo proveriti i mesec i godinu
            if (requestMonth == month && requestYear == year) {
                filteredRequests.add(registrationRequest);
            }
        }

        if (filteredRequests.isEmpty()) {
            Log.d("RegistrationRequestListAdapter", "No registration requests found for month " + month + " and year " + year);
        } else {
            Log.d("RegistrationRequestListAdapter", "Filtered registration requests for month " + month + " and year " + year + ": " + filteredRequests.size());
        }

        clear();
        addAll(filteredRequests);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<RegistrationRequest> filteredRequests = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented, return all products
                    //filteredProducts.addAll(aProducts);
                    for (RegistrationRequest registrationRequest : mregistrationRequests) {
                            filteredRequests.add(registrationRequest);
                    }
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (RegistrationRequest registrationRequest : mregistrationRequests) {
                        if (registrationRequest.getOwner().getCompany().getName().toLowerCase().contains(filterPattern)) {
                            filteredRequests.add(registrationRequest);
                        }
                    }
                }

                results.values = filteredRequests;
                results.count = filteredRequests.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((ArrayList<RegistrationRequest>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
