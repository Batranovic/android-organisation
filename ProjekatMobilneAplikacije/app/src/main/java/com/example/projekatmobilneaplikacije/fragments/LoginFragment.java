package com.example.projekatmobilneaplikacije.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
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
import com.example.projekatmobilneaplikacije.activities.RegistrationActivity;
import com.example.projekatmobilneaplikacije.databinding.FragmentLoginBinding;
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
}