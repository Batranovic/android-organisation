package com.example.projekatmobilneaplikacije.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.UserNotificationsAdapter;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserNotificationsFragment extends ListFragment implements SensorEventListener {

    private static final String ARG_PARAM = "param";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser loggedInUser = mAuth.getCurrentUser();

    private UserNotificationsAdapter adapter;

    public static ArrayList<Notification> notifications = new ArrayList<>();


    ListView listView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notificationsRef = db.collection("notifications");
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime;
    private static final int SHAKE_THRESHOLD = 700;
    private static final int SHAKE_INTERVAL = 3000; // Interval između dva shake događaja
    private Vibrator vibrator;

    private float lastX, lastY, lastZ;

    private enum NotificationFilter {
        ALL,
        READ,
        UNREAD
    }

    private NotificationFilter currentFilter = NotificationFilter.ALL;

    public UserNotificationsFragment() {
        // Required empty public constructor
    }

    public static UserNotificationsFragment newInstance() {
        UserNotificationsFragment fragment = new UserNotificationsFragment();
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
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new UserNotificationsAdapter(getActivity(), notifications);
        setListAdapter(adapter);

        listView = view.findViewById(android.R.id.list);

        String username = loggedInUser.getEmail();
        fetchNotificationsFromFirestore(username);

        Button btnRead = view.findViewById(R.id.btnRead);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = NotificationFilter.READ;
                updateDisplayedNotifications();
            }
        });

        Button btnUnread = view.findViewById(R.id.btnUnread);
        btnUnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = NotificationFilter.UNREAD;
                updateDisplayedNotifications();
            }
        });

        Button btnAll = view.findViewById(R.id.btnAll);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = NotificationFilter.ALL;
                updateDisplayedNotifications();
            }
        });

        listView.setAdapter(adapter);
    }

    private void updateDisplayedNotifications() {
        ArrayList<Notification> filteredNotifications = new ArrayList<>();

        switch (currentFilter) {
            case ALL:
                filteredNotifications.addAll(notifications);
                break;
            case READ:
                for (Notification notification : notifications) {
                    if (notification.isRead()) {
                        filteredNotifications.add(notification);
                    }
                }
                break;
            case UNREAD:
                for (Notification notification : notifications) {
                    if (!notification.isRead()) {
                        filteredNotifications.add(notification);
                    }
                }
                break;
        }

        adapter = new UserNotificationsAdapter(getActivity(), filteredNotifications);
        setListAdapter(adapter);
    }

    private void fetchNotificationsFromFirestore(String username) {
        notificationsRef.whereEqualTo("username", username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                notifications.clear();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Notification notification = documentSnapshot.toObject(Notification.class);
                        notifications.add(notification);
                    }
                    updateDisplayedNotifications(); // Update displayed notifications after fetching
                } else {
                    Log.d("UserNotificationsFragment", "No notifications found");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastShakeTime) > SHAKE_INTERVAL) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float deltaX = x - lastX;
                float deltaY = y - lastY;
                float deltaZ = z - lastZ;

                lastX = x;
                lastY = y;
                lastZ = z;

                double acceleration = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / SensorManager.GRAVITY_EARTH;
                Log.d("UserNotificationsFragment", "Acceleration: " + acceleration);

                if (acceleration > 2) { // adjust this threshold as necessary
                    lastShakeTime = currentTime;
                    handleShakeEvent();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ne koristi se u ovom primeru
    }

    private void handleShakeEvent() {
        vibrator.vibrate(100); // Vibracija kao povratna informacija o shake događaju

        switch (currentFilter) {
            case ALL:
                currentFilter = NotificationFilter.UNREAD;
                break;
            case UNREAD:
                currentFilter = NotificationFilter.READ;
                break;
            case READ:
                currentFilter = NotificationFilter.ALL;
                break;
        }

        updateDisplayedNotifications();
    }
}
