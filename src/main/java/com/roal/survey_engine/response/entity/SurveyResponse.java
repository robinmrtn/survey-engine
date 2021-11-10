package com.roal.survey_engine.response.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roal.survey_engine.survey.entity.Campaign;
import com.roal.survey_engine.survey.entity.Survey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class SurveyResponse {

    @Id
    @GeneratedValue
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyResponse", fetch = FetchType.EAGER)
    private List<AbstractElementResponse> elementResponses = new ArrayList<>();

    public SurveyResponse() {
        // needed by hibernate
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

    public List<AbstractElementResponse> getElementResponses() {
        return elementResponses;
    }

    public void setElementResponses(List<AbstractElementResponse> elementResponses) {
        elementResponses.forEach(e -> e.setSurveyResponse(this));
        this.elementResponses = elementResponses;
    }

    public SurveyResponse addElement(AbstractElementResponse elementResponse) {
        elementResponses.add(elementResponse);
        elementResponse.setSurveyResponse(this);
        return this;
    }

    public SurveyResponse setCampaign(Campaign campaign) {
        this.campaign = campaign;
        return this;
    }

    public Campaign getCampaign() {
        return campaign;
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
