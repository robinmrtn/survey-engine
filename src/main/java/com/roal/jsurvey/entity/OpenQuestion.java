package com.roal.jsurvey.entity;

public class OpenQuestion implements SurveyElement{

    private String question;
    private int position;

    public OpenQuestion(String question) {
        this.question = question;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getPosition() {
        return position;
    }

    public String getQuestion() {
        return question;
    }
}
