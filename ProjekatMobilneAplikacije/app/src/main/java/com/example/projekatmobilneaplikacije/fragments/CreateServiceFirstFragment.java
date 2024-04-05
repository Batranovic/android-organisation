package com.example.projekatmobilneaplikacije.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.CreateServiceActivity;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateServiceFirstBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateServiceFirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateServiceFirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentCreateServiceFirstBinding fragmentCreateServiceFirstBinding;

    public CreateServiceFirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateServiceFirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateServiceFirstFragment newInstance(String param1, String param2) {
        CreateServiceFirstFragment fragment = new CreateServiceFirstFragment();
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

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_service_first, container, false);
    }*/

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_service_first, container, false);

        ImageButton btnFragment2 = view.findViewById(R.id.nextFragmentButton);
        btnFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(CreateServiceSecondFragment.newInstance("Fragment 2", "Ovo je fragment 2"), CreateServiceFirstFragment.this, false, R.id.main);
            }
        });

        return view;
    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentCreateServiceFirstBinding = FragmentCreateServiceFirstBinding.inflate(inflater, container, false);

        View root = fragmentCreateServiceFirstBinding.getRoot();
        fragmentCreateServiceFirstBinding.nextFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment secondFragment = new CreateServiceSecondFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.create_service_container, secondFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        ImageButton addNewSubcategoryButton = root.findViewById(R.id.addNewSubcategoryButton);
        addNewSubcategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        return root;
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Unesite novu podkategoriju");

        // Postavljanje layouta unutar dijaloga
        View dialogView = getLayoutInflater().inflate(R.layout.subcategory_dialog, null);
        builder.setView(dialogView);

        EditText editText = dialogView.findViewById(R.id.editText);

        builder.setPositiveButton("Potvrdi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ovdje možete obraditi uneseni tekst iz editTexta
                String newSubcategory = editText.getText().toString();
                // Primjer: Prikazivanje unesenog teksta u Logcatu
                Log.d("Novi tekst:", newSubcategory);
            }
        });

        builder.setNegativeButton("Otkaži", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Zatvaranje dijaloga ako korisnik odustane
                dialog.dismiss();
            }
        });

        builder.show();
    }

}