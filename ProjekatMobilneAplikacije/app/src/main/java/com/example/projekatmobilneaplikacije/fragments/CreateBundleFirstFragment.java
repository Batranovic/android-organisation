package com.example.projekatmobilneaplikacije.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateBundleFirstBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateBundleFirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateBundleFirstFragment extends Fragment {

    private Uri selectedImageUri;
    EditText title, description, discount;
    Spinner spinner;
    String available, visible;

    private final int GALLERY_REQ_CODE = 1000;
    ImageView imgGallery;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentCreateBundleFirstBinding binding;

    public CreateBundleFirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateBundleFirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateBundleFirstFragment newInstance(String param1, String param2) {
        CreateBundleFirstFragment fragment = new CreateBundleFirstFragment();
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
        binding = FragmentCreateBundleFirstBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        imgGallery = root.findViewById(R.id.imgGallery);
        ImageButton btnGallery = root.findViewById(R.id.btnGallery);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        Spinner spinner = binding.btnCategory;
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.category_list));
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);



        title = binding.editTitle;
        description = binding.editDescription;
        discount = binding.editDiscount;


        binding.nextFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AVAILABLE AND VISIBLE
                RadioGroup radioGroupAvailability = binding.availability;
                int selectedRadioButtonId = radioGroupAvailability.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = root.findViewById(selectedRadioButtonId);
                    String selectedText = selectedRadioButton.getText().toString();
                    available = selectedText;
                } else {
                    // Nijedan RadioButton nije odabran
                    available = "";
                }

                RadioGroup radioGroupVisibility = binding.visibility;
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


                Fragment secondFragment = CreateBundleSecondFragment.newInstance(title.getText().toString(), description.getText().toString(), spinner.getSelectedItem().toString(), discount.toString(), available, visible, selectedImageUri);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.create_bundle_container, secondFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });




        return root;
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