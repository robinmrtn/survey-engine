package com.roal.jsurvey.entity;

public class Survey {

    private String description;

    private DateRange dateRange;

    public Survey() {
    }

    public Survey(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
