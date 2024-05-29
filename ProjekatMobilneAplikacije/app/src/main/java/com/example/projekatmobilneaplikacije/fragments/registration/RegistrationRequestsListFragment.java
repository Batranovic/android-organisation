package com.example.projekatmobilneaplikacije.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.RegistrationRequestsListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ServiceAndProductListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentRegistrationRequestsBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentRegistrationRequestsListBinding;
import com.example.projekatmobilneaplikacije.fragments.serviceAndProduct.ServiceAndProductListFragment;
import com.example.projekatmobilneaplikacije.model.RegistrationRequest;
import com.example.projekatmobilneaplikacije.model.enumerations.Owner;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;


public class RegistrationRequestsListFragment extends ListFragment {

    private FragmentRegistrationRequestsListBinding binding;

    private RegistrationRequestsListAdapter adapter;
    private ArrayList<RegistrationRequest> registrationRequest;
    private static final String ARG_PARAM = "param";


    public RegistrationRequestsListFragment() {
        // Required empty public constructor
    }


    public static RegistrationRequestsListFragment newInstance(ArrayList<RegistrationRequest> data) {
        RegistrationRequestsListFragment fragment = new RegistrationRequestsListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, data);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrationRequestsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            registrationRequest = (ArrayList<RegistrationRequest>) getArguments().getSerializable(ARG_PARAM); // Promenjeno preuzimanje podataka
            adapter = new RegistrationRequestsListAdapter(getActivity(), registrationRequest); // Promenjen konstruktor adaptera
            setListAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}