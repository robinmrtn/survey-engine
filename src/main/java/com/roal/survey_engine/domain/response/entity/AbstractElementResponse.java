package com.roal.survey_engine.domain.response.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "element_response")
public abstract class AbstractElementResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_response_element")
    @SequenceGenerator(name = "seq_response_element")
    protected Long id;

    @ManyToOne
    private SurveyResponse surveyResponse;

    public AbstractElementResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SurveyResponse getSurveyResponse() {
        return surveyResponse;
    }

    public void setSurveyResponse(SurveyResponse surveyResponse) {
        this.surveyResponse = surveyResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractElementResponse that = (AbstractElementResponse) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
