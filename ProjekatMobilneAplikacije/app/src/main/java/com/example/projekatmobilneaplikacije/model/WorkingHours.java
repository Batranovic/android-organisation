package com.example.projekatmobilneaplikacije.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Map;

public class WorkingHours  implements Serializable {
    private Map<String, DayWorkingHours> dayWorkingHoursMap;

    public WorkingHours(){}

    public WorkingHours(Map<String, DayWorkingHours> dayWorkingHoursMap) {
        this.dayWorkingHoursMap = dayWorkingHoursMap;
    }

    public Map<String, DayWorkingHours> getDayWorkingHoursMap() {
        return dayWorkingHoursMap;
    }

    public void setDayWorkingHoursMap(Map<String, DayWorkingHours> dayWorkingHoursMap) {
        this.dayWorkingHoursMap = dayWorkingHoursMap;
    }
}


