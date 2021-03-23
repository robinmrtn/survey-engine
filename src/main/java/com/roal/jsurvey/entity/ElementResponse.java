package com.roal.jsurvey.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ElementResponse {

    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    SurveyResponse surveyResponse;

    String value;

    public ElementResponse() {
    }

    public ElementResponse(long id, SurveyResponse surveyResponse, String value) {
        this.id = id;
        this.surveyResponse = surveyResponse;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SurveyResponse getSurveyResponse() {
        return surveyResponse;
    }

    public void setSurveyResponse(SurveyResponse surveyResponse) {
        this.surveyResponse = surveyResponse;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
