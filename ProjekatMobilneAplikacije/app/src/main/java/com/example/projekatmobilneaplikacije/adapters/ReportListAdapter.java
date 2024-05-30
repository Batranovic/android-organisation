package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.Report;
import com.example.projekatmobilneaplikacije.model.enumerations.Status;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

public class ReportListAdapter extends ArrayAdapter<Report> {
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



}
