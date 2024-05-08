package com.example.projekatmobilneaplikacije.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.CreateServiceActivity;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateServiceFirstBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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


    private Uri selectedImageUri;
    EditText title, description, specificity, discount;
    Spinner spinner, spinnerSubcategory;
    String available, visible;
    Button btnSubmit;

    private final int GALLERY_REQ_CODE = 1000;
    ImageView imgGallery;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentCreateServiceFirstBinding = FragmentCreateServiceFirstBinding.inflate(inflater, container, false);

        View root = fragmentCreateServiceFirstBinding.getRoot();


        ImageButton addNewSubcategoryButton = root.findViewById(R.id.addNewSubcategoryButton);
        addNewSubcategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        spinner = fragmentCreateServiceFirstBinding.btnCategory;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.category_list));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinnerSubcategory = fragmentCreateServiceFirstBinding.btnSubcategory;
        ArrayAdapter<String> arrayAdapterSubcategory = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.subcategory_list));
        arrayAdapterSubcategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubcategory.setAdapter(arrayAdapterSubcategory);


        SeekBar priceSeekBar = fragmentCreateServiceFirstBinding.priceSeekBar;
        TextView priceText = fragmentCreateServiceFirstBinding.textViewPrice;

        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priceText.setVisibility(View.VISIBLE);
                priceText.setText(progress+"/100");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //IMAGE
        imgGallery = fragmentCreateServiceFirstBinding.imgGallery;
        ImageButton btnGallery = fragmentCreateServiceFirstBinding.btnGallery;

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });



        title = fragmentCreateServiceFirstBinding.title;
        description = fragmentCreateServiceFirstBinding.editDescription;
        specificity = fragmentCreateServiceFirstBinding.specificity;
        discount = fragmentCreateServiceFirstBinding.discount;
        fragmentCreateServiceFirstBinding.nextFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroupAvailability = fragmentCreateServiceFirstBinding.availability; // Postavite pravilan ID za RadioGroup
                int selectedRadioButtonId = radioGroupAvailability.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = root.findViewById(selectedRadioButtonId);
                    String selectedText = selectedRadioButton.getText().toString();
                    available = selectedText;
                } else {
                    // Nijedan RadioButton nije odabran
                    available = "";
                }

                RadioGroup radioGroupVisibility = fragmentCreateServiceFirstBinding.visibility; // Postavite pravilan ID za RadioGroup
                int selectedRadioVisibilityButtonId = radioGroupVisibility.getCheckedRadioButtonId();

                if (selectedRadioVisibilityButtonId != -1) {
                    RadioButton selectedRadioVisibilityButton = root.findViewById(selectedRadioVisibilityButtonId);
                    String selectedText = selectedRadioVisibilityButton.getText().toString();
                    visible = selectedText;
                } else {
                    visible = "";
                }

                String Available = available;
                String Visible = visible;

                Fragment secondFragment = CreateServiceSecondFragment.newInstance(title.getText().toString(), description.getText().toString(), specificity.getText().toString(), discount.getText().toString(), spinner.getSelectedItem().toString(), spinnerSubcategory.getSelectedItem().toString(), priceSeekBar.getProgress(), available, visible, selectedImageUri);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.create_service_container, secondFragment)
                        .addToBackStack(null)
                        .commit();

                // Enkodiranje slike u Base64 format
                String base64Image = ""; // Ovdje ćemo smestiti enkodiranu sliku
                if (selectedImageUri != null) {
                    try {
                        // Pretvaranje URI-ja odabrane slike u bitmapu
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);

                        // Konvertovanje bitmape u byte[] koristeći ByteArrayOutputStream
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageData = baos.toByteArray();

                        // Konvertovanje byte[] u Base64 string
                        base64Image = Base64.encodeToString(imageData, Base64.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                // Čuvanje URI-ja odabrane slike u globalnoj promenljivoj fragmenta
                selectedImageUri = data.getData();

                // Dobijanje URI-ja odabrane slike iz galerije
                Uri selectedImageUri = this.selectedImageUri;

                try {
                    // Pretvaranje URI-ja u bitmapu
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);

                    // Konvertovanje bitmape u byte[] koristeći ByteArrayOutputStream
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();

                    // Konvertovanje byte[] u Base64 string
                    String base64Image = Base64.encodeToString(imageData, Base64.DEFAULT);

                    // Sada možete sačuvati base64Image u bazi podataka zajedno sa ostalim podacima proizvoda

                    // Prikažite sliku u ImageView-u (opcionalno)
                    imgGallery.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}