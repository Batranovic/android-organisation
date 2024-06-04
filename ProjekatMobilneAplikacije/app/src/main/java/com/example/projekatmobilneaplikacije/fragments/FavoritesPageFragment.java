package com.example.projekatmobilneaplikacije.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.FavoritesListAdapter;
import com.example.projekatmobilneaplikacije.model.FavoriteItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritesPageFragment extends Fragment {

    private List<FavoriteItem> favoriteItems;
    private FavoritesListAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference favoritesRef = db.collection("favorites");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourites_item_list, container, false);

        ListView listView = view.findViewById(R.id.favorites_list_view);
        favoriteItems = new ArrayList<>();
        adapter = new FavoritesListAdapter(getContext(), favoriteItems);
        listView.setAdapter(adapter);

        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        favoritesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                favoriteItems.clear();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        FavoriteItem favoriteItem = document.toObject(FavoriteItem.class);
                        favoriteItems.add(favoriteItem);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
