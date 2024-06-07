package com.example.projekatmobilneaplikacije.fragments.company;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.CompanyReview;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewCompanyComment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewCompanyComment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference commentsRef = db.collection("comments");

    private String userRole;
    private String username;

    FirebaseAuth auth;
    FirebaseUser user;


    public AddNewCompanyComment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewCompanyComment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewCompanyComment newInstance(String param1, String param2) {
        AddNewCompanyComment fragment = new AddNewCompanyComment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_company_comment, container, false);

        Button newCommentButton = view.findViewById(R.id.button_add_comment);
        EditText commentEditText = view.findViewById(R.id.edit_text_comment);
        RatingBar ratingBar = view.findViewById(R.id.rating_bar);

        newCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEditText.getText().toString();
                double rating = ratingBar.getRating();
                String company = "komp@gmail.com";
                String owner = "andjela1108@gmail.com";
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();
                Date date = new Date();

                String commentId = generateCommentId();
                CompanyReview comment = new CompanyReview(commentId, company, owner, commentText, rating, user.getEmail(), date);
                db.collection("comments")
                        .add(comment)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "Comment added with ID: " + documentReference.getId());

                                String notificationId = db.collection("notifications").document().getId();
                                Date currentTimestamp = new Date();
                                Notification notification = new Notification(
                                        notificationId,
                                        "New company review",
                                        "Event Organizer " + user.getEmail() + " added new comment: " + commentText,
                                        false,
                                        currentTimestamp,
                                        owner
                                );

                                // Save the notification to Firestore
                                db.collection("notifications").document(notificationId)
                                        .set(notification)
                                        .addOnSuccessListener(aVoid1 -> Toast.makeText(getContext(), "Notification sent", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error sending notification", Toast.LENGTH_SHORT).show());


                                Navigation.findNavController(requireActivity(), R.id.add_new_review_fragment)
                                        .navigate(R.id.nav_company_comments);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding comment", e);

                            }
                        });
            }
        });

        Log.d(TAG, "CompanyCommentsFragment successfully created");

        return view;
    }

    private String generateCommentId() {
        return Long.toString(System.currentTimeMillis());
    }

}