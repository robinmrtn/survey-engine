package com.roal.jsurvey.entity;

import java.util.List;

public class Survey {

    private String description;

    private DateRange dateRange;
    private List<SurveyPage> surveyPageList;

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
