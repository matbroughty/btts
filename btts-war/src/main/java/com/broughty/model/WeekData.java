package com.broughty.model;

import java.util.Date;

/**
 * Created by matbroughty on 05/01/14.
 */
public class WeekData {

    String weekNumber;

    Date startDate;

    Date endDate;

    public WeekData() {
    }

    public WeekData(String weekNumber, Date startDate, Date endDate) {
        this.weekNumber = weekNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(String weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}



