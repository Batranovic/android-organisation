package com.example.projekatmobilneaplikacije.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateBundleSecondBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateBundleThirdBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateBundleThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateBundleThirdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentCreateBundleThirdBinding binding;

    public CreateBundleThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateBundleThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateBundleThirdFragment newInstance(String param1, String param2) {
        CreateBundleThirdFragment fragment = new CreateBundleThirdFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateBundleThirdBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        binding.nextFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fourthFragment = new CreateBundleFourthFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.create_bundle_container, fourthFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return root;
    }
}