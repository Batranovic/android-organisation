package com.example.projekatmobilneaplikacije.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.ReportListAdapter;
import com.example.projekatmobilneaplikacije.adapters.UserNotificationsAdapter;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.Report;
import com.example.projekatmobilneaplikacije.model.enumerations.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserNotificationsFragment extends ListFragment {

    private static final String ARG_PARAM = "param";

    private String mParam1;
    private String mParam2;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser loggedInUser = mAuth.getCurrentUser();

    private UserNotificationsAdapter adapter;

    public static ArrayList<Notification> notifications = new ArrayList<Notification>();

    ListView listView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notificationsRef = db.collection("notifications");

    public UserNotificationsFragment() {
        // Required empty public constructor
    }

    public static UserNotificationsFragment newInstance(String param1, String param2) {
        UserNotificationsFragment fragment = new UserNotificationsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, notifications);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notifications = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new UserNotificationsAdapter(getActivity(), notifications);
            setListAdapter(adapter);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //prepareProductList(products);

        adapter = new UserNotificationsAdapter(getActivity(), notifications);
        // Set adapter for the ListFragment
        setListAdapter(adapter);

        listView = view.findViewById(android.R.id.list);

        String username = loggedInUser.getEmail();

        notifications = fetchNotificationsFromFirestore(username);
        adapter = new UserNotificationsAdapter(getActivity(), notifications);
        setListAdapter(adapter);



        Button btnRead = view.findViewById(R.id.btnRead);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Notification> readNotifications = new ArrayList<>();
                // Filter notifications where isRead is true
                for (Notification notification : notifications) {
                    if (notification.isRead()) {
                        readNotifications.add(notification);
                    }
                }
                // Update adapter with filtered list
                adapter = new UserNotificationsAdapter(getActivity(), readNotifications);
                setListAdapter(adapter);
            }
        });

        Button btnUnread = view.findViewById(R.id.btnUnread);
        btnUnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Notification> unreadNotifications = new ArrayList<>();
                for (Notification notification : notifications) {
                    if (!notification.isRead()) {
                        unreadNotifications.add(notification);
                    }
                }
                // Update adapter with filtered list
                adapter = new UserNotificationsAdapter(getActivity(), unreadNotifications);
                setListAdapter(adapter);
            }
        });

        Button btnAll = view.findViewById(R.id.btnAll);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifications = fetchNotificationsFromFirestore(username);
                // Update adapter with fetched notifications
                adapter = new UserNotificationsAdapter(getActivity(), notifications);
                setListAdapter(adapter);
            }
        });

        listView.setAdapter(adapter);

    }

    private ArrayList<Notification> fetchNotificationsFromFirestore(String username) {
        final ArrayList<Notification> fetchedNotifications = new ArrayList<>();

        notificationsRef.whereEqualTo("username", username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                fetchedNotifications.clear();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Notification notification = documentSnapshot.toObject(Notification.class);
                        fetchedNotifications.add(notification);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("UserNotificationsFragment", "No notifications found");
                }
            }
        });

        return fetchedNotifications;
    }


}