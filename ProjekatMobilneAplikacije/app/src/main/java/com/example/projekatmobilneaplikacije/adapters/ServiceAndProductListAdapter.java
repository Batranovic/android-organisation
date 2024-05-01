package com.example.projekatmobilneaplikacije.adapters;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.EditServiceAndProductActivity;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.Subcategory;

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

import java.util.ArrayList;

public class ServiceAndProductListAdapter extends ArrayAdapter<Object> {
    private ArrayList<Object> mData;
    private Context mContext; // Dodato polje za Context

    public ServiceAndProductListAdapter(Context context, ArrayList<Object> data) {
        super(context, R.layout.category_card, data);
        mContext = context; // Inicijalizacija Context-a
        mData = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Object item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_card, parent, false);
        }

        LinearLayout categoryCard = convertView.findViewById(R.id.category_card_item);
        TextView categoryName = convertView.findViewById(R.id.category_name);
        TextView categoryDescription = convertView.findViewById(R.id.category_description);

        if (item instanceof Category) {
            Category category = (Category) item;
            categoryName.setText(category.getName());
            categoryDescription.setText(category.getDescription());
        } else if (item instanceof Subcategory) {
            Subcategory subcategory = (Subcategory) item;
            categoryName.setText(subcategory.getName());
            categoryDescription.setText(subcategory.getDescription());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obrada klika na kategoriju ili podkategoriju
                if (item instanceof Category) {
                    Category clickedCategory = (Category) item;
                    // Obrada klika na kategoriju

                    // Prelazak na novi intent za EditServiceAndProductActivity za kategoriju
                    Intent intent = new Intent(mContext, EditServiceAndProductActivity.class);
                    // Ovde možete proslediti dodatne informacije ako je potrebno
                    mContext.startActivity(intent);
                } else if (item instanceof Subcategory) {
                    Subcategory clickedSubcategory = (Subcategory) item;
                    // Obrada klika na podkategoriju

                    // Prelazak na novi intent za EditServiceAndProductActivity za podkategoriju
                    Intent intent = new Intent(mContext, EditServiceAndProductActivity.class);
                    // Ovde možete proslediti dodatne informacije ako je potrebno
                    mContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }
}

