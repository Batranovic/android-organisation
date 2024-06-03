package com.example.projekatmobilneaplikacije.activities.agenda;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.AddAgendaBinding;
import com.example.projekatmobilneaplikacije.model.Agenda;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AddAgendaActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private AddAgendaBinding binding;
    private TimePicker start;
    private TimePicker end;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button createButton;
    private Button generatePdfButton;

    private String getTimeFromPicker(TimePicker timePicker) {
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        return String.format("%02d:%02d", hour, minute);
    }

    public static AddAgendaActivity newInstance() {
        return new AddAgendaActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddAgendaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();
        start = root.findViewById(R.id.time_picker_activity_start);
        end = root.findViewById(R.id.time_picker_activity_end);
        EditText agendaName = root.findViewById(R.id.edit_text_activity_name);
        EditText agendaDescription = root.findViewById(R.id.edit_text_activity_description);
        EditText agendaLocation = root.findViewById(R.id.edit_text_activity_location);

        createButton = root.findViewById(R.id.button_add_activity);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAgendaToFirestore(agendaName, agendaDescription, agendaLocation);
            }
        });

        generatePdfButton = root.findViewById(R.id.button_generate_pdf);
        generatePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    fetchAgendasAndGeneratePdf();
                } else {
                    requestPermission();
                }
            }
        });
    }

    private void clearInputFields() {
        EditText agendaName = findViewById(R.id.edit_text_activity_name);
        EditText agendaDescription = findViewById(R.id.edit_text_activity_description);
        EditText agendaLocation = findViewById(R.id.edit_text_activity_location);
        TimePicker start = findViewById(R.id.time_picker_activity_start);
        TimePicker end = findViewById(R.id.time_picker_activity_end);

        agendaName.setText("");
        agendaDescription.setText("");
        agendaLocation.setText("");
        start.setCurrentHour(0);
        start.setCurrentMinute(0);
        end.setCurrentHour(0);
        end.setCurrentMinute(0);
    }

    private void saveAgendaToFirestore(EditText agendaName, EditText agendaDescription, EditText agendaLocation) {
        String name = agendaName.getText().toString();
        String description = agendaDescription.getText().toString();
        String location = agendaLocation.getText().toString();
        String startTime = getTimeFromPicker(start);
        String endTime = getTimeFromPicker(end);

        Agenda agenda = new Agenda();
        agenda.setName(name);
        agenda.setDescription(description);
        agenda.setLocation(location);
        agenda.setTimeFrom(startTime);
        agenda.setTimeTo(endTime);

        db.collection("addAgenda")
                .add(agenda)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AddAgendaActivity", "Agenda added with ID: " + documentReference.getId());
                        Toast.makeText(AddAgendaActivity.this, "Agenda created successfully", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddAgendaActivity", "Error adding agenda document", e);
                        Toast.makeText(AddAgendaActivity.this, "Error creating agenda", Toast.LENGTH_SHORT).show();
                    }
                });
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
                fetchAgendasAndGeneratePdf();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchAgendasAndGeneratePdf() {
        db.collection("addAgenda")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Agenda> agendas = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Agenda agenda = document.toObject(Agenda.class);
                                agendas.add(agenda);
                            }
                            try {
                                generatePdf(agendas);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.w("AddAgendaActivity", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void generatePdf(List<Agenda> agendas) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDoc);
        Paragraph title = new Paragraph("Agenda for event")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);
       // document.add(new LineSeparator(new SolidLine()).setMarginBottom(10));

        for (Agenda agenda : agendas) {
            document.add(new Paragraph("Name: " + agenda.getName()));
            document.add(new Paragraph("Description: " + agenda.getDescription()));
            document.add(new Paragraph("Location: " + agenda.getLocation()));
            document.add(new Paragraph("Start Time: " + agenda.getTimeFrom()));
            document.add(new Paragraph("End Time: " + agenda.getTimeTo()));
            document.add(new Paragraph("\n"));
        }

        document.close();

        savePdfToFile(byteArrayOutputStream.toByteArray());
    }

    private void savePdfToFile(byte[] pdfBytes) throws IOException {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/agendas.pdf";
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(pdfBytes);
        fos.close();

        Toast.makeText(this, "PDF generated and saved to Downloads", Toast.LENGTH_SHORT).show();
    }
}
