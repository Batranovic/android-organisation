package com.example.projekatmobilneaplikacije.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.HomeActivity;
import com.example.projekatmobilneaplikacije.activities.NotificationOverviewActivity;
import com.example.projekatmobilneaplikacije.activities.RegistrationActivity;
import com.example.projekatmobilneaplikacije.adapters.ReportListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentLoginBinding;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.Reservation;
import com.example.projekatmobilneaplikacije.model.UserDetails;
import com.example.projekatmobilneaplikacije.model.enumerations.UserRole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    EditText username, password_log;
    FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null) {

            db.collection("userDetails")
                    .whereEqualTo("username", currentUser.getEmail())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Ako postoji rezultat, preuzmite prvi dokument (trebalo bi da bude samo jedan)
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                // Preuzmite UserDetails iz dokumenta
                                UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);

                                if (userDetails!=null && currentUser.isEmailVerified() && !userDetails.getIsBlocked()) {
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(requireContext(), "Is blocked" + userDetails.getIsBlocked(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else {
                            Log.w("Firestore", "Error logging in.", task.getException());
                        }
                    });
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        username = root.findViewById(R.id.username);
        password_log = root.findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        //dugme iz fragment_login.xml
        Button registerButton = root.findViewById(R.id.register);
        Button logInButton = root.findViewById(R.id.login);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prlazim sa ove aktivnosti na RegistrationActivity pritiskom na dugme registerButton
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(username.getText());
                password = String.valueOf(password_log.getText());

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(requireContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task <AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if (currentUser != null) {
                                        if (currentUser.isEmailVerified()) {
                                            db.collection("userDetails")
                                                    .whereEqualTo("username", currentUser.getEmail())
                                                    .get()
                                                    .addOnCompleteListener(tasks -> {
                                                        if (tasks.isSuccessful()) {
                                                            QuerySnapshot querySnapshot = tasks.getResult();
                                                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                                // Ako postoji rezultat, preuzmite prvi dokument (trebalo bi da bude samo jedan)
                                                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                                                // Preuzmite UserDetails iz dokumenta
                                                                UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);

                                                                if (userDetails!=null && currentUser.isEmailVerified() && !userDetails.getIsBlocked()) {
                                                                    Toast.makeText(requireContext(), "Is blocked " + userDetails.getIsBlocked(), Toast.LENGTH_SHORT).show();
                                                                    Toast.makeText(requireContext(), "Authentication success", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                                    startActivity(intent);
                                                                }else {
                                                                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        } else {
                                                            Log.w("Firestore", "Error logging in.", tasks.getException());
                                                        }
                                                    });
                                         //   Toast.makeText(requireContext(), "Authentication success", Toast.LENGTH_SHORT).show();
                                            //Intent intent = new Intent(getActivity(), HomeActivity.class);
                                          //  startActivity(intent);
                                            reminderNotification(currentUser.getEmail());
                                            Toast.makeText(requireContext(), "Authentication success", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                                            getNotificationsForUser(currentUser.getEmail());


                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(requireContext(), "Please verify your email to log in.", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut(); // Odjava korisnika ako nije verifikovao email
                                        }
                                    }
                                } else {
                                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        return root;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void getNotificationsForUser(String username){
        db.collection("notifications")
                .whereEqualTo("username", username)
                .whereEqualTo("read",false)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Ovde možete izvršiti željenu logiku za svaku notifikaciju
                            // Na primer, možete dobiti objekat notifikacije i raditi sa njim
                            // Primer:
                            Notification notification = documentSnapshot.toObject(Notification.class);
                            makeNotification(notification);
                        }
                    } else {
                        Log.e("Notification", "No notifications found for user: " + username);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Notification", "Failed to get notifications: " + e.getMessage());
                });
    }

    private void reminderNotification(String username) {
        // Trenutno vreme kada se korisnik prijavio
        Calendar currentTime = Calendar.getInstance();

        // Vreme kada se korisnik prijavio plus jedan sat
        Calendar oneHourLater = Calendar.getInstance();
        oneHourLater.add(Calendar.HOUR_OF_DAY, 1);

        db.collection("reservations")
                .whereEqualTo("eventOrganizer.username", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Reservation reservation = documentSnapshot.toObject(Reservation.class);
                            Calendar eventTime = Calendar.getInstance();
                            eventTime.setTime(reservation.getEvent().getDate());

                            // Provera da li je vreme događaja unutar narednog sata od trenutka prijave korisnika
                            if (eventTime.after(currentTime) && eventTime.before(oneHourLater)) {

                                String notificationId = db.collection("notifications").document().getId();
                                Date currentTimestamp = new Date();
                                Notification notification = new Notification(
                                        notificationId,
                                        "Reminder of reservation",
                                        "Event in one hour or less: " + reservation.getEvent().getDate(),
                                        false,
                                        currentTimestamp,
                                        username
                                );

                                db.collection("notifications").document(notificationId)
                                        .set(notification)
                                        .addOnSuccessListener(aVoid1 -> Toast.makeText(getContext(), "Notification sent", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error sending notification", Toast.LENGTH_SHORT).show());

                                getNotificationsForUser(username);
                            }

                        }
                    } else {
                        Log.e("Notification", "No notifications found for user: " + username);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Notification", "Failed to get notifications: " + e.getMessage());
                });
    }



    public void makeNotification(Notification notification) {
        String channelID = "CHANNEL_ID_NOTIFICATION";
        Context context = getContext();
        if (context == null) {
            Log.e("Notification", "Context is null");
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID);
        builder.setSmallIcon(R.drawable.notification)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getMessage())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(context, NotificationOverviewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", "Some value");

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID, "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
                Log.d("Notification", "Notification channel created: " + channelID);
            } else {
                Log.d("Notification", "Notification channel already exists: " + channelID);
            }
        }

        // Generisanje jedinstvenog ID-a za svaku notifikaciju
        int notificationId = (int) System.currentTimeMillis() + (int) (Math.random() * 1000);
        notificationManager.notify(notificationId, builder.build());

        Log.d("Notification", "Notification sent with ID: " + notificationId);
    }

}