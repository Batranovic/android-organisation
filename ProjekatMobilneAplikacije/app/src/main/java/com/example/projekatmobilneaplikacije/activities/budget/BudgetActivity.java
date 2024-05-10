package com.example.projekatmobilneaplikacije.activities.budget;

import static android.app.PendingIntent.getActivity;
import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;


import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeProfileActivity;
import com.example.projekatmobilneaplikacije.activities.events.NewEventActivity;
import com.example.projekatmobilneaplikacije.databinding.ActivityEventBudgetBinding;
import com.example.projekatmobilneaplikacije.fragments.EventListFragment;
import com.example.projekatmobilneaplikacije.fragments.FragmentTransition;
import com.example.projekatmobilneaplikacije.fragments.ListSubCatForEventFragment;
import com.example.projekatmobilneaplikacije.model.CreateEvent;
import com.example.projekatmobilneaplikacije.model.Subcategory;
import com.example.projekatmobilneaplikacije.model.SubcategorySuggestion;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BudgetActivity extends AppCompatActivity {
    ActivityEventBudgetBinding binding;
    public static ArrayList<Subcategory> suggestions = new ArrayList<>();

    public static BudgetActivity newInstance(ArrayList<Subcategory> suggestions) {
       return new BudgetActivity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBudgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



       FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("subcategories")
                .get()
                .addOnCompleteListener(task-> {
                    if(task.isSuccessful()){
                        suggestions.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Subcategory addsug = document.toObject(Subcategory.class);
                            suggestions.add(addsug);
                        }
                        FragmentTransition.to(ListSubCatForEventFragment.newInstance(suggestions), BudgetActivity.this,
                                false, R.id.list_subcat);

                    }
                    else{
                        Log.d(TAG,"Error getting documents", task.getException());
                    }
                });


        FragmentTransition.to(ListSubCatForEventFragment.newInstance(suggestions), BudgetActivity.this,
                false, R.id.list_subcat);







        Button finishButton = binding.btnAddNewItem;
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BudgetActivity.this, PlanBudgetActivity.class);
                startActivity(intent);
            }
        });

    }


}

