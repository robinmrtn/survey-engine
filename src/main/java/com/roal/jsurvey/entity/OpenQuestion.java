package com.roal.jsurvey.entity;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class OpenQuestion extends AbstractSurveyElement {


    private String question;

    public OpenQuestion() {
    }


    public OpenQuestion(String question) {
        this.question = question;
    }

    public OpenQuestion(long id, String question) {
        this.question = question;
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenQuestion that = (OpenQuestion) o;
        return Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question);
    }
}
