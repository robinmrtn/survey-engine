package com.roal.jsurvey.entity;

import javax.persistence.Entity;


@Entity
public class ClosedQuestion extends AbstractSurveyElement {

    private String question;

    public ClosedQuestion() {
    }

    public ClosedQuestion(String question, SurveyPagePosition surveyPagePosition) {
        this.question = question;

    }

    public ClosedQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }


}
