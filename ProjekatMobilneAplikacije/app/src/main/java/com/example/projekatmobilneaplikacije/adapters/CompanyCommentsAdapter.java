package com.example.projekatmobilneaplikacije.adapters;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeProfileActivity;
import com.example.projekatmobilneaplikacije.model.CompanyReview;
import com.example.projekatmobilneaplikacije.model.CompanyReviewReport;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.RegistrationRequest;
import com.example.projekatmobilneaplikacije.model.enumerations.CompanyReviewReportStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CompanyCommentsAdapter extends ArrayAdapter<CompanyReview> {
    private ArrayList<CompanyReview> aComments;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context mContext;

    private String userRole;
    private String username;


    FirebaseAuth auth;
    FirebaseUser currentUser;

    Button reportButton;

    public CompanyCommentsAdapter(Context context, ArrayList<CompanyReview> comments){
        super(context, R.layout.company_comment_card, comments);
        aComments = comments;
        mContext = context;

    }

    @Override
    public int getCount() {
        return aComments.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public CompanyReview getItem(int position) {
        return aComments.get(position);
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

    /*
     * Ova metoda popunjava pojedinacan element ListView-a podacima.
     * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
     * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
     * uzece java objekat sa odredjene pozicije (model) koji cuva podatke,
     * i layout koji treba da prikaze te podatke (view) npr R.layout.product_card.
     * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
     * popuniti view podacima i poslati listview da prikaze, i nastavice
     * sledecu iteraciju.
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CompanyReview review = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_comment_card,
                    parent, false);
        }
        LinearLayout reviewCard = convertView.findViewById(R.id.company_comment_card);
        TextView text = convertView.findViewById(R.id.comment);
        TextView grade = convertView.findViewById(R.id.grade);
        TextView user = convertView.findViewById(R.id.user);
        TextView date = convertView.findViewById(R.id.comment_date);
        reportButton = convertView.findViewById(R.id.report_button);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            username = currentUser.getEmail();
            if (username == null) {
                Log.e("ReservationListAdapter", "Username is null");
            }

            // Use a query to find the document with the specified username field
            db.collection("userDetails")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    if (documentSnapshot.exists()) {
                                        userRole = documentSnapshot.getString("role");
                                        if ("Owner".equals(userRole)) {
                                            //reportButton.setVisibility(View.VISIBLE);
                                        } else {
                                            //reportButton.setVisibility(View.GONE);
                                        }
                                        Log.d("ReservationListAdapter", "User role: " + userRole);
                                        return;
                                    }
                                }
                            } else {
                                Log.d("ReservationListAdapter", "User document not found for username: " + username);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ReservationListAdapter", "Error fetching user role", e);
                        }
                    });
        } else {
            Log.e("ReservationListAdapter", "User not logged in");
        }

        if(review != null){
            text.setText(review.getText());
            grade.setText(String.valueOf(review.getGrade()));
            user.setText(review.getEventOrganizer());

            Date reviewDate = review.getReviewDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String fromDateString = dateFormat.format(reviewDate);
            date.setText(fromDateString);

            reviewCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("App", "Clicked: " + review.getText() + ", id: " +
                        review.getId());
            });





            reportButton.setOnClickListener(v -> {
                if (!"Owner".equals(userRole)) {
                    Toast.makeText(mContext, "Not allowed", Toast.LENGTH_SHORT).show();
                    return;
                }
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.FullScreenBottomSheetDialog);
                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_comment_report, null);
                bottomSheetDialog.setContentView(dialogView);


                EditText reason = dialogView.findViewById(R.id.editTextReason);
                Button submitButton = dialogView.findViewById(R.id.applyFilterButton);

                submitButton.setOnClickListener(view -> {
                    String reportReason = reason.getText().toString().trim();
                    if (!reportReason.isEmpty()) {
                        createReport(review.getId(), reportReason);
                        bottomSheetDialog.dismiss();
                    } else {
                        Toast.makeText(mContext, "Please provide a reason for the report", Toast.LENGTH_SHORT).show();
                    }
                });

                bottomSheetDialog.show();
            });


    }


        return convertView;
    }

    private void createReport(String reviewId, String reason) {
        String reportId = generateReviewId();
        Date reportDate = new Date();
        CompanyReviewReport report = new CompanyReviewReport(reportId, currentUser.getEmail(), reviewId, reason, reportDate, CompanyReviewReportStatus.Reported);
        db.collection("companyReviewReports")
                .add(report)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Report added with ID: " + documentReference.getId());

                        String notificationId = db.collection("notifications").document().getId();
                        Date currentTimestamp = new Date();
                        Notification notification = new Notification(
                                notificationId,
                                "New company review report",
                                "Owner " + currentUser.getEmail() + " reported comment: " + reviewId,
                                false,
                                currentTimestamp,
                                "batranovicnina@gmail.com"
                        );

                        db.collection("notifications").document(notificationId)
                                .set(notification)
                                .addOnSuccessListener(aVoid1 -> Toast.makeText(getContext(), "Notification sent", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error sending notification", Toast.LENGTH_SHORT).show());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding comment", e);

                    }
                });
    }

    public void filterByDate(int day, int month, int year) {
        ArrayList<CompanyReview> filteredReviews = new ArrayList<>();
        for (CompanyReview review : aComments) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(review.getReviewDate());
            int reviewDay = calendar.get(Calendar.DAY_OF_MONTH);
            int reviewMonth = calendar.get(Calendar.MONTH) + 1;
            int reviewYear = calendar.get(Calendar.YEAR);

            if (reviewMonth == month && reviewDay == day && reviewYear == year) {
                filteredReviews.add(review);
            }
        }

        if (filteredReviews.isEmpty()) {
            Log.d("CompanyCommentsAdapter", "No reviews found for month " + month);
        } else {
            Log.d("CompanyCommentsAdapter", "Filtered registration requests for month " + month + ": " + filteredReviews.size());
        }

        clear();
        addAll(filteredReviews);
        notifyDataSetChanged();
    }

    private String generateReviewId() {
        return Long.toString(System.currentTimeMillis());
    }

    private void fetchUserRole() {
        if (username == null) {
            Log.e("ReservationListFragment", "Username is null");
            return;
        }

        db.collection("userDetails")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    userRole = documentSnapshot.getString("role");
                                    Log.d("ReservationListFragment", "User role: " + userRole);
                                    if (!"Owner".equals(userRole)) {
                                        if (reportButton != null) {
                                            reportButton.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                    return;
                                }
                            }
                        } else {
                            Log.d("CompanyCommentsAdapter", "User document not found for username: " + username);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CompanyCommentsAdapter", "Error fetching user role", e);
                    }
                });
    }

}
