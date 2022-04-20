package com.roal.survey_engine.domain.response.entity;

import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class SurveyResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_response")
    @SequenceGenerator(name = "seq_response")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean deleted = Boolean.FALSE;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyResponse", fetch = FetchType.EAGER)
    private List<AbstractElementResponse> elementResponses = new ArrayList<>();

    public SurveyResponse() {
        // needed by hibernate
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public SurveyResponse setSurvey(Survey survey) {
        this.survey = survey;
        return this;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
