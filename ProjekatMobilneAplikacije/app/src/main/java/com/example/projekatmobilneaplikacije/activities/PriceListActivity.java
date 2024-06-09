package com.example.projekatmobilneaplikacije.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.adapters.BundleListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ProductListAdapter;
import com.example.projekatmobilneaplikacije.adapters.ServiceListAdapter;
import com.example.projekatmobilneaplikacije.model.CustomBundle;
import com.example.projekatmobilneaplikacije.model.Product;
import com.example.projekatmobilneaplikacije.model.Service;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.annotation.Nullable;


import java.io.File;
import java.util.ArrayList;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
//import com.itextpdf.layout.property.TextAlignment;


public class PriceListActivity extends AppCompatActivity {
    public static final String ARG_PARAM = "product_list";
    private static final int REQUEST_CODE_EDIT_ITEM = 1;
    private ProductListAdapter adapter;
    private ServiceListAdapter serviceAdapter;
    private BundleListAdapter bundleAdapter;

    public static ArrayList<Product> products = new ArrayList<Product>();
    public static ArrayList<Service> services = new ArrayList<Service>();
    public static ArrayList<CustomBundle> bundles = new ArrayList<CustomBundle>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference productsRef = db.collection("products");
    CollectionReference servicesRef = db.collection("services");
    CollectionReference bundlesRef = db.collection("bundles");

    ListView listView, listViewService, listViewBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_price_list);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ARG_PARAM)) {
            products = intent.getParcelableArrayListExtra(ARG_PARAM);
            adapter = new ProductListAdapter(this, products, null);
        }

        adapter = new ProductListAdapter(this, products, null);
        serviceAdapter = new ServiceListAdapter(this, services, null);
        bundleAdapter = new BundleListAdapter(this, bundles);

        productsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                products.clear();

                // Check if there are any documents in the collection
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Iterate through each document in the collection
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Convert each document to a Product object
                        Product product = documentSnapshot.toObject(Product.class);

                        products.add(product);
                    }
                    // After retrieving all products, update your adapter
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle case when there are no products in the collection
                    Log.d("PriceListActivity", "No products found");
                }
            }
        });

        servicesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                services.clear();

                // Check if there are any documents in the collection
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Iterate through each document in the collection
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Convert each document to a Product object
                        Service service = documentSnapshot.toObject(Service.class);

                        services.add(service);
                    }
                    // After retrieving all products, update your adapter
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    // Handle case when there are no products in the collection
                    Log.d("PriceListActivity", "No services found");
                }
            }
        });

        bundlesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                bundles.clear();

                // Check if there are any documents in the collection
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Iterate through each document in the collection
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Convert each document to a Product object
                        CustomBundle bundle = documentSnapshot.toObject(CustomBundle.class);

                        bundles.add(bundle);
                    }
                    // After retrieving all products, update your adapter
                    bundleAdapter.notifyDataSetChanged();
                } else {
                    // Handle case when there are no products in the collection
                    Log.d("PriceListActivity", "No bundles found");
                }
            }
        });

        listView = findViewById(R.id.productsList);
        listView.setAdapter(adapter);

        listViewService = findViewById(R.id.servicesList);
        listViewService.setAdapter(serviceAdapter);

        listViewBundle = findViewById(R.id.bundlesList);
        listViewBundle.setAdapter(bundleAdapter);

        Button btnPdf = findViewById(R.id.btnPdf);
        btnPdf.setOnClickListener(v -> generatePdf());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.price_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void generatePdf() {
        try {
            // Create a PDF file in the external storage directory
            String pdfPath = getExternalFilesDir(null) + "/price_list.pdf";
            PdfWriter writer = new PdfWriter(pdfPath);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A4);

            document.add(new Paragraph("Price List")
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER));

            // Adding Products
            document.add(new Paragraph("Products")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.LEFT));

            Table productTable = new Table(new float[]{4, 4, 4, 4});
            productTable.addHeaderCell("Name");
            productTable.addHeaderCell("Price");
            productTable.addHeaderCell("Discount");
            productTable.addHeaderCell("Discount Price");

            for (Product product : products) {
                productTable.addCell(product.getTitle());
                productTable.addCell(String.valueOf(product.getPrice()));
                productTable.addCell(String.valueOf(product.getDiscount()));
                productTable.addCell(String.valueOf(product.getPriceWithDiscount()));
            }
            document.add(productTable);

            // Adding Services
            document.add(new Paragraph("Services")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.LEFT));

            Table serviceTable = new Table(new float[]{4, 4, 4, 4});
            serviceTable.addHeaderCell("Name");
            serviceTable.addHeaderCell("Price");
            serviceTable.addHeaderCell("Discount");
            serviceTable.addHeaderCell("Discount Price");

            for (Service service : services) {
                serviceTable.addCell(service.getTitle());
                serviceTable.addCell(String.valueOf(service.getPrice()));
                serviceTable.addCell(String.valueOf(service.getDiscount()));
                serviceTable.addCell(String.valueOf(service.getPriceWithDiscount()));
            }
            document.add(serviceTable);

            // Adding Bundles
            document.add(new Paragraph("Bundles")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.LEFT));

            Table bundleTable = new Table(new float[]{4, 4, 4, 4});
            bundleTable.addHeaderCell("Name");
            bundleTable.addHeaderCell("Price");
            bundleTable.addHeaderCell("Discount");
            bundleTable.addHeaderCell("Discount Price");

            for (CustomBundle bundle : bundles) {
                bundleTable.addCell(bundle.getTitle());
                bundleTable.addCell(String.valueOf(bundle.getPrice()));
                bundleTable.addCell(String.valueOf(bundle.getDiscount()));
                bundleTable.addCell(String.valueOf(bundle.getPriceWithDiscount()));
            }
            document.add(bundleTable);

            document.close();
            writer.close();

            Toast.makeText(this, "PDF generated successfully", Toast.LENGTH_SHORT).show();
            openPdf(pdfPath);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPdf(String pdfPath) {
        File pdfFile = new File(pdfPath);
        Uri pdfUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", pdfFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_ITEM && resultCode == RESULT_OK) {
            refreshLists();  // Custom method to refresh your lists
        }
    }

    private void refreshLists() {
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
                    Log.d("PriceListActivity", "No products found");
                }
            }
        });

        servicesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                services.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Service service = documentSnapshot.toObject(Service.class);
                        services.add(service);
                    }
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    Log.d("PriceListActivity", "No services found");
                }
            }
        });

        bundlesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                bundles.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        CustomBundle bundle = documentSnapshot.toObject(CustomBundle.class);
                        bundles.add(bundle);
                    }
                    bundleAdapter.notifyDataSetChanged();
                } else {
                    Log.d("PriceListActivity", "No bundles found");
                }
            }
        });
    }

}