package com.roal.jsurvey.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SurveyPagePosition implements Comparable<SurveyPagePosition> {

    @Id
    @GeneratedValue
    private long id;

    private int position;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AbstractSurveyElement surveyElement;

    public SurveyPagePosition() {
    }

    public SurveyPagePosition(long id, int position, AbstractSurveyElement surveyElement, SurveyPage surveyPage) {
        this.id = id;
        this.position = position;
        this.surveyElement = surveyElement;
    }

    public SurveyPagePosition(int position, AbstractSurveyElement surveyElement) {
        this.position = position;
        this.surveyElement = surveyElement;
    }

    public int getPosition() {
        return position;
    }

    public AbstractSurveyElement getSurveyElement() {
        return surveyElement;
    }

    public void setSurveyElement(ClosedQuestion surveyElement) {
        this.surveyElement = surveyElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyPagePosition that = (SurveyPagePosition) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(SurveyPagePosition o) {
        return Integer.compare(this.position, o.getPosition());
    }
}
