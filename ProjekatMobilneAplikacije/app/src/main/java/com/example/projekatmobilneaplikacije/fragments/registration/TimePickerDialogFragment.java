package com.example.projekatmobilneaplikacije.fragments.registration;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.FragmentTimePickerDialogBinding;
import com.example.projekatmobilneaplikacije.model.DayWorkingHours;
import com.example.projekatmobilneaplikacije.model.WorkingHours;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class TimePickerDialogFragment extends Fragment {

    private FragmentTimePickerDialogBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView mondayDayTextView, tuesdayDayTextView, wednesdayDayTextView, thursdayDayTextView, fridayDayTextView, saturdayDayTextView, sundayDayTextView;
    private TimePicker mondayStartClock, mondayEndClock, tuesdayStartClock, tuesdayEndClock, wednesdayStartClock, wednesdayEndClock, thursdayStartClock, thursdayEndClock, fridayStartClock, fridayEndClock, saturdayStartClock, saturdayEndClock, sundayStartClock, sundayEndClock;
    private Button saveButton;
    public static TimePickerDialogFragment newInstance() {
        return new TimePickerDialogFragment();
    }
    public interface WorkingHoursListener {
        void onWorkingHoursSet(WorkingHours workingHours);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTimePickerDialogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mondayDayTextView = root.findViewById(R.id.monday_day);
        tuesdayDayTextView = root.findViewById(R.id.tuesday_day);
        wednesdayDayTextView = root.findViewById(R.id.wednesday_day);
        thursdayDayTextView = root.findViewById(R.id.thursday_day);
        fridayDayTextView = root.findViewById(R.id.friday_day);
        saturdayDayTextView = root.findViewById(R.id.saturday_day);
        sundayDayTextView = root.findViewById(R.id.sunday_day);

        mondayStartClock = root.findViewById(R.id.monday_start_clock);
        mondayEndClock = root.findViewById(R.id.monday_end_clock);
        tuesdayStartClock = root.findViewById(R.id.tuesday_start_clock);
        tuesdayEndClock = root.findViewById(R.id.tuesday_end_clock);
        wednesdayStartClock = root.findViewById(R.id.wednesday_start_clock);
        wednesdayEndClock = root.findViewById(R.id.wednesday_end_clock);
        thursdayStartClock = root.findViewById(R.id.thursday_start_clock);
        thursdayEndClock = root.findViewById(R.id.thursday_end_clock);
        fridayStartClock = root.findViewById(R.id.friday_start_clock);
        fridayEndClock = root.findViewById(R.id.friday_end_clock);
        saturdayStartClock = root.findViewById(R.id.saturday_start_clock);
        saturdayEndClock = root.findViewById(R.id.saturday_end_clock);
        sundayStartClock = root.findViewById(R.id.sunday_start_clock);
        sundayEndClock = root.findViewById(R.id.sunday_end_clock);
        binding.buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();

                int mondayHour = mondayStartClock.getHour();
                int mondayMinute = mondayStartClock.getMinute();
                Time mondayStartTime = new Time(mondayHour, mondayMinute, 0);
                int mondayEndHour = mondayEndClock.getHour();
                int mondayEndMinute = mondayEndClock.getMinute();
                Time mondayEndTime = new Time(mondayEndHour, mondayEndMinute, 0);

                int tuesdayHour = tuesdayStartClock.getHour();
                int tuesdayMinute = tuesdayStartClock.getMinute();
                Time tuesdayStartTime = new Time(tuesdayHour, tuesdayMinute, 0);
                int tuesdayEndHour = tuesdayEndClock.getHour();
                int tuesdayEndMinute = tuesdayEndClock.getMinute();
                Time tuesdayEndTime = new Time(tuesdayEndHour, tuesdayEndMinute, 0);

                int wednesdayHour = wednesdayStartClock.getHour();
                int wednesdaMinute = wednesdayStartClock.getMinute();
                Time wednesdayStartTime = new Time(wednesdayHour, wednesdaMinute, 0);
                int wednsedayEndHour = wednesdayEndClock.getHour();
                int wednesdayEndMinute = wednesdayEndClock.getMinute();
                Time wednesdayEndTime = new Time(wednsedayEndHour, wednesdayEndMinute, 0);
                int thursdayHour = thursdayStartClock.getHour();
                int thursdayMinute = thursdayStartClock.getMinute();
                Time thursdayStartTime = new Time(thursdayHour, thursdayMinute, 0);
                int thursdayEndHour = thursdayEndClock.getHour();
                int thursdayEndMinute = thursdayEndClock.getMinute();
                Time thursdayEndTime = new Time(thursdayEndHour, thursdayEndMinute, 0);

                int fridayHour = fridayStartClock.getHour();
                int fridayMinute = fridayStartClock.getMinute();
                Time fridayStartTime = new Time(fridayHour, fridayMinute, 0);
                int fridayEndHour = fridayEndClock.getHour();
                int fridayEndMinute = fridayEndClock.getMinute();
                Time fridayEndTime = new Time(fridayEndHour, fridayEndMinute, 0);

                int saturdayHour = saturdayStartClock.getHour();
                int saturdayMinute = saturdayStartClock.getMinute();
                Time saturdayStartTime = new Time(saturdayHour, saturdayMinute, 0);
                int saturdayEndHour = saturdayEndClock.getHour();
                int saturdayEndMinute = saturdayEndClock.getMinute();
                Time saturdayEndTime = new Time(saturdayEndHour, saturdayEndMinute, 0);

                int sundayHour = sundayStartClock.getHour();
                int sundayMinute = sundayStartClock.getMinute();
                Time sundayStartTime = new Time(sundayHour, sundayMinute, 0);
                int sundayEndHour = sundayEndClock.getHour();
                int sundayEndMinute = sundayEndClock.getMinute();
                Time sundayEndTime = new Time(sundayEndHour, sundayEndMinute, 0);

                // Stvoriti objekte DayWorkingHours za svaki dan
                DayWorkingHours mondayWorkingHours = new DayWorkingHours(mondayStartTime, mondayEndTime);
                DayWorkingHours tuesdayWorkingHours = new DayWorkingHours(tuesdayStartTime, tuesdayEndTime);
                DayWorkingHours wednesdayWorkingHours = new DayWorkingHours(wednesdayStartTime, wednesdayEndTime);
                DayWorkingHours thursdayWorkingHours = new DayWorkingHours(thursdayStartTime, thursdayEndTime);
                DayWorkingHours fridayWorkingHours = new DayWorkingHours(fridayStartTime, fridayEndTime);
                DayWorkingHours saturdayWorkingHours = new DayWorkingHours(saturdayStartTime, saturdayEndTime);
                DayWorkingHours sundayWorkingHours = new DayWorkingHours(sundayStartTime, sundayEndTime);

                // Mapa za spremanje radnih sati za svaki dan u tjednu
                Map<String, DayWorkingHours> dayWorkingHoursMap = new HashMap<>();
                dayWorkingHoursMap.put("Monday", mondayWorkingHours);
                dayWorkingHoursMap.put("Tuesday", tuesdayWorkingHours);
                dayWorkingHoursMap.put("Wednesday", wednesdayWorkingHours);
                dayWorkingHoursMap.put("Thursday", thursdayWorkingHours);
                dayWorkingHoursMap.put("Friday", fridayWorkingHours);
                dayWorkingHoursMap.put("Saturday", saturdayWorkingHours);
                dayWorkingHoursMap.put("Sunday", sundayWorkingHours);

                // Stvoriti objekt WorkingHours sa svim radnim satima za tjedan
                WorkingHours workingHours = new WorkingHours(dayWorkingHoursMap);
                if (workingHoursListener != null) {
                    workingHoursListener.onWorkingHoursSet(workingHours);
                }

                db.collection("working_hours")
                        .document("week_hours")
                        .set(workingHours)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Podaci o radnim satima su uspješno spremljeni u Firestore!");
                                // Ovdje možete dodati akcije koje želite poduzeti nakon uspješnog spremanja
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Greška prilikom spremanja podataka o radnim satima u Firestore", e);
                                // Ovdje možete dodati postupke u slučaju neuspješnog spremanja
                            }
                        });

                Bundle args = new Bundle();
                args.putSerializable("working_hours", (Serializable) workingHours);

                RegisterFragment registerFragment = RegisterFragment.newInstance();
                registerFragment.setArguments(args);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_registration_container, registerFragment)
                        .commit();
            }
        });

        return root;
    }
    private WorkingHoursListener workingHoursListener;

    public void setWorkingHoursListener(WorkingHoursListener listener) {
        this.workingHoursListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
