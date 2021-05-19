package com.roal.jsurvey.entity.survey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Survey {

    @Id
    @GeneratedValue
    private long id;

    private String description;

    private DateRange dateRange;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "survey", orphanRemoval = true)
    private List<SurveyPage> surveyPages = new ArrayList<>();

    public Survey() {
    }

    public Survey(long id, String description, DateRange dateRange, List<SurveyPage> surveyPages) {
        this.id = id;
        this.description = description;
        this.dateRange = dateRange;
        this.surveyPages = surveyPages;
    }


    public void setId(long id) {
        this.id = id;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public void setSurveyPages(List<SurveyPage> surveyPages) {
        this.surveyPages = surveyPages;
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
        surveyPage.setSurvey(this);
    }

    public List<SurveyPage> getSurveyPages() {
        return surveyPages;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survey survey = (Survey) o;
        return id == survey.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
