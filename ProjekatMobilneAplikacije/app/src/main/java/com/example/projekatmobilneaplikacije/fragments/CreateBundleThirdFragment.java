package com.example.projekatmobilneaplikacije.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ServiceListAdapter;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateBundleSecondBinding;
import com.example.projekatmobilneaplikacije.databinding.FragmentCreateBundleThirdBinding;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateBundleThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateBundleThirdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentCreateBundleThirdBinding binding;

    String title, description, category, available, visible;
    int discount;

    Uri selectedImageUri;

    Service selectedService;
    Product selectedProduct;

    private ListView listView;
    private ProductListAdapter adapter;

    private ArrayList<Product> products = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = db.collection("products");

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
    public static CreateBundleThirdFragment newInstance(Service selectedService, String title, String description, String category, String available, String visible, int discount, Uri selectedImageUri) {
        CreateBundleThirdFragment fragment = new CreateBundleThirdFragment();
        Bundle args = new Bundle();
        args.putParcelable("selectedService", selectedService);
        Log.d("Create bundle third fragment","selectedService "+ selectedService.getTitle());
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
        if (getArguments() != null) {
            products = getArguments().getParcelableArrayList(ARG_PARAM);
        }
        // Initialize the services ArrayList if it's null
        if (products == null) {
            products = new ArrayList<>();
        }
        // Initialize the adapter with the services ArrayList
        adapter = new ProductListAdapter(getActivity(), products, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateBundleThirdBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        listView = root.findViewById(android.R.id.list);
        adapter = new ProductListAdapter(getActivity(), products, this);
        listView.setAdapter(adapter);

        // Učitavanje servisa iz baze podataka
        loadProducts();

        Bundle args = getArguments();
        if (args != null) {
            selectedService = args.getParcelable("selectedService");
            title = args.getString("title", "");
            description = args.getString("description", "");
            selectedImageUri = args.getParcelable("selectedImageUri");
            category = args.getString("category", "");
            discount = args.getInt("discount", 0);
            available = args.getString("available", "");
            visible = args.getString("visible", "");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProduct = products.get(position);
                // Postavljanje odabrane pozadine za kliknuti element
                Log.d("CreateBundleThirdFragment", "selected item: " + selectedProduct.getTitle());
                view.setBackgroundColor(ContextCompat.getColor(getContext(), androidx.cardview.R.color.cardview_dark_background));
            }
        });



        binding.nextFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fourthFragment = CreateBundleFourthFragment.newInstance(selectedService, selectedProduct, title, description, category, available, visible, discount, selectedImageUri);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.create_bundle_container, fourthFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return root;
    }


    private void loadProducts() {
        productsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                products.clear();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Product product = documentSnapshot.toObject(Product.class);
                        products.add(product);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("CreateBundleThirdFragment", "No products found");
                }
            }
        });
    }
}