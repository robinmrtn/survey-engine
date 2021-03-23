package com.roal.jsurvey.entity;

import javax.persistence.*;

@Entity
public class ElementResponse {

    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    SurveyResponse surveyResponse;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    AbstractSurveyElement element;

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

    public AbstractSurveyElement getElement() {
        return element;
    }

    public void setElement(AbstractSurveyElement element) {
        this.element = element;
    }

}
