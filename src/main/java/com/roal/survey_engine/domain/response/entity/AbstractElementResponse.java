package com.roal.survey_engine.domain.response.entity;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "element_response")
public abstract class AbstractElementResponse {

    @Id
    @GeneratedValue
    protected long id;

    @ManyToOne
    private SurveyResponse surveyResponse;

    public AbstractElementResponse() {
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
}
