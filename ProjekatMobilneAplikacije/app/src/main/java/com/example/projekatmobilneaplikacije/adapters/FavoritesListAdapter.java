package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.FavoriteDetailActivity;
import com.example.projekatmobilneaplikacije.model.FavoriteItem;

import java.util.List;

public class FavoritesListAdapter extends ArrayAdapter<FavoriteItem> {

    private Context mContext;
    private List<FavoriteItem> favoriteItems;

    public FavoritesListAdapter(@NonNull Context context, @NonNull List<FavoriteItem> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.favoriteItems = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FavoriteItem favoriteItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.favourites_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.text_view_titlef);

        if (favoriteItem != null) {
            titleTextView.setText(favoriteItem.getTitle());
        }

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, FavoriteDetailActivity.class);
            intent.putExtra("id", favoriteItem.getId());
            intent.putExtra("title", favoriteItem.getTitle());
            mContext.startActivity(intent);
        });

        return convertView;
    }
}
