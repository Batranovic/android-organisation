package com.example.projekatmobilneaplikacije.fragments;

import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.projekatmobilneaplikacije.R;


import com.example.projekatmobilneaplikacije.model.BudgetItem;
import com.example.projekatmobilneaplikacije.model.Category;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanBudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanBudgetFragment extends Fragment {

    public static ArrayList<Category> categories = new ArrayList<Category>();
    public static ArrayList<BudgetItem> budgetItems = new ArrayList<BudgetItem>();


    public PlanBudgetFragment() {
        // Required empty public constructor
    }

    public static PlanBudgetFragment newInstance() {
        PlanBudgetFragment fragment = new PlanBudgetFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        return view;


    }

        private void showPopupWindow (View anchorView){
            View popupView = getLayoutInflater().inflate(R.layout.buttom_fillter, null);

            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
            );


            popupWindow.showAsDropDown(anchorView);
        }

    }
