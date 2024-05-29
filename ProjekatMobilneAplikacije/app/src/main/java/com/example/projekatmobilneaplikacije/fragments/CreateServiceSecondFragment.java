package com.example.projekatmobilneaplikacije.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.CreateProductActivity;
import com.example.projekatmobilneaplikacije.activities.CreateServiceActivity;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateServiceFirstBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateServiceSecondBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateServiceSecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateServiceSecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText duration, engagement,reservationDeadline, cancellationDeadline;
    String confirmationMode;
    boolean isPrivateEventChecked = false;
    boolean isCorporateEventChecked = false;
    boolean isEducationEventChecked = false;
    boolean isSportEventChecked = false;
    boolean isCulturalEventChecked = false;
    boolean isHumanitarianEventChecked = false;

    String title, description, specificity, category, subcategory, available, visible;

    int price, discount;
    Uri selectedImageUri;

    Button btnSubmit;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FragmentCreateServiceSecondBinding binding;

    private OnServiceCreatedListener mListener;

    public CreateServiceSecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateServiceSecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateServiceSecondFragment newInstance(String title, String description, String specificity, int discount, String category, String subcategory, int price, String available, String visible, Uri selectedImageUri) {
        CreateServiceSecondFragment fragment = new CreateServiceSecondFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putString("specificity", specificity);
        args.putInt("discount", discount);
        args.putString("category", category);
        args.putString("subcategory", subcategory);
        args.putInt("price", price);
        args.putString("available", available);
        args.putString("visible", visible);
        args.putParcelable("selectedImageUri", selectedImageUri);
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

        binding = FragmentCreateServiceSecondBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        // Izvadite argumente
        Bundle args = getArguments();
        if (args != null) {
            title = args.getString("title", "");
            description = args.getString("description", "");
            specificity = args.getString("specificity", "");
            discount = args.getInt("discount", 0);
            selectedImageUri = args.getParcelable("selectedImageUri");
            category = args.getString("category", "");
            subcategory = args.getString("subcategory", "");
            price = args.getInt("price", 0);
            available = args.getString("available", "");
            visible = args.getString("visible", "");
        }

        duration = binding.editTextDuration;
        engagement = binding.editTextEngagement;
        reservationDeadline = binding.editTextReservationDeadline;
        cancellationDeadline = binding.editTextCancellationDeadline;

        Button btnAddEventTypes = binding.btnAddEventTypes;
        btnAddEventTypes.setOnClickListener(v -> {
            Log.i("ShopApp", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_event_types, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();

            CheckBox privateEventCheckbox = dialogView.findViewById(R.id.privateEventCheckbox);
            privateEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isPrivateEventChecked = isChecked;
            });

            CheckBox corporateEventCheckbox = dialogView.findViewById(R.id.corporateEventCheckbox);
            corporateEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isCorporateEventChecked = isChecked;
            });

            CheckBox educationEventCheckbox = dialogView.findViewById(R.id.educationEventCheckbox);
            educationEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isEducationEventChecked = isChecked;
            });

            CheckBox culturalEventCheckbox = dialogView.findViewById(R.id.culturalEventCheckbox);
            culturalEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isCulturalEventChecked = isChecked;
            });

            CheckBox sportEventCheckbox = dialogView.findViewById(R.id.sportEventCheckbox);
            sportEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isSportEventChecked = isChecked;
            });

            CheckBox humanitarianEventCheckbox = dialogView.findViewById(R.id.humanitarianEventCheckbox);
            humanitarianEventCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isHumanitarianEventChecked = isChecked;
            });


        });


        //SUBMIT DUGME
        btnSubmit = binding.btnSubmit;

        db = FirebaseFirestore.getInstance();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Duration = duration.getText().toString();
                String Engagement = engagement.getText().toString();
                String ReservationDeadline = reservationDeadline.getText().toString();
                String CancellationDeadline = cancellationDeadline.getText().toString();
                String Title = title;
                String Description = description;
                String Specificity = specificity;
                int Discount = discount;
                String Category = category;
                String Subcategory = subcategory;
                int Price = price;
                String Available = available;
                String Visible = visible;

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


                RadioGroup radioGroupAvailability = binding.radioGroupConfirmationMode;
                int selectedRadioButtonId = radioGroupAvailability.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = root.findViewById(selectedRadioButtonId);
                    String selectedText = selectedRadioButton.getText().toString();
                    confirmationMode = selectedText;
                } else {
                    // Nijedan RadioButton nije odabran
                    confirmationMode = "";
                }


                String ConfirmationMode = confirmationMode;


                String EventType = "";
                if (isPrivateEventChecked) {
                    EventType += getString(R.string.privateEvent) + ", ";
                }
                if (isCorporateEventChecked) {
                    EventType += getString(R.string.corporateEvent) + ", ";
                }
                if (isEducationEventChecked) {
                    EventType += getString(R.string.educationEvent) + ", ";
                }
                if (isCulturalEventChecked) {
                    EventType += getString(R.string.culturalEvent) + ", ";
                }
                if (isSportEventChecked) {
                    EventType += getString(R.string.sportEvent) + ", ";
                }
                if (isHumanitarianEventChecked) {
                    EventType += getString(R.string.humanitarianEvent) + ", ";
                }


                // Uklonite poslednji zarez iz eventType ako je potrebno
                if (!EventType.isEmpty() && EventType.endsWith(", ")) {
                    EventType = EventType.substring(0, EventType.length() - 2);
                }

                String UniqueServiceId = UUID.randomUUID().toString();

                Map<String,Object> service = new HashMap<>();
                service.put("id", UniqueServiceId);
                service.put("title", Title);
                service.put("description", Description);
                service.put("category", Category);
                service.put("subcategory", Subcategory);
                service.put("price", Price);
                service.put("availability", Available);
                service.put("visibility", Visible);
                service.put("duration", Duration);
                service.put("specificity", Specificity);
                service.put("discount", Discount);
                service.put("engagement", Engagement);
                service.put("reservationDeadline", ReservationDeadline);
                service.put("cancellationDeadline", CancellationDeadline);
                service.put("confirmationMode", ConfirmationMode);
                service.put("eventType", EventType);
                service.put("isDeleted", false);
                service.put("image", base64Image);

                //service.put("image", base64Image);

                db.collection("services")
                        .add(service)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("Tag", "Uspesno");
                                Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show();
                                if (mListener != null) {
                                    mListener.onServiceCreated(); // Call the interface method
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Tag", "Neuspesno");
                                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnServiceCreatedListener) {
            mListener = (OnServiceCreatedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnServiceCreatedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnServiceCreatedListener {
        void onServiceCreated();
    }


}