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

    @OneToMany(targetEntity = AbstractSurveyElement.class, cascade = CascadeType.ALL, mappedBy = "surveyPage")
    private List<AbstractSurveyElement> surveyElements = new LinkedList<>();


    public SurveyPage() {
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

}
