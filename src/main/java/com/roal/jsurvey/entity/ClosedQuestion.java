package com.roal.jsurvey.entity;


public class ClosedQuestion extends AbstractSurveyElement implements SurveyElement{

    private String question;
    private int position;


    public ClosedQuestion() {
    }

    public ClosedQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public int getPosition() {
        return position;
    }

}
