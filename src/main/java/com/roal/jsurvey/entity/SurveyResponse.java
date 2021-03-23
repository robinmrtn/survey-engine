package com.roal.jsurvey.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class SurveyResponse {

    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    Survey survey;

    @OneToMany(cascade = CascadeType.ALL)
    List<ElementResponse> elementResponses;

    public SurveyResponse() {
    }

    public SurveyResponse(long id, Survey survey, List<ElementResponse> elementResponses) {
        this.id = id;
        this.survey = survey;
        this.elementResponses = elementResponses;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public List<ElementResponse> getElementResponses() {
        return elementResponses;
    }

    public void setElementResponses(List<ElementResponse> elementResponses) {
        this.elementResponses = elementResponses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyResponse that = (SurveyResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
