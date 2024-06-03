package com.example.projekatmobilneaplikacije.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateBundleFourthBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateBundleThirdBinding;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateBundleFourthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateBundleFourthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private FragmentCreateBundleFourthBinding binding;
    String title, description, category, available, visible;
    int discount;

    Uri selectedImageUri;

    Service selectedService;
    Product selectedProduct;

    Button btnSubmit;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<Integer> prices = new ArrayList<>();

    private OnBundleeCreatedListener mListener;
    public CreateBundleFourthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment CreateBundleFourthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateBundleFourthFragment newInstance(Service selectedService, Product selectedProduct, String title, String description, String category, String available, String visible, int discount, Uri selectedImageUri) {
        CreateBundleFourthFragment fragment = new CreateBundleFourthFragment();
        Bundle args = new Bundle();
        args.putParcelable("selectedService", selectedService);
        args.putParcelable("selectedProduct", selectedProduct);
        args.putString("title", title);
        args.putString("description", description);
        args.putString("category", category);
        args.putInt("discount", discount);
        args.putString("available", available);
        args.putString("visible", visible);
        args.putParcelable("selectedImageUri", selectedImageUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateBundleFourthBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        Bundle args = getArguments();
        if (args != null) {
            selectedService = args.getParcelable("selectedService");
            selectedProduct = args.getParcelable("selectedProduct");
            title = args.getString("title", "");
            description = args.getString("description", "");
            selectedImageUri = args.getParcelable("selectedImageUri");
            category = args.getString("category", "");
            discount = args.getInt("discount", 0);
            available = args.getString("available", "");
            visible = args.getString("visible", "");
        }


        TextView eventTypeTextView = root.findViewById(R.id.eventType);
        Spinner spinner = root.findViewById(R.id.btnSubcategory);

        if (selectedService !=null && selectedProduct !=null){
            //Calculate bundle price
            prices.add(selectedService.getPrice());
            prices.add(selectedProduct.getPrice());

            //Subcategories
            String productSubcategory = selectedProduct.getSubcategory();
            String serviceSubcategory = selectedService.getSubcategory();

            List<String> subcategories = new ArrayList<>();
            subcategories.add(productSubcategory);
            subcategories.add(serviceSubcategory);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, subcategories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);

            //Event types
            String productEventType = selectedProduct != null ? selectedProduct.getEventType() : "";
            String serviceEventType = selectedService != null ? selectedService.getEventType() : "";

            HashSet<String> eventTypeSet = new HashSet<>();
            eventTypeSet.add(productEventType);
            eventTypeSet.add(serviceEventType);

            List<String> allEventTypes = new ArrayList<>(eventTypeSet);

// Dodavanje podrazumevane vrednosti za eventType ako nema vrednosti u argumentima
            String eventType = getArguments() != null ? getArguments().getString("eventType", "") : "";
            if (!eventType.isEmpty()) {
                allEventTypes.add(eventType);
            }

// Postavljanje teksta eventTypeTextView
            eventTypeTextView.setText(TextUtils.join(", ", allEventTypes));

        }

        int total = 0;
        for (int price : prices) {
            total += price;
        }

        TextView priceTextView = root.findViewById(R.id.price);
        priceTextView.setText(String.valueOf(total));

        TextView reservationDeadlineTextView = root.findViewById(R.id.reservationDeadline);
        TextView cancellationDeadlineTextView = root.findViewById(R.id.cancellationDeadline);
        TextView methodTextView = root.findViewById(R.id.methodOfCOnfirmation);
       if (selectedService != null){
           int serviceReservationDeadline = selectedService.getReservationDeadline();
           reservationDeadlineTextView.setText(String.valueOf(serviceReservationDeadline));

           int serviceCancellationDeadline = selectedService.getCancellationDeadline();
           cancellationDeadlineTextView.setText(String.valueOf(serviceCancellationDeadline));

           int method = selectedService.getReservationDeadline();
           methodTextView.setText(String.valueOf(method));

       }






        //Submit
        //SUBMIT DUGME
        btnSubmit = binding.btnSubmit;

        db = FirebaseFirestore.getInstance();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = title != null ? title : "";
                String Description = description != null ? description : "";
                int Discount = discount;
                String Category = category != null ? category : "";
                String Available = available != null ? available : "";
                String Visible = visible != null ? visible : "";
                String Subcategory = spinner.getSelectedItem() != null ? spinner.getSelectedItem().toString() : "";
                String priceText = priceTextView.getText().toString();
                int Price = Integer.parseInt(priceText);

                String reservationDeadlineText = reservationDeadlineTextView.getText().toString();
                int ReservationDeadline;
                try {
                    ReservationDeadline = Integer.parseInt(reservationDeadlineText);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Please enter a valid number for reservation deadline", Toast.LENGTH_SHORT).show();
                    return;
                }

                String cancellationDeadlineText = cancellationDeadlineTextView.getText().toString();
                int CancellationDeadline;
                try {
                    CancellationDeadline = Integer.parseInt(cancellationDeadlineText);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Please enter a valid number for cancellation deadline", Toast.LENGTH_SHORT).show();
                    return;
                }


                String ConfirmationMode = methodTextView != null ? methodTextView.getText().toString() : "";
                String EventType = eventTypeTextView != null ? eventTypeTextView.getText().toString() : "";
                String UniqueServiceId = UUID.randomUUID().toString();

                Map<String,Object> bundle = new HashMap<>();
                bundle.put("id", UniqueServiceId);
                bundle.put("title", Title);
                bundle.put("description", Description);
                bundle.put("category", Category);
                bundle.put("subcategory", Subcategory);
                bundle.put("price", Price);
                bundle.put("availability", Available);
                bundle.put("visibility", Visible);
                bundle.put("discount", Discount);
                bundle.put("reservationDeadline", ReservationDeadline);
                bundle.put("cancellationDeadline", CancellationDeadline);
                bundle.put("confirmationMode", ConfirmationMode);
                bundle.put("eventType", EventType);
                bundle.put("isDeleted", false);

                List<String> productIds = new ArrayList<>();
                List<String> serviceIds = new ArrayList<>();
                if (selectedProduct != null) {
                    productIds.add(selectedProduct.getId());
                }
                if (selectedService != null) {
                    serviceIds.add(selectedService.getId());
                }
                bundle.put("productIds", productIds);
                bundle.put("serviceIds", serviceIds);



                //service.put("image", base64Image);

                db.collection("bundles")
                        .add(bundle)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("Tag", "Uspesno");
                                Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show();
                                if (mListener != null) {
                                    mListener.onBundleeCreated(); // Call the interface method
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



        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateBundleFourthFragment.OnBundleeCreatedListener) {
            mListener = (CreateBundleFourthFragment.OnBundleeCreatedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBundleeCreatedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnBundleeCreatedListener {
        void onBundleeCreated();
    }
}