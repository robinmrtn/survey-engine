package com.roal.survey_engine.entity.survey;

import com.roal.survey_engine.entity.question.AbstractSurveyElement;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class SurveyPage {

    @Id
    @GeneratedValue
    private long id;

    private int position;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AbstractSurveyElement> surveyPageElements = new ArrayList<>();

    public SurveyPage() {
        // needed by Hibernate
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<AbstractSurveyElement> getSurveyPageElements() {
        return surveyPageElements;
    }

    public int getPosition() {
        return position;
    }

    public SurveyPage setPosition(int position) {
        this.position = position;
        return this;
    }

    public SurveyPage addSurveyElement(AbstractSurveyElement surveyElement) {
        surveyPageElements.add(surveyElement);
//   surveyElement.setSurveyPage(this);
        return this;
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
