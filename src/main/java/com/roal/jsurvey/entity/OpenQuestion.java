package com.roal.jsurvey.entity;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class OpenQuestion extends AbstractSurveyElement implements SurveyElement{


    private String question;
    private int position;

    public OpenQuestion() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenQuestion that = (OpenQuestion) o;
        return position == that.position && Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, position);
    }
}
