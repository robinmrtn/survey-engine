package com.roal.jsurvey.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
