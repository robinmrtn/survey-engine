package com.roal.jsurvey.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class SurveyPage {

    @Id
    @GeneratedValue
    private long id;

    private int position;

    @OneToMany()
    @JoinColumn(name = "surveypage_id")
    private List<OpenQuestion> surveyElements = new LinkedList<>();


    public SurveyPage() {
    }

    public List<OpenQuestion> getSurveyElements() {
        return surveyElements;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addSurveyElement(OpenQuestion surveyElement) {
        surveyElements.add(surveyElement);
    }
}
