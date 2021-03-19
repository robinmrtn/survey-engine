package com.roal.jsurvey.entity;


import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class SurveyPage {


    private long id;

    private int position;


    private List<AbstractSurveyElement> surveyElements = new LinkedList<>();


    public SurveyPage() {
    }

    public SurveyPage(long id, int position, List<AbstractSurveyElement> surveyElements) {
        this.id = id;
        this.position = position;
        this.surveyElements = surveyElements;
    }

    public List<AbstractSurveyElement> getSurveyElements() {
        return surveyElements;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addSurveyElement(AbstractSurveyElement surveyElement) {
        surveyElements.add(surveyElement);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyPage that = (SurveyPage) o;
        return id == that.id && position == that.position && Objects.equals(surveyElements, that.surveyElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, surveyElements);
    }
}
