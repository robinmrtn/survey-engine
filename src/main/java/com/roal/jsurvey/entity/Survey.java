package com.roal.jsurvey.entity;

import java.util.LinkedList;
import java.util.List;

public class Survey {

    private String description;

    private DateRange dateRange;
    private List<SurveyPage> surveyPageList = new LinkedList<>();

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

    public void addSurveyPage(SurveyPage surveyPage) {
        surveyPageList.add(surveyPage);
    }

    public List<SurveyPage> getSurveyPageList() {
        return surveyPageList;
    }
}
