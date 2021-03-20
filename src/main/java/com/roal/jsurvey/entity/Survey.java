package com.roal.jsurvey.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Survey {

    @Id
    @GeneratedValue
    private long id;

    private String description;

    private DateRange dateRange;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "survey_id")
    private final List<SurveyPage> surveyPages = new LinkedList<>();

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

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateRange != null ? dateRange.hashCode() : 0);
        result = 31 * result + (surveyPages != null ? surveyPages.hashCode() : 0);
        return result;
    }
}
