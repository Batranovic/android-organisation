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

import com.example.projekatmobilneaplikacije.model.Subcategory;


import java.util.ArrayList;

public class SubcategoryForEventAdapter extends ArrayAdapter<Subcategory> {

    private ArrayList<Subcategory> aSubcategorys;

    public SubcategoryForEventAdapter(Context context, ArrayList<Subcategory> subcategorys) {
        super(context, R.layout.one_subcategory_forevent, subcategorys);
        aSubcategorys = subcategorys;
    }

    @Override
    public int getCount() {
        return aSubcategorys.size();
    }
    @Nullable
    @Override
    public Subcategory getItem(int position) {
        return aSubcategorys.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Subcategory subcategory = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_subcategory_forevent,
                    parent, false);
        }
        LinearLayout subcategoryCard = convertView.findViewById(R.id.one_event);
        TextView subcategoryName = convertView.findViewById(R.id.event_name);
        TextView subcategoryDescription = convertView.findViewById(R.id.event_description);

        if(subcategoryCard != null && subcategory != null){
            subcategoryName.setText(subcategory.getName()); // Ovde je izmenjeno
            subcategoryDescription.setText(subcategory.getDescription()); // Ako postoji metoda getType() koja vraća String
            subcategoryCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'

            });
        }

        return convertView;
    }

}

