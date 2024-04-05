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
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.SubcategorySuggestion;

import java.util.ArrayList;

public class SubcategorySuggestionListAdapter extends ArrayAdapter<SubcategorySuggestion> {

    private ArrayList<SubcategorySuggestion> aSubcategorySugestions;

    public SubcategorySuggestionListAdapter(Context context, ArrayList<SubcategorySuggestion> subcategorySugestions) {
        super(context, R.layout.subcategory_suggestion_card, subcategorySugestions);
        aSubcategorySugestions = subcategorySugestions;
    }

    @Override
    public int getCount() {
        return aSubcategorySugestions.size();
    }
    @Nullable
    @Override
    public SubcategorySuggestion getItem(int position) {
        return aSubcategorySugestions.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SubcategorySuggestion subcategorySuggestion = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subcategory_suggestion_card,
                    parent, false);
        }
        LinearLayout subcategorySuggestionCard = convertView.findViewById(R.id.subcategory_suggestion_card_item);
        TextView subcategorySuggestionName = convertView.findViewById(R.id.subcategory_suggestion_name);
        TextView subcategorySuggestionType = convertView.findViewById(R.id.subcategory_suggestion_type);

        if(subcategorySuggestionCard != null && subcategorySuggestion != null){
            subcategorySuggestionName.setText(subcategorySuggestion.getName()); // Ovde je izmenjeno
            subcategorySuggestionType.setText(subcategorySuggestion.getSubcategory().toString()); // Ako postoji metoda getType() koja vraća String
            subcategorySuggestionCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + subcategorySuggestion.getName() + ", id: " +
                        subcategorySuggestion.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + subcategorySuggestion.getName()  +
                        ", id: " + subcategorySuggestion.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }

}
