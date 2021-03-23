package com.roal.jsurvey.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
public class SurveyPage {

    @Id
    @GeneratedValue
    private long id;

    private int position;

    @OneToMany(targetEntity = SurveyPagePosition.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "survey_page_id")
    private final List<SurveyPagePosition> surveyPagePositions = new LinkedList<>();

    public SurveyPage() {
    }

    public List<SurveyPagePosition> getSurveyPagePositions() {
        return surveyPagePositions;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addSurveyElement(SurveyPagePosition surveyElement) {
        surveyPagePositions.add(surveyElement);
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
