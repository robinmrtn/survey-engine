package com.roal.jsurvey.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class SurveyResponse {

    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    Survey survey;

    @OneToMany
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
}
