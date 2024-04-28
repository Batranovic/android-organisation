package com.example.projekatmobilneaplikacije.fragments.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.HomeActivity;
import com.example.projekatmobilneaplikacije.activities.MainActivity;
import com.example.projekatmobilneaplikacije.databinding.FragmentLogoutBinding;
import com.example.projekatmobilneaplikacije.fragments.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogoutFragment extends Fragment {

   private FragmentLogoutBinding binding;
    Button logout;
    TextView loggedInUser;
    FirebaseAuth auth;
    FirebaseUser user;
    public LogoutFragment() {
        // Required empty public constructor
    }

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        logout = root.findViewById(R.id.logout_button);

        auth = FirebaseAuth.getInstance();
        loggedInUser = root.findViewById(R.id.loggedInUser);
        user = auth.getCurrentUser();
        if(user == null){
            loggedInUser.setText("");
        }else {
            loggedInUser.setText(user.getEmail());

        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(view.getContext(), HomeActivity.class);
                startActivity(intent);

            }
        });
        return root;
    }
}