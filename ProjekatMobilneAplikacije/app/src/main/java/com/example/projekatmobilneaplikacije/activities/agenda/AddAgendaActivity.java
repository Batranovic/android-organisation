package com.example.projekatmobilneaplikacije.activities.agenda;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekatmobilneaplikacije.R;

import com.example.projekatmobilneaplikacije.databinding.AddAgendaBinding;
import com.example.projekatmobilneaplikacije.model.Agenda;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddAgendaActivity extends AppCompatActivity {
   private AddAgendaBinding binding;

    private TimePicker start;

    private TimePicker end;

    //FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static AddAgendaActivity newInstance() {
        return new AddAgendaActivity();
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddAgendaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();
        start = root.findViewById(R.id.time_picker_activity_start);
        end = root.findViewById(R.id.time_picker_activity_end);
        EditText agendaName = root.findViewById(R.id.edit_text_activity_name);
        EditText agendaDescription = root.findViewById(R.id.edit_text_activity_description);
        EditText agendaLocation = root.findViewById(R.id.edit_text_activity_location);



        Button createButton = root.findViewById(R.id.button_add_activity);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAgendaToFirestore(agendaName, agendaDescription, agendaLocation);
                String message = "Created Succesfully";
                //Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void saveAgendaToFirestore(EditText agendaName, EditText agendaDescription, EditText agendaLocation) {
        String name = agendaName.getText().toString();
        String description = agendaDescription.getText().toString();
        String location = agendaLocation.getText().toString();
        String startTime = start.toString();
        String endTime = end.toString();


        // Sada možete dodati ovu vrijednost u CreateEvent objekt
        Agenda agenda = new Agenda();


        agenda.setName(name);
        agenda.setDescription(description);
        agenda.setLocation(location);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("addAgenda")
                .add(agenda)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AddAgendaFragment", "AddAgenda added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddAgendaFragment", "Error adding AddAgenda document", e);
                    }
                });

    }
}
