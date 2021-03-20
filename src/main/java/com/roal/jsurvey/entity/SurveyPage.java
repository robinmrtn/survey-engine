package com.roal.jsurvey.entity;

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

    @OneToMany(targetEntity = SurveyPagePosition.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "survey_page_id")
    private final List<SurveyPagePosition> surveyPagePositions = new ArrayList<>();

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
        return id == that.id && position == that.position && Objects.equals(surveyPagePositions, that.surveyPagePositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, surveyPagePositions);
    }
}
