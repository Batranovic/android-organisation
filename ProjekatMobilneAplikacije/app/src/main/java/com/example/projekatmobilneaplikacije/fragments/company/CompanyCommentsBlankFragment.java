package com.example.projekatmobilneaplikacije.fragments.company;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentCompanyCommentsBlankBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentCompanyProfileBinding;
import com.example.projekatmobilneaplikacije.fragments.CreateBundleSecondFragment;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.fragments.employees.EmployeeWHOverview;
import com.example.projekatmobilneaplikacije.fragments.registration.RegisterFragment;
import com.example.projekatmobilneaplikacije.model.Notification;
import com.example.projekatmobilneaplikacije.model.Report;
import com.example.projekatmobilneaplikacije.model.enumerations.Status;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyCommentsBlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyCommentsBlankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton reportCompany;
    private FragmentCompanyCommentsBlankBinding binding;

    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CompanyCommentsBlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanyCommentsBlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyCommentsBlankFragment newInstance(String param1, String param2) {
        CompanyCommentsBlankFragment fragment = new CompanyCommentsBlankFragment();
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
        binding = FragmentCompanyCommentsBlankBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View companyProfileView = inflater.inflate(R.layout.fragment_company_profile, container, false);

        ViewGroup companyLayout = root.findViewById(R.id.company_layout);

        Fragment previousFragment = getChildFragmentManager().findFragmentById(R.id.company_layout);
        if (previousFragment != null) {
            getChildFragmentManager().beginTransaction().remove(previousFragment).commit();
        }

        companyLayout.addView(companyProfileView);

        FloatingActionButton nextButton = root.findViewById(R.id.comments_floating_buttons);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //.to(CompanyCommentsFragment.newInstance("WH Frag", "Employee Working Hours overview"), getActivity(), false, R.id.company_layout);
                Navigation.findNavController(requireActivity(), R.id.company_layout)
                        .navigate(R.id.nav_company_comments);


            }
        });

        reportCompany = root.findViewById(R.id.reportCompany);
        reportCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Report button clicked");
                showReportDialog();
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void showReportDialog() {
        Log.d(TAG, "Showing report dialog");
        // Kreiranje AlertDialog-a
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Report reason");

        // Dodavanje EditText polja za unos razloga
        final EditText input = new EditText(getContext());
        input.setHint("Enter reason");
        builder.setView(input);

        // Dodavanje dugmadi za potvrdu i otkazivanje
        builder.setPositiveButton("Report", (dialog, which) -> {
            String reason = input.getText().toString();
            if (!reason.isEmpty()) {
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();

                String reportId = db.collection("reports").document().getId();
                Date currentTimestamp = new Date();
                String companyEmail = "komp@gmail.com";
                Report report = new Report(reportId, companyEmail, user.getEmail(), reason, Status.Pending, currentTimestamp);

                db.collection("reports").document(reportId)
                        .set(report)
                        .addOnSuccessListener(aVoid1 -> Toast.makeText(getContext(), "Report saved", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error saving report", Toast.LENGTH_SHORT).show());

                String notificationId = db.collection("notifications").document().getId();
                String adminEmail = "nina@gmail.com";
                Notification notification = new Notification(
                        notificationId,
                        "Report",
                        "Reason " + reason,
                        false,
                        currentTimestamp,
                        adminEmail
                );

                db.collection("notifications").document(notificationId)
                        .set(notification)
                        .addOnSuccessListener(aVoid1 -> Toast.makeText(getContext(), "Notification sent", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error sending notification", Toast.LENGTH_SHORT).show());
            } else {
                input.setError("Enter reason");
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Prikazivanje dijaloga
        builder.show();
    }


}