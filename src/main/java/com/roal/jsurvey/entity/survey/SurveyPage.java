package com.roal.jsurvey.entity.survey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roal.jsurvey.entity.AbstractSurveyElement;
import com.roal.jsurvey.entity.questions.OpenQuestion;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    private List<AbstractSurveyElement> surveyPageElements = new LinkedList<>();

    public SurveyPage() {
    }

    public SurveyPage(long id, int position, List<AbstractSurveyElement> surveyPageElement) {
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

    public void setSurveyPageElement(List<AbstractSurveyElement> surveyPageElements) {
        this.surveyPageElements = surveyPageElements;
    }

    public List<AbstractSurveyElement> getSurveyPageElements() {
        return surveyPageElements;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSurveyPageElements(List<AbstractSurveyElement> surveyPageElements) {
        this.surveyPageElements = surveyPageElements;
    }

    public void addSurveyElement(OpenQuestion surveyElement) {
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
