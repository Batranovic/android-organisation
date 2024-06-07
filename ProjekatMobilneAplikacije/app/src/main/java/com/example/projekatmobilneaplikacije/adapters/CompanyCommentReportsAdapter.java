package com.example.projekatmobilneaplikacije.adapters;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.CompanyReview;
import com.example.projekatmobilneaplikacije.model.CompanyReviewReport;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.enumerations.CompanyReviewReportStatus;
import com.example.projekatmobilneaplikacije.model.enumerations.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyCommentReportsAdapter extends ArrayAdapter<CompanyReviewReport> {
    private ArrayList<CompanyReviewReport> aReports;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context mContext;

    private String userRole;
    private String username;


    FirebaseAuth auth;
    FirebaseUser currentUser;

    Button acceptButton;
    Button declineButton;

    public CompanyCommentReportsAdapter(Context context, ArrayList<CompanyReviewReport> reports){
        super(context, R.layout.company_review_report_card, reports);
        aReports = reports;
        mContext = context;

    }

    @Override
    public int getCount() {
        return aReports.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public CompanyReviewReport getItem(int position) {
        return aReports.get(position);
    }

    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra da bude identifikator.
     * Naravno mozemo iskoristiti i jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CompanyReviewReport report = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_review_report_card,
                    parent, false);
        }
        LinearLayout reportCard = convertView.findViewById(R.id.company_review_report_card);
        TextView reason = convertView.findViewById(R.id.reason);
        TextView status = convertView.findViewById(R.id.status);
        TextView user = convertView.findViewById(R.id.user);
        TextView date = convertView.findViewById(R.id.report_date);
        acceptButton = convertView.findViewById(R.id.accept_button);
        declineButton = convertView.findViewById(R.id.decline_button);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            username = currentUser.getEmail();
        }

        if(report != null){
            reason.setText(report.getReason());
            status.setText(report.getStatus().toString());
            user.setText(report.getOwner());

            Date reportDate = report.getReportDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String fromDateString = dateFormat.format(reportDate);
            date.setText(fromDateString);

            reportCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("App", "Clicked: " + report.getReason() + ", id: " +
                        report.getId());
            });

            acceptButton.setOnClickListener(v -> {
                db.collection("comments")
                        .whereEqualTo("id", report.getCommentId())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    String documentId = documentSnapshot.getId();
                                    db.collection("comments").document(documentId)
                                            .update("isDeleted", true)
                                            .addOnSuccessListener(aVoid -> {
                                                // Ažuriranje statusa reporta na Accepted
                                                db.collection("companyReviewReports").whereEqualTo("id", report.getId()).get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                    if (!queryDocumentSnapshots1.isEmpty()) {
                                                        for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots1) {
                                                            String reportDocumentId = documentSnapshot1.getId();
                                                            db.collection("companyReviewReports").document(reportDocumentId)
                                                                    .update("status", "Accepted")
                                                                    .addOnSuccessListener(aVoid1 -> {
                                                                        report.setStatus(CompanyReviewReportStatus.Accepted);
                                                                        status.setText("Accepted");
                                                                        notifyDataSetChanged();
                                                                        Toast.makeText(getContext(), "Review Report accepted", Toast.LENGTH_SHORT).show();
                                                                    })
                                                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error updating status", Toast.LENGTH_SHORT).show());
                                                            break;
                                                        }
                                                    } else {
                                                        Toast.makeText(getContext(), "Review Report not found", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error finding report", Toast.LENGTH_SHORT).show());
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error updating comment", Toast.LENGTH_SHORT).show());
                                    break;
                                }
                            } else {
                                Toast.makeText(getContext(), "Comment not found", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error finding comment", Toast.LENGTH_SHORT).show());
            });

            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an AlertDialog to get the rejection reason
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Decline Review Report");

                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String rejectionReason = input.getText().toString();

                            // Proceed with rejecting the report and sending the notification
                            db.collection("companyReviewReports").whereEqualTo("id", report.getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        String documentId = documentSnapshot.getId();
                                        db.collection("companyReviewReports").document(documentId)
                                                .update("status", "Declined")
                                                .addOnSuccessListener(aVoid -> {
                                                    report.setStatus(CompanyReviewReportStatus.Declined);
                                                    status.setText("Declined");
                                                    notifyDataSetChanged();
                                                    Toast.makeText(getContext(), "Review Report declined", Toast.LENGTH_SHORT).show();

                                                    //loadReports();

                                                    // Create a notification
                                                    String notificationId = db.collection("notifications").document().getId();
                                                    Date currentTimestamp = new Date();
                                                    Notification notification = new Notification(
                                                            notificationId,
                                                            "Review Report Declines",
                                                            "Your report has been rejected. Reason: " + rejectionReason,
                                                            false,
                                                            currentTimestamp,
                                                            report.getOwner()
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
                                    Toast.makeText(getContext(), "Review Report not found", Toast.LENGTH_SHORT).show();
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

    public void loadReports() {
        db.collection("companyReviewReports")
                .whereEqualTo("status", CompanyReviewReportStatus.Reported.toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<CompanyReviewReport> reportsList = new ArrayList<>();


                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            CompanyReviewReport report = document.toObject(CompanyReviewReport.class);
                            reportsList.add(report);
                        }

                        aReports.clear();
                        aReports.addAll(reportsList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error loading reports", e);
                    }
                });
    }


}

