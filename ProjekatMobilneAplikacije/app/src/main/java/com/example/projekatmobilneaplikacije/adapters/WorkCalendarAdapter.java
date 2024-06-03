package com.example.projekatmobilneaplikacije.adapters;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.model.Event;
import com.example.projekatmobilneaplikacije.model.WorkCalendar;

import java.text.SimpleDateFormat;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WorkCalendarAdapter extends ArrayAdapter<WorkCalendar> {
    private List<WorkCalendar> workCalendarList;
    private Context mContext;
    public WorkCalendarAdapter(Context context,List<WorkCalendar> workCalendarList) {
        super(context, 0, workCalendarList);
        this.workCalendarList = workCalendarList;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_work_calendar, parent, false);
        }

        WorkCalendar workCalendar = workCalendarList.get(position);

        TextView workStartTextView = convertView.findViewById(R.id.workStartTextView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        StringBuilder eventInfo = new StringBuilder();
        boolean hasReservedEvents = false;

        List<Event> events = workCalendar.getEvents();
        if (events != null && !events.isEmpty()) {
            for (Event event : events) {
                if (event.getType().equals("reserved")) {
                    hasReservedEvents = true;
                    eventInfo.append("Event Start: ").append(dateFormat.format(event.getStartTime())).append("\n");
                    eventInfo.append("Event End: ").append(dateFormat.format(event.getEndTime())).append("\n");
                    eventInfo.append("Status: ").append(event.getType()).append("\n");
                    eventInfo.append("-------------\n");
                }
            }
        }

        if (!hasReservedEvents) {
            eventInfo.append("Free whenever on this date");
        }

        workStartTextView.setText(eventInfo.toString());

        return convertView;
    }


}

