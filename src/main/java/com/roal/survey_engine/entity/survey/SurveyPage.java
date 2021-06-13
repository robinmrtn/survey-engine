package com.roal.survey_engine.entity.survey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roal.survey_engine.entity.questions.AbstractSurveyElement;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "surveyPage", orphanRemoval = true)
    private Set<AbstractSurveyElement> surveyPageElements = new TreeSet<>();

    public SurveyPage() {
        // needed by Hibernate
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public SurveyPage addSurveyElement(AbstractSurveyElement surveyElement) {
        surveyPageElements.add(surveyElement);
        surveyElement.setSurveyPage(this);
        return this;
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
