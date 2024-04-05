package com.example.projekatmobilneaplikacije.adapters;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.EditServiceAndProductActivity;
import com.example.projekatmobilneaplikacije.model.Category;

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

public class ServiceAndProductListAdapter extends ArrayAdapter<Category> {

    private ArrayList<Category> aCategories;

    public ServiceAndProductListAdapter(Context context, ArrayList<Category> categories) {
        super(context, R.layout.category_card, categories);
        aCategories = categories;
    }

    @Override
    public int getCount() {
        return aCategories.size();
    }
    @Nullable
    @Override
    public Category getItem(int position) {
        return aCategories.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Category category = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_card,
                    parent, false);
        }
        LinearLayout categoryCard = convertView.findViewById(R.id.category_card_item);
        TextView categoryName = convertView.findViewById(R.id.category_name);
        TextView categoryDescription = convertView.findViewById(R.id.category_description);

        if(category != null){
            categoryName.setText(category.getName());
            categoryDescription.setText(category.getDescription());
            categoryCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + category.getName() + ", id: " +
                        category.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + category.getName()  +
                        ", id: " + category.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }

        View itemView = convertView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event

                Intent intent = new Intent(v.getContext(), EditServiceAndProductActivity.class);
                intent.putExtra("id", category.getId());
                v.getContext().startActivity(intent);
            }
        });


        return convertView;
    }
}
