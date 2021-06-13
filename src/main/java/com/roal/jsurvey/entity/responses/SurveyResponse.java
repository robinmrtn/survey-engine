package com.roal.jsurvey.entity.responses;

import com.roal.jsurvey.entity.survey.Campaign;
import com.roal.jsurvey.entity.survey.Survey;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class SurveyResponse {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Survey survey;

    @ManyToOne
    private Campaign campaign;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyResponse")
    private List<AbstractElementResponse> elementResponses;

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
        this.elementResponses = elementResponses;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
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
