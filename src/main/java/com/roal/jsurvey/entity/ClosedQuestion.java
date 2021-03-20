package com.roal.jsurvey.entity;

import javax.persistence.Entity;
import java.util.Objects;


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClosedQuestion that = (ClosedQuestion) o;
        return Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question);
    }
}
