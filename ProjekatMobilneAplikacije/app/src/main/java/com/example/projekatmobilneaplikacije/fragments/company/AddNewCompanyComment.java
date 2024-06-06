package com.example.projekatmobilneaplikacije.fragments.company;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projekatmobilneaplikacije.R;

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

        newCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.add_new_review_fragment)
                        .navigate(R.id.nav_company_comments);

            }
        });

        Log.d(TAG, "CompanyCommentsFragment successfully created");

        return view;
    }
}