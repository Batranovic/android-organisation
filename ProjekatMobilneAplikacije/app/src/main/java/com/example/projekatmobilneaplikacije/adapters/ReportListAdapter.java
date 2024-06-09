package com.example.projekatmobilneaplikacije.adapters;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.NotificationOverviewActivity;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.Report;
import com.example.projekatmobilneaplikacije.model.UserDetails;
import com.example.projekatmobilneaplikacije.activities.ProfileViewActivity;
import com.example.projekatmobilneaplikacije.model.enumerations.ReservationStatus;
import com.example.projekatmobilneaplikacije.model.enumerations.Status;
import com.example.projekatmobilneaplikacije.model.enumerations.UserRole;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ReportListAdapter extends ArrayAdapter<Report> {

    FirebaseUser user;
    FirebaseAuth auth;
    private ArrayList<Report> aReports;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ReportListAdapter(Context context, ArrayList<Report> reports){
        super(context, R.layout.report_card, reports);
        aReports = reports;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Report report = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_card, parent, false);
        }

        LinearLayout reportCard = convertView.findViewById(R.id.report_card);
        TextView reportedBy = convertView.findViewById(R.id.reportedBy);
        TextView reported = convertView.findViewById(R.id.reported);
        TextView reason = convertView.findViewById(R.id.reason);
        TextView date = convertView.findViewById(R.id.date);
        TextView status = convertView.findViewById(R.id.status);
        Button acceptButton = convertView.findViewById(R.id.accept);
        Button rejectButton = convertView.findViewById(R.id.reject);
        Button viewProfile = convertView.findViewById(R.id.viewProfile);
        Button viewReportedEntityProfile = convertView.findViewById(R.id.viewReportedProfile);

        if (report != null) {
            reportedBy.setText(report.getReportedByUsername());
            reported.setText(String.valueOf(report.getReportedEntityUsername()));
            reason.setText(String.valueOf(report.getReason()));
            date.setText(String.valueOf(report.getDate()));
            status.setText(String.valueOf(report.getStatus()));

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("reports").whereEqualTo("id", report.getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String documentId = documentSnapshot.getId();
                                db.collection("reports").document(documentId)
                                        .update("status", "Accepted")
                                        .addOnSuccessListener(aVoid -> {
                                            report.setStatus(Status.Accepted);
                                            status.setText("Accepted");
                                            notifyDataSetChanged();
                                            Toast.makeText(getContext(), "Report accepted", Toast.LENGTH_SHORT).show();

                                            // Block the reported user
                                            blockUser(report.getReportedEntityUsername());

                                            rejectActiveReservations(report.getReportedEntityUsername());
                                            rejectActiveReservationsPupV(report.getReportedEntityUsername());
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error updating status", Toast.LENGTH_SHORT).show());
                                break; // Only update the first matching document
                            }
                        } else {
                            Toast.makeText(getContext(), "Report not found", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error finding report", Toast.LENGTH_SHORT).show());
                }
            });


            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an AlertDialog to get the rejection reason
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Reject Report");

                    // Set up the input
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String rejectionReason = input.getText().toString();

                            // Proceed with rejecting the report and sending the notification
                            db.collection("reports").whereEqualTo("id", report.getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        String documentId = documentSnapshot.getId();
                                        db.collection("reports").document(documentId)
                                                .update("status", "Rejected")
                                                .addOnSuccessListener(aVoid -> {
                                                    report.setStatus(Status.Rejected);
                                                    status.setText("Rejected");
                                                    notifyDataSetChanged();
                                                    Toast.makeText(getContext(), "Report rejected", Toast.LENGTH_SHORT).show();

                                                    // Create a notification
                                                    String notificationId = db.collection("notifications").document().getId();
                                                    Date currentTimestamp = new Date();
                                                    Notification notification = new Notification(
                                                            notificationId,
                                                            "Report Rejected",
                                                            "Your report has been rejected. Reason: " + rejectionReason,
                                                            false,
                                                            currentTimestamp,
                                                            report.getReportedByUsername()
                                                    );

                                                    /*auth = FirebaseAuth.getInstance();
                                                    user = auth.getCurrentUser();
                                                    Log.d("FirebaseAuth", "User Email: " + user.getEmail());
                                                    if (notification.getUsername().equals(user.getEmail())) {
                                                        makeNotification(notification); // Ako trenutni korisnik nije isti kao korisnik kojem je notifikacija namenjena, izlazimo iz metode
                                                    }*/


                                                    // Save the notification to Firestore
                                                    db.collection("notifications").document(notificationId)
                                                            .set(notification)
                                                            .addOnSuccessListener(aVoid1 -> Toast.makeText(getContext(), "Notification sent", Toast.LENGTH_SHORT).show())
                                                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error sending notification", Toast.LENGTH_SHORT).show());

                                                })
                                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error updating status", Toast.LENGTH_SHORT).show());
                                        break; // Only update the first matching document
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Report not found", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error finding report", Toast.LENGTH_SHORT).show());
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });


            viewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("userDetails")
                            .whereEqualTo("username", report.getReportedByUsername())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                                        UserRole userRole = userDetails.getRole();

                                        // Proverite da li je uloga EventOrganizer ili Owner
                                        if (userRole == UserRole.EventOrganizer || userRole == UserRole.Owner || userRole == UserRole.Employee) {
                                            // Otvorite ProfileViewActivity i prosledite podatke
                                            Intent profileIntent = new Intent(getContext(), ProfileViewActivity.class);
                                            profileIntent.putExtra("name", userDetails.getName());
                                            profileIntent.putExtra("surname", userDetails.getSurname());
                                            profileIntent.putExtra("username", userDetails.getUsername());
                                            getContext().startActivity(profileIntent);
                                        } else {
                                            Log.e("User is not an Event Organizer or Owner", "");
                                        }
                                        break;
                                    }
                                } else {
                                    Toast.makeText(getContext(), "User details not found", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error finding user details", Toast.LENGTH_SHORT).show();
                            });
                }
            });

            viewReportedEntityProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("userDetails")
                            .whereEqualTo("username", report.getReportedEntityUsername())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                                        UserRole userRole = userDetails.getRole();

                                        // Proverite da li je uloga EventOrganizer ili Owner
                                        if (userRole == UserRole.EventOrganizer || userRole == UserRole.Owner || userRole == UserRole.Employee) {
                                            // Otvorite ProfileViewActivity i prosledite podatke
                                            Intent profileIntent = new Intent(getContext(), ProfileViewActivity.class);
                                            profileIntent.putExtra("name", userDetails.getName());
                                            profileIntent.putExtra("surname", userDetails.getSurname());
                                            profileIntent.putExtra("username", userDetails.getUsername());
                                            getContext().startActivity(profileIntent);
                                        } else {
                                            Log.e("User is not an Event Organizer or Owner", "");
                                        }
                                        break;
                                    }
                                } else {
                                    Toast.makeText(getContext(), "User details not found", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error finding user details", Toast.LENGTH_SHORT).show();
                            });
                }
            });


        }

        return convertView;
    }

    private void blockUser(String username) {
        db.collection("userDetails").whereEqualTo("username", username).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String documentId = documentSnapshot.getId();
                    db.collection("userDetails").document(documentId)
                            .update("isBlocked", true)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "User blocked", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error blocking user", Toast.LENGTH_SHORT).show());
                    break; // Only update the first matching document
                }
            } else {
                Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error finding user", Toast.LENGTH_SHORT).show());
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



    private void rejectActiveReservations(String username) {
        db.collection("reservations")
                .whereEqualTo("eventOrganizer.username", username)
                .whereIn("status", Arrays.asList(ReservationStatus.New, ReservationStatus.Accepted)) // Filtriranje statusa
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        WriteBatch batch = db.batch(); // Koristimo batch za grupisano ažuriranje
                        List<String> employeeEmails = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            DocumentReference docRef = documentSnapshot.getReference();
                            batch.update(docRef, "status", ReservationStatus.CancelledByAdmin); // Ažuriramo status na CancelledByAdmin

                            // Prikupimo sve employee tokene za notifikaciju
                            Map<String, Object> reservationData = documentSnapshot.getData();
                            Map<String, Object> employeeData = (Map<String, Object>) reservationData.get("employee");
                            if (employeeData != null) {
                                String employeeEmail = (String) employeeData.get("email");
                                if (employeeEmail != null) {
                                    employeeEmails.add(employeeEmail);
                                }
                            }
                        }
                        batch.commit().addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "All active reservations rejected", Toast.LENGTH_SHORT).show();
                            for (String email : employeeEmails) {
                                // Create a notification
                                String notificationId = db.collection("notifications").document().getId();
                                Date currentTimestamp = new Date();
                                Notification notification = new Notification(
                                        notificationId,
                                        "Reservation Cancellation",
                                        "Your reservation has been cancelled by the admin. ",
                                        false,
                                        currentTimestamp,
                                        email
                                );

                                // Save the notification to Firestore
                                db.collection("notifications").document(notificationId)
                                        .set(notification)
                                        .addOnSuccessListener(aVoid1 -> Toast.makeText(getContext(), "Notification sent", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error sending notification", Toast.LENGTH_SHORT).show());

                                //makeNotification(notification);

                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Error rejecting reservations", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Toast.makeText(getContext(), "No active reservations found for the user", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error finding reservations", Toast.LENGTH_SHORT).show());
    }

    //radi
    private void checkIfUserIsOwner(String reportedEntityUsername, OnRoleCheckListener listener) {
        db.collection("userDetails")
                .whereEqualTo("username", reportedEntityUsername)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                            if (userDetails.getRole() == UserRole.Owner) {
                                listener.onRoleCheck(true);
                            } else {
                                listener.onRoleCheck(false);
                            }
                        }
                    } else {
                        listener.onRoleCheck(false);
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onRoleCheck(false);
                });
    }

    public interface OnRoleCheckListener {
        void onRoleCheck(boolean isOwner);
    }

    private void rejectActiveReservationsPupV(String username) {
        checkIfUserIsOwner(username, isOwner -> {
            if (isOwner) {  //uspesno proveri da li je owner
                db.collection("reservations")
                        .whereEqualTo("employee.email", username)
                        .whereIn("status", Arrays.asList(ReservationStatus.New, ReservationStatus.Accepted))
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                WriteBatch batch = db.batch(); // Koristimo batch za grupisano ažuriranje
                                List<String> eventOrganizerUsernames = new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    DocumentReference docRef = documentSnapshot.getReference();
                                    batch.update(docRef, "status", ReservationStatus.CancelledByAdmin); // Ažuriramo status na CancelledByAdmin

                                    // Prikupimo sve employee username za notifikaciju
                                    Map<String, Object> reservationData = documentSnapshot.getData();
                                    Map<String, Object> eventOrganizerData = (Map<String, Object>) reservationData.get("eventOrganizer");
                                    if (eventOrganizerData != null) {
                                        String eventOrganizerUsername = (String) eventOrganizerData.get("username");
                                        if (eventOrganizerUsername != null) {
                                            eventOrganizerUsernames.add(eventOrganizerUsername);
                                        }
                                    }
                                }
                                batch.commit().addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "All active reservations rejected", Toast.LENGTH_SHORT).show();
                                    for (String eventOrganizerUsername : eventOrganizerUsernames) {
                                        // Create a notification
                                        String notificationId = db.collection("notifications").document().getId();
                                        Date currentTimestamp = new Date();
                                        Notification notification = new Notification(
                                                notificationId,
                                                "Reservation Cancellation",
                                                "Your reservation has been cancelled by the admin. ",
                                                false,
                                                currentTimestamp,
                                                eventOrganizerUsername
                                        );

                                        // Save the notification to Firestore
                                        db.collection("notifications").document(notificationId)
                                                .set(notification)
                                                .addOnSuccessListener(aVoid1 -> Toast.makeText(getContext(), "Notification sent", Toast.LENGTH_SHORT).show())
                                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error sending notification", Toast.LENGTH_SHORT).show());

                                        makeNotification(notification);

                                    }
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Error rejecting reservations", Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                Toast.makeText(getContext(), "No active reservations found for the user", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error finding reservations", Toast.LENGTH_SHORT).show());
            } else {

            }
        });
    }



}
