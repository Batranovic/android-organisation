package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.EditSubcategoryActivity;
import com.example.projekatmobilneaplikacije.activities.HomeActivity;
import com.example.projekatmobilneaplikacije.fragments.UserNotificationsFragment;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.Report;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserNotificationsAdapter extends ArrayAdapter<Notification> {
    private ArrayList<Notification> aNotifications;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserNotificationsAdapter(Context context, ArrayList<Notification> notifications){
        super(context, R.layout.notification_card, notifications);
        aNotifications = notifications;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Notification notification = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_card, parent, false);
        }

        LinearLayout notificationCard = convertView.findViewById(R.id.notification_card);
        TextView title = convertView.findViewById(R.id.title);
        TextView message = convertView.findViewById(R.id.message);
        TextView date = convertView.findViewById(R.id.date);


        if (notification != null) {
            title.setText(notification.getTitle());
            message.setText(String.valueOf(notification.getMessage()));
            date.setText(String.valueOf(notification.getDate()));
        }
        notificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Postavite status notifikacije na "read"
                if (notification != null) {
                    notification.setRead(true);
                    updateNotificationStatus(notification);

                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }


    private void updateNotificationStatus(Notification notification){
        db.collection("notifications")
                .whereEqualTo("id", notification.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            DocumentReference docRef = documentSnapshot.getReference();
                            docRef.update("read", true)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getContext(), "Read", Toast.LENGTH_SHORT).show();

                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Unread", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(getContext(), "Not read", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Not read", Toast.LENGTH_SHORT).show();

                });

    }
}
