package com.example.projekatmobilneaplikacije.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekatmobilneaplikacije.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmNewPasswordEditText;
    private ImageView profilePicture;
    private Button saveChangesButton;
    private Button deactivateAccountButton;

    private FirebaseFirestore db;
    private DocumentReference userDocRef;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firstNameEditText = view.findViewById(R.id.firstName);
        lastNameEditText = view.findViewById(R.id.lastName);
        emailEditText = view.findViewById(R.id.email);
        currentPasswordEditText = view.findViewById(R.id.oldPassword);
        newPasswordEditText = view.findViewById(R.id.newPassword);
        confirmNewPasswordEditText = view.findViewById(R.id.confirmNewPassword);
        profilePicture = view.findViewById(R.id.profilePicture);
        saveChangesButton = view.findViewById(R.id.saveChangesButton);
        deactivateAccountButton = view.findViewById(R.id.deactivateAccountButton);

        // Get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String email = user.getEmail();
            emailEditText.setText(email);
            Log.d(TAG, "User email: " + email);

            // Fetch additional user details from Firestore using email
            db.collection("userDetails")
                    .whereEqualTo("username", email) // Proverite da li je "username" tačno polje koje koristi email
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    userDocRef = document.getReference();
                                    String firstName = document.getString("name");
                                    String lastName = document.getString("surname");
                                    Log.d(TAG, "User first name: " + firstName);
                                    Log.d(TAG, "User last name: " + lastName);

                                    firstNameEditText.setText(firstName);
                                    lastNameEditText.setText(lastName);
                                }
                            } else {
                                Log.d(TAG, "Document does not exist for email: " + email);
                                Toast.makeText(getActivity(), "Document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG, "Error getting user details", task.getException());
                            Toast.makeText(getActivity(), "Error getting user details", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // No user is signed in
            Toast.makeText(getActivity(), "No user signed in", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No user signed in");
        }

        // Set onClickListeners for buttons
        saveChangesButton.setOnClickListener(v -> saveChanges());
        deactivateAccountButton.setOnClickListener(v -> deactivateAccount());

        return view;
    }

    private void saveChanges() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String currentPassword = currentPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (userDocRef != null) {
                userDocRef.update("name", firstName, "surname", lastName)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Profile updated successfully");
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error updating profile", e);
                            Toast.makeText(getActivity(), "Error updating profile", Toast.LENGTH_SHORT).show();
                        });

                if (!TextUtils.isEmpty(currentPassword) && !TextUtils.isEmpty(newPassword) && newPassword.equals(confirmNewPassword)) {
                    // Reauthenticate the user with the current password
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                    user.reauthenticate(credential)
                            .addOnSuccessListener(aVoid -> {
                                // Update the user's password
                                user.updatePassword(newPassword)
                                        .addOnSuccessListener(aVoid1 -> {
                                            Toast.makeText(getActivity(), "Password updated", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "Password updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w(TAG, "Error updating password", e);
                                            Toast.makeText(getActivity(), "Error updating password", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error reauthenticating", e);
                                Toast.makeText(getActivity(), "Error reauthenticating. Please check your current password.", Toast.LENGTH_SHORT).show();
                            });
                } else if (!TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Passwords do not match");
                }
            } else {
                Log.d(TAG, "User document reference is null");
                Toast.makeText(getActivity(), "Error updating profile. User document reference is null.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deactivateAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Account deactivated", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Account deactivated successfully");
                            // Redirect to login or main screen
                        } else {
                            Toast.makeText(getActivity(), "Error deactivating account", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Error deactivating account", task.getException());
                        }
                    });
        }
    }
}
