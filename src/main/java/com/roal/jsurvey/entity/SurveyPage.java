package com.roal.jsurvey.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class SurveyPage {

    @Id
    @GeneratedValue
    private long id;

    private int position;

    @OneToMany(targetEntity = SurveyPagePosition.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "survey_page_id")
    private final Set<SurveyPagePosition> surveyPagePositions = new TreeSet<>();

    public SurveyPage() {
    }

    public Set<SurveyPagePosition> getSurveyPagePositions() {
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
