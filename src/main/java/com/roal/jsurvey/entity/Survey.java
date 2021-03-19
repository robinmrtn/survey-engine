package com.roal.jsurvey.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document
public class Survey {

    private final List<SurveyPage> surveyPages = new ArrayList<>();

    private String description;

    private DateRange dateRange;
    @Id
    private String id;

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
        surveyPages.add(surveyPage);
    }

    public List<SurveyPage> getSurveyPages() {
        return surveyPages;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survey survey = (Survey) o;
        return Objects.equals(id, survey.id) && Objects.equals(description, survey.description) && Objects.equals(surveyPages, survey.surveyPages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, surveyPages);
    }
}
