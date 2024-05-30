package com.example.projekatmobilneaplikacije.model;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class WorkCalendar {
    private String employee;
    private Date day;
    private Time workStart;
    private Time workEnd;
    private List<Event> events;

    public WorkCalendar() {
    }
}
