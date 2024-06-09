package com.example.projekatmobilneaplikacije.activities.agenda;







import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.OneGuestActivity;
import com.example.projekatmobilneaplikacije.databinding.AddGuestsListBinding;

import com.example.projekatmobilneaplikacije.fragments.GuestListFragment;
import com.example.projekatmobilneaplikacije.model.Agenda;
import com.example.projekatmobilneaplikacije.model.GuestList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddGuestListActivity extends AppCompatActivity {

    AddGuestsListBinding binding;
    EditText guestName;
    Spinner spinner;
    RadioGroup invitedGroup;
    RadioGroup acceptedGroup;
    CheckBox noReqCheckBox;
    CheckBox veganCheckBox;
    CheckBox vegetarianCheckBox;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int PERMISSION_REQUEST_CODE = 1;

    public static AddGuestListActivity newInstance() {
        return new AddGuestListActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddGuestsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();
        guestName = root.findViewById(R.id.edit_text_guest_name);
        spinner = root.findViewById(R.id.spinner_guest_age);
        invitedGroup = root.findViewById(R.id.radio_group_invited);
        acceptedGroup = root.findViewById(R.id.radio_group_accepted);
        noReqCheckBox = root.findViewById(R.id.checkbox_noreq);
        veganCheckBox = root.findViewById(R.id.checkbox_vegan);
        vegetarianCheckBox = root.findViewById(R.id.checkbox_vegetarian);

        Button createButton = root.findViewById(R.id.button_add_guest);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGuestToFirestore();
            }
        });
        Button allGuestsButton = root.findViewById(R.id.button_all_guests);
        allGuestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllGuestsFragment();
            }
        });
         Button generatePdfButton = root.findViewById(R.id.button_generate_pdf_g);
        generatePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    fetchGuestsAndGeneratePdf();
                } else {
                    requestPermission();
                }
            }
        });
    }
    private void clearInputFields() {
        guestName.setText("");
        spinner.setSelection(0);
        invitedGroup.clearCheck();
        acceptedGroup.clearCheck();
        noReqCheckBox.setChecked(false);
        veganCheckBox.setChecked(false);
        vegetarianCheckBox.setChecked(false);
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchGuestsAndGeneratePdf();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchGuestsAndGeneratePdf() {
        db.collection("guestList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<GuestList> guests = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                GuestList guest = document.toObject(GuestList.class);
                                guests.add(guest);
                            }
                            try {
                                generatePdf(guests);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.w("AddGuestListActivity", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void generatePdf(List<GuestList> guests) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDoc);
        Paragraph title = new Paragraph("Guest List")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);
        // document.add(new LineSeparator(new SolidLine()).setMarginBottom(10));

        for (GuestList guest : guests) {
            document.add(new Paragraph("Name: " + guest.getName()));
            document.add(new Paragraph("Age: " + guest.getAge()));
            document.add(new Paragraph("Invited: " + guest.isInvited()));
            document.add(new Paragraph("Accepted: " + guest.isHasAccepted()));
            document.add(new Paragraph("Special Request: " + guest.getSpecialRequests()));
            document.add(new Paragraph("\n"));
        }

        document.close();

        savePdfToFile(byteArrayOutputStream.toByteArray());
    }

    private void savePdfToFile(byte[] pdfBytes) throws IOException {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/guestList.pdf";
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(pdfBytes);
        fos.close();

        Toast.makeText(this, "PDF generated and saved to Downloads", Toast.LENGTH_SHORT).show();
    }


    private void saveGuestToFirestore() {
        String name = guestName.getText().toString();
        String age = spinner.getSelectedItem().toString();



        int selectedInvitedId = invitedGroup.getCheckedRadioButtonId();
        boolean isInvited = selectedInvitedId == R.id.radio_invited_yes;

        int selectedAcceptedId = acceptedGroup.getCheckedRadioButtonId();
        boolean hasAccepted = selectedAcceptedId == R.id.radio_accepted_yes;

        List<String> specialRequests = new ArrayList<>();
        if (noReqCheckBox.isChecked()) {
            specialRequests.add(getString(R.string.noreq));
        }
        if (veganCheckBox.isChecked()) {
            specialRequests.add(getString(R.string.vegan));
        }
        if (vegetarianCheckBox.isChecked()) {
            specialRequests.add(getString(R.string.vegetarian));
        }

        GuestList guest = new GuestList();

        guest.setName(name);
        guest.setAge(age);
        guest.setInvited(isInvited);
        guest.setHasAccepted(hasAccepted);
        guest.setSpecialRequests(specialRequests);


        db.collection("guestList")
                .add(guest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        guest.setId(id); // Set the ID in the guest object

                        // Update the document with the ID
                        documentReference.update("id", id)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("AddGuestListActivity", "Guest added with ID: " + id);
                                        Toast.makeText(AddGuestListActivity.this, "Guest created successfully", Toast.LENGTH_SHORT).show();
                                        clearInputFields();

                                        // Send the guest ID to the next activity

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("AddGuestListActivity", "Error updating guest ID", e);
                                        Toast.makeText(AddGuestListActivity.this, "Error creating guest", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddGuestListActivity", "Error adding guest document", e);
                        Toast.makeText(AddGuestListActivity.this, "Error creating guest", Toast.LENGTH_SHORT).show();
                    }
                });
                    }


                    private void showAllGuestsFragment() {
                        db.collection("guestList").get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        ArrayList<GuestList> guestList = new ArrayList<>();
                                        for (DocumentSnapshot document : task.getResult()) {
                                            GuestList guest = document.toObject(GuestList.class);
                                            guestList.add(guest);
                                        }

                                        GuestListFragment guestListFragment = GuestListFragment.newInstance(guestList);
                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.guest_container, guestListFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    } else {
                                        Log.w("AddGuestListActivity", "Error getting documents.", task.getException());
                                    }
                                });
                    }

                }

