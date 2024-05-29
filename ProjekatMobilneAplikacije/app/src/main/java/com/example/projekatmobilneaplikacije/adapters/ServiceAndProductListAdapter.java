package com.example.projekatmobilneaplikacije.adapters;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.EditServiceAndProductActivity;
import com.example.projekatmobilneaplikacije.activities.EditSubcategoryActivity;
import com.example.projekatmobilneaplikacije.model.Category;
import com.example.projekatmobilneaplikacije.model.Subcategory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ServiceAndProductListAdapter extends ArrayAdapter<Object> {
    private ArrayList<Object> mData;
    private Context mContext;

    public ServiceAndProductListAdapter(Context context, ArrayList<Object> data) {
        super(context, R.layout.category_card, data);
        mContext = context;
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

            categoryCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EditServiceAndProductActivity.class);
                    intent.putExtra("category_name", category.getName());
                    intent.putExtra("category_description", category.getDescription());
                    intent.putExtra("old_name", category.getName());
                    mContext.startActivity(intent);
                }
            });
        } else if (item instanceof Subcategory) {
            Subcategory subcategory = (Subcategory) item;
            categoryName.setText(subcategory.getName());
            categoryDescription.setText(subcategory.getDescription());

            categoryCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subcategoryName = subcategory.getName();
                    String subcategoryDescription = subcategory.getDescription();
                    String subcategoryType = subcategory.getSubcategoryType().toString();
                    String subcategoryCategoryName = subcategory.getCategory().getName();




                    Intent intent = new Intent(mContext, EditSubcategoryActivity.class);
                    intent.putExtra("subcategory_name", subcategoryName);
                    intent.putExtra("subcategory_description", subcategoryDescription);
                    intent.putExtra("subcategory_type", subcategoryType);
                    intent.putExtra("subcategory_category_name", subcategoryCategoryName);
                    intent.putExtra("old_subname", subcategoryName);

                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
