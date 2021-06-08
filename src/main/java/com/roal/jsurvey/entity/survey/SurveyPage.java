package com.roal.jsurvey.entity.survey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roal.jsurvey.entity.AbstractSurveyElement;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class SurveyPage {

    @Id
    @GeneratedValue
    private long id;

    private int position;

    @JsonIgnore
    @ManyToOne
    private Survey survey;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "surveyPage")
    private Set<AbstractSurveyElement> surveyPageElements = new TreeSet<>();

    public SurveyPage() {
    }

    public SurveyPage(long id, int position, Set<AbstractSurveyElement> surveyPageElement) {
        this.id = id;
        this.position = position;
        this.surveyPageElements = surveyPageElement;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSurveyPageElement(Set<AbstractSurveyElement> surveyPageElements) {
        this.surveyPageElements = surveyPageElements;
    }

    public Set<AbstractSurveyElement> getSurveyPageElements() {
        return surveyPageElements;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSurveyPageElements(Set<AbstractSurveyElement> surveyPageElements) {
        this.surveyPageElements = surveyPageElements;
    }

    public void addSurveyElement(AbstractSurveyElement surveyElement) {
        surveyPageElements.add(surveyElement);
        surveyElement.setSurveyPage(this);
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyPage that = (SurveyPage) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
