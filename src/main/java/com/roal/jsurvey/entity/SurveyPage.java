package com.roal.jsurvey.entity;

import java.util.LinkedList;
import java.util.List;

public class SurveyPage {

    private int position;
    private List<SurveyElement> surveyElementList = new LinkedList<>();


    public SurveyPage() {
    }

    public List<SurveyElement> getSurveyElementList() {
        return surveyElementList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addSurveyElement(SurveyElement surveyElement) {
        surveyElementList.add(surveyElement);
    }
}
